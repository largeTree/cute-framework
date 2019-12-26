package com.qiuxs.cuteframework.tech.mybatis.tx;

import java.io.IOException;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;

/**
 * 替换SpringManagedTransactionFactory类型替换成自定义的EcSpringManagedTransactionFactory
 * @author qiuxs
 *
 */
public class CuteSqlSessionFactoryBean extends SqlSessionFactoryBean {

	@Override
	protected SqlSessionFactory buildSqlSessionFactory() throws IOException {
		CuteSpringManagedTransactionFactory transactionFactory = new CuteSpringManagedTransactionFactory();
		super.setTransactionFactory(transactionFactory);
		return super.buildSqlSessionFactory();
	}

}
