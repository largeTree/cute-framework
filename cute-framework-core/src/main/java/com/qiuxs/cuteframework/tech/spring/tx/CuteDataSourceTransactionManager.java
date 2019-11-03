package com.qiuxs.cuteframework.tech.spring.tx;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;

public class CuteDataSourceTransactionManager extends DataSourceTransactionManager {
	
	private static final long serialVersionUID = 6741445513190998495L;
	
	public CuteDataSourceTransactionManager(DataSource dataSource) {
		super(dataSource);
	}

}
