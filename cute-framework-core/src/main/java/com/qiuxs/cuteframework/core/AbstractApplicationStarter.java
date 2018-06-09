package com.qiuxs.cuteframework.core;

import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import com.qiuxs.cuteframework.tech.spring.tx.CuteDataSourceTransactionManager;
import com.qiuxs.cuteframework.tech.spring.tx.CuteTransactionInterceptor;

//SpringBoot 默认仅扫面当前包及子包下的类，如果在其他包下存在需要扫面的类 则需要手动指定
@EnableTransactionManagement
@ServletComponentScan("com.qiuxs")
@ComponentScan(basePackages = { "com.qiuxs" })
@MapperScan(basePackages = { "com.qiuxs.**.dao" })
public abstract class AbstractApplicationStarter {

	/**
	 * 调用此方法启动服务
	 * 
	 * @param startClass
	 * @param args
	 */
	protected static final void run(Class<?> startClass, String[] args) {
		SpringApplication application = new SpringApplication(startClass);
		application.run(args);
	}

	@Bean("transactionManager")
	public PlatformTransactionManager txManager(DataSource dataSource) {
		return getPlatformTransactionManager(dataSource);
	}

	/**
	 * 给子类机会，使用自定义事务管理器
	 * 
	 * @param dataSource
	 * @return
	 */
	protected PlatformTransactionManager getPlatformTransactionManager(DataSource dataSource) {
		return new CuteDataSourceTransactionManager(dataSource);
	}

	@Bean("transactionInterceptor")
	public TransactionInterceptor transactionInterceptor(PlatformTransactionManager platformTransactionManager,
			AnnotationTransactionAttributeSource attributes) {
		return getTransactionInterceptor(platformTransactionManager, attributes);
	}

	/**
	 * 给子类机会，使用自定义事务拦截器
	 * 
	 * @param platformTransactionManager
	 * @param attributes
	 * @return
	 */
	protected TransactionInterceptor getTransactionInterceptor(PlatformTransactionManager platformTransactionManager,
			AnnotationTransactionAttributeSource attributes) {
		return new CuteTransactionInterceptor(platformTransactionManager, attributes);
	}

}
