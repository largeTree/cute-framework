package com.qiuxs.cuteframework.tech.spring.tx;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;

public class CuteDataSourceTransactionManager extends DataSourceTransactionManager {
	
	public CuteDataSourceTransactionManager(DataSource dataSource) {
		super(dataSource);
	}

	private static final long serialVersionUID = 6741445513190998495L;

}
