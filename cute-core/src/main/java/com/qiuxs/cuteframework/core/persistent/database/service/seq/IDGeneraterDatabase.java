package com.qiuxs.cuteframework.core.persistent.database.service.seq;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.persistent.database.lookup.DynamicDataSource;
import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDGeneraterable;

@Service
public class IDGeneraterDatabase implements IDGeneraterable {

	private static Logger log = LogManager.getLogger(IDGeneraterDatabase.class);

	private static final String CREATE_SQL = "CREATE TABLE `seq_{0}`(`id` BIGINT(20) PRIMARY KEY AUTO_INCREMENT,`sub` VARCHAR(1) NOT NULL,UNIQUE KEY `UK_seq_{0}`(`sub`))ENGINE=MYISAM COMMENT''seq''";

	private static final String SEQ_SQL = "REPLACE INTO `seq_{0}`(sub)VALUES(''a'');";

	private static final String GET_ID_SQL = "SELECT LAST_INSERT_ID()";

	private DynamicDataSource dataSource;

	@Override
	public Long getNextId(String seqName) {
		DataSource seqDateSource = dataSource.getTargetDataSources().get(dataSource.getSeqDb());
		Connection conn = null;
		Statement stam = null;
		ResultSet ckRs = null;
		ResultSet idRs = null;
		try {
			conn = seqDateSource.getConnection();
			stam = conn.createStatement();
			Long nextId = this.getNextId(stam, seqName);
			return nextId;
		} catch (Throwable e) {
			String message = e.getMessage();
			if (message != null && message.indexOf("doesn't exist") >= 0) {
				//如果表不存在，创建表后重新获取序列
				try {
					createSequenceTable(stam, seqName);
					return getNextId(stam, seqName);
				} catch (Throwable e1) {
					log.error("create SeqTable Error ext=" + e1.getLocalizedMessage(), e1);
					throw ExceptionUtils.unchecked(e);
				}
			} else {
				log.error("getNextId Error ext=" + e.getLocalizedMessage(), e);
				throw ExceptionUtils.unchecked(e);
			}
		} finally {
			this.close(idRs);
			this.close(ckRs);
			this.close(stam);
			this.close(conn);
		}
	}

	private void createSequenceTable(Statement stam, String seqName) throws SQLException {
		stam.execute(MessageFormat.format(CREATE_SQL, seqName));
	}

	private Long getNextId(Statement stam, String seqName) throws SQLException {
		stam.execute(MessageFormat.format(SEQ_SQL, seqName));
		ResultSet idRs = stam.executeQuery(GET_ID_SQL);
		if (idRs.next()) {
			long nextId = idRs.getLong(1);
			return nextId;
		} else {
			throw new RuntimeException("get[" + seqName + "]Next Id Failed ");
		}
	}

	@Resource
	public void setDataSource(DynamicDataSource dataSource) {
		this.dataSource = dataSource;
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
