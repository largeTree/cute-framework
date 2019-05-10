package com.qiuxs.cuteframework.core.persistent.database.lookup;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import com.qiuxs.cuteframework.tech.spring.tx.CuteDataSourceTransactionManager;
import com.qiuxs.cuteframework.tech.spring.tx.CuteTransactionInterceptor;

@Component
@Configuration
public class DynamicDataSourceConfig implements EnvironmentAware {

	private static Logger log = LogManager.getLogger(DynamicDataSourceConfig.class);

	private static final String DATA_SOURCE_CONFIG_PREFIX = "spring.datasource.";

	private BasicDataSource defaultDataSource;

	private Environment environment;

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
		this.initDefaultDataSource();
	}

	private void initDefaultDataSource() {
		this.defaultDataSource = new BasicDataSource();
		this.defaultDataSource.setUrl(this.getStringEnv(DATA_SOURCE_CONFIG_PREFIX + "url"));
		this.defaultDataSource.setUsername(this.getStringEnv(DATA_SOURCE_CONFIG_PREFIX + "username"));
		this.defaultDataSource.setPassword(this.getStringEnv(DATA_SOURCE_CONFIG_PREFIX + "password"));
		this.defaultDataSource.setDriverClassName(this.getStringEnv(DATA_SOURCE_CONFIG_PREFIX + "driver-class-name"));
		this.defaultDataSource.setInitialSize(this.getTypeEnv(DATA_SOURCE_CONFIG_PREFIX + "dbcp2.initial-size", int.class));
		this.defaultDataSource.setMaxTotal(this.getTypeEnv(DATA_SOURCE_CONFIG_PREFIX + "dbcp2.max-total", int.class));
		this.defaultDataSource.setMinIdle(this.getTypeEnv(DATA_SOURCE_CONFIG_PREFIX + "dbcp2.min-idle", int.class));
		this.defaultDataSource.setMaxWaitMillis(this.getTypeEnv(DATA_SOURCE_CONFIG_PREFIX + "dbcp2.max-wait-millis", long.class));
		this.defaultDataSource.setValidationQuery(this.getStringEnv(DATA_SOURCE_CONFIG_PREFIX + "dbcp2.validation-query"));

		try {
			this.defaultDataSource.getConnection().close();
		} catch (Exception e) {
			log.info("Connection to Entry Error, ext = " + e.getLocalizedMessage(), e);
			throw new RuntimeException(e);
		}
	}

	private <T> T getTypeEnv(String key, Class<T> clz) {
		return this.environment.getProperty(key, clz);
	}

	private String getStringEnv(String key) {
		return this.environment.getProperty(key);
	}

	@Bean("dataSource")
	public DataSource dataSource() {
		DynamicDataSource dynamicDataSource = new DynamicDataSource();
		dynamicDataSource.setDefaultTargetDataSource(this.defaultDataSource);
		return dynamicDataSource;
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
