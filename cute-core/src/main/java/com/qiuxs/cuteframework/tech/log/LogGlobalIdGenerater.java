package com.qiuxs.cuteframework.tech.log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.persistent.database.lookup.DataSourceContext;

@Service
@Deprecated
public class LogGlobalIdGenerater {

	private static Logger log = LogManager.getLogger(LogGlobalIdGenerater.class);

	@Resource
	private DataSource dataSource;

	public Long nextId() {
		String logDb = DataSourceContext.getLogDb();
		if (StringUtils.isBlank(logDb)) {
			return 0L;
		}
		String oldDsId = DataSourceContext.setLogDb();
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			stat = conn.createStatement();
			stat.executeUpdate("REPLACE INTO `log_global_id_seq`(`std`)VALUES('a')");
			rs = stat.executeQuery("SELECT LAST_INSERT_ID();");
			while (rs.next()) {
				return rs.getLong(1);
			}
		} catch (Exception e) {
			log.error("gen Next Log Global Id Error ext=" + e.getLocalizedMessage(), e);
		} finally {
			DataSourceContext.setUpDs(oldDsId);
			close(rs);
			close(stat);
			close(conn);
		}
		return 0L;
	}

	private void close(AutoCloseable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (Exception e) {
			log.error("close Error ext=" + e.getLocalizedMessage(), e);
		}
	}

}
