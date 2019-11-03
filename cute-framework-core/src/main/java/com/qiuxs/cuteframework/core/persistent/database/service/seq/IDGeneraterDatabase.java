package com.qiuxs.cuteframework.core.persistent.database.service.seq;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.persistent.database.lookup.DynamicDataSource;
import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDGeneraterable;

@Service
public class IDGeneraterDatabase implements IDGeneraterable {

	private static Logger log = LogManager.getLogger(IDGeneraterDatabase.class);

	private Set<String> seqTables = new HashSet<>();

	private static final String GET_ALL_SEQ_TABLE_SQL = "SELECT TABLE_NAME FROM information_schema.`TABLES` WHERE TABLE_COMMENT = 'seq' AND TABLE_SCHEMA = SCHEMA()";

	private static final String CK_SQL = "SELECT COUNT(1) FROM information_schema.`TABLES` WHERE TABLE_NAME = ''seq_{0}'' AND TABLE_SCHEMA = SCHEMA()";

	private static final String CREATE_SQL = "CREATE TABLE `seq_{0}`(`id` BIGINT(20) PRIMARY KEY AUTO_INCREMENT,`sub` VARCHAR(1) NOT NULL,UNIQUE KEY `UK_seq_{0}`(`sub`))COMMENT''seq''";

	private static final String SEQ_SQL = "REPLACE INTO `seq_{0}`(sub)VALUES(''a'');";

	private static final String GET_ID_SQL = "SELECT LAST_INSERT_ID()";

	private DynamicDataSource dataSource;

	@Override
	public Long getNextId(String tableName) {
		DataSource seqDateSource = dataSource.getTargetDataSources().get(dataSource.getSeqDb());
		Connection conn = null;
		Statement stam = null;
		ResultSet ckRs = null;
		ResultSet idRs = null;
		try {
			conn = seqDateSource.getConnection();
			stam = conn.createStatement();
			// 先检查缓存中是否存在本序列表
			if (!seqTables.contains(StringUtils.append("seq_", tableName))) {
				// 再检查一下数据库中是否存在此序列表
				ckRs = stam.executeQuery(MessageFormat.format(CK_SQL, tableName));
				int tbCount = 0;
				while (ckRs.next()) {
					tbCount = ckRs.getInt(1);
				}
				if (tbCount == 0) {
					stam.executeUpdate(MessageFormat.format(CREATE_SQL, tableName));
				}
				// 到了这一步 肯定有序列表了 添加进缓存
				seqTables.add(tableName);
			}
			stam.executeUpdate(MessageFormat.format(SEQ_SQL, tableName));
			idRs = stam.executeQuery(GET_ID_SQL);
			Long nextId = 0L;
			while (idRs.next()) {
				nextId = idRs.getLong(1);
			}
			return nextId;
		} catch (Exception e) {
			log.error("getNextId Error ext=" + e.getLocalizedMessage(), e);
			throw ExceptionUtils.unchecked(e);
		} finally {
			this.close(idRs);
			this.close(ckRs);
			this.close(stam);
			this.close(conn);
		}
	}

	@Resource
	public void setDataSource(DynamicDataSource dataSource) {
		this.dataSource = dataSource;
		this.initAllSeqTables();
	}

	/**
	 * 获取所有序列表，存入缓存
	 * @author qiuxs
	 *
	 *
	 * 创建时间：2018年8月9日 下午10:10:42
	 */
	private void initAllSeqTables() {
		Connection conn = null;
		Statement stam = null;
		ResultSet rs = null;
		try {
			DataSource seqDateSource = this.dataSource.getTargetDataSources().get(dataSource.getSeqDb());
			conn = seqDateSource.getConnection();
			stam = conn.createStatement();
			rs = stam.executeQuery(GET_ALL_SEQ_TABLE_SQL);
			while (rs.next()) {
				this.seqTables.add(rs.getString(1));
			}
		} catch (Exception e) {
			log.error("getNextId Error ext=" + e.getLocalizedMessage(), e);
			throw ExceptionUtils.unchecked(e);
		} finally {
			this.close(rs);
			this.close(stam);
			this.close(conn);
		}
	}

	private void close(AutoCloseable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception e) {
				log.error("close error ext=" + e.getLocalizedMessage(), e);
			}
		}
	}

}
