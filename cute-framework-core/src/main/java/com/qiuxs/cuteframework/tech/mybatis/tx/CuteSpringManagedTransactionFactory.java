package com.qiuxs.cuteframework.tech.mybatis.tx;

import javax.sql.DataSource;

import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;

public class CuteSpringManagedTransactionFactory extends SpringManagedTransactionFactory {

	@Override
	public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
		return new CuteSpringManagedTransaction(dataSource);
	}

}
