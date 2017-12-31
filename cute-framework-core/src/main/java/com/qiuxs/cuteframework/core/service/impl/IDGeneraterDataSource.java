package com.qiuxs.cuteframework.core.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.core.service.IDGeneraterable;

@Service("IDGeneraterDataSource")
public class IDGeneraterDataSource implements IDGeneraterable {

	private static final String PLACE_HOLDER = "{tableName}";

	private static final String CREATE_SEQ_TABLE = "CREATE TABLE `seq_" + PLACE_HOLDER + "`(`id` BIGINT(20) PRIMARY KEY AUTO_INCREMENT,`std` VARCHAR(1) NOT NULL,UNIQUE KEY `UK_std`(`std`))";

	private static final String CHECK_EXISTS = "SELECT COUNT(1) FROM information_schema.`TABLES` WHERE table_name = 'seq_" + PLACE_HOLDER + "'";

	private static final String GENERATER_REPLACE = "REPLACE INTO `seq_" + PLACE_HOLDER + "`(`std`) VALUES('a');";

	private static final String GET_ID = "SELECT LAST_INSERT_ID()";

	@Resource
	private BasicDataSource dataSource;

	@Override
	public Object getNextId(String tableName) {
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			Statement stat = conn.createStatement();
			rs = stat.executeQuery(CHECK_EXISTS.replace(PLACE_HOLDER, tableName));
			boolean createFlag = false;
			while (rs.next()) {
				long count = rs.getLong(1);
				createFlag = count == 0;
			}
			rs.close();
			if (createFlag) {
				stat.executeUpdate(CREATE_SEQ_TABLE.replace(PLACE_HOLDER, tableName));
			}
			stat.executeUpdate(GENERATER_REPLACE.replace(PLACE_HOLDER, tableName));
			rs = stat.executeQuery(GET_ID);
			Long id = null;
			while (rs.next()) {
				id = rs.getLong(1);
			}
			return id;
		} catch (SQLException e) {
			throw new RuntimeException(e.getLocalizedMessage(), e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				throw new RuntimeException(e.getLocalizedMessage(), e);
			}
		}
	}

	private Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

}
