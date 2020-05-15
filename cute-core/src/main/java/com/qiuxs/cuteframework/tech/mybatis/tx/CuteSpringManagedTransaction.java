package com.qiuxs.cuteframework.tech.mybatis.tx;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.transaction.Transaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.qiuxs.cuteframework.core.persistent.database.lookup.DynamicDataSource;
import com.qiuxs.cuteframework.core.tx.local.SpringTxContext;
import com.qiuxs.cuteframework.tech.mybatis.MyBatisManager;

public class CuteSpringManagedTransaction implements Transaction {

	private static Logger log = LogManager.getLogger(CuteSpringManagedTransaction.class);

	private final DataSource dataSource;

	private Connection connection;

	private boolean isConnectionTransactional;

	private boolean autoCommit;

	public CuteSpringManagedTransaction(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Connection getConnection() throws SQLException {
		if (!SpringTxContext.isTransactional() || SpringTxContext.isTxDsId()) {
			if (this.connection == null) {
				this.openConnection();
			}
			return this.connection;
		} else {
			Connection con = MyBatisManager.getConnection();
			if ( con == null || con.isClosed()) {
				con = dataSource.getConnection();
				MyBatisManager.setConnection(con);
			}
			return con;
		}
	}

	@Override
	public void commit() throws SQLException {
		if (this.connection != null && !isConnectionTransactional && !this.autoCommit) {
			if (log.isDebugEnabled()) {
				log.debug("Committing JDBC Connection [" + this.connection + "]");
			}
			this.connection.commit();
		}
	}

	@Override
	public void rollback() throws SQLException {
		if (this.connection != null && !isConnectionTransactional && !this.autoCommit) {
			if (log.isDebugEnabled()) {
				log.debug("Rolling back JDBC Connection [" + this.connection + "]");
			}
			this.connection.rollback();
		}
	}

	@Override
	public void close() throws SQLException {
		DataSourceUtils.releaseConnection(this.connection, this.dataSource);
	}

	@Override
	public Integer getTimeout() throws SQLException {
		ConnectionHolder holder = (ConnectionHolder) TransactionSynchronizationManager.getResource(dataSource);
		if (holder != null && holder.hasTimeout()) {
			return holder.getTimeToLiveInSeconds();
		}
		return null;
	}

	/**
	 * 开启链接
	 * @throws SQLException
	 */
	private void openConnection() throws SQLException {
		DataSource ds = DynamicDataSource.getDynamicDataSource();
		if (!ds.equals(this.dataSource)) {
			log.error("DataSource Not Consistency!!! DynamicDataSource = " + ds + ", EcSpringManagedTransaction.dataSource=" + this.dataSource);
		}
		this.connection = DataSourceUtils.getConnection(this.dataSource);
		this.autoCommit = this.connection.getAutoCommit();
		this.isConnectionTransactional = DataSourceUtils.isConnectionTransactional(this.connection, this.dataSource);

		if (log.isDebugEnabled()) {
			log.debug("JDBC Connection [" + this.connection + "] will" + (this.isConnectionTransactional ? " " : " not ") + "be managed by Spring");
		}
	}

}
