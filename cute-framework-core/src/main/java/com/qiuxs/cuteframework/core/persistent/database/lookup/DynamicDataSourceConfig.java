package com.qiuxs.cuteframework.core.persistent.database.lookup;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import com.qiuxs.cuteframework.tech.spring.tx.CuteDataSourceTransactionManager;
import com.qiuxs.cuteframework.tech.spring.tx.CuteTransactionInterceptor;

@Component
@ConfigurationProperties(prefix = DynamicDataSourceConfig.PREFIX)
public class DynamicDataSourceConfig {

	protected static final String PREFIX = "spring.datasource";

	private static Logger log = LogManager.getLogger(DynamicDataSourceConfig.class);

	private String url;
	private String username;
	private String password;
	private String driverClassName;
	private Dbcp2Config dbcp2;

	private BasicDataSource defaultDataSource;

	private void initDefaultDataSource() {
		this.defaultDataSource = new BasicDataSource();
		this.defaultDataSource.setUrl(this.url);
		this.defaultDataSource.setUsername(this.username);
		this.defaultDataSource.setPassword(this.password);
		this.defaultDataSource.setDriverClassName(this.driverClassName);
		this.defaultDataSource.setInitialSize(this.dbcp2.getInitialSize());
		this.defaultDataSource.setMaxTotal(this.dbcp2.getMaxTotal());
		this.defaultDataSource.setMinIdle(this.dbcp2.getMinIdle());
		this.defaultDataSource.setMaxWaitMillis(this.dbcp2.getMaxWaitMillis());
		this.defaultDataSource.setValidationQuery(this.dbcp2.getValidationQuery());

		try {
			this.defaultDataSource.getConnection().close();
		} catch (Exception e) {
			log.info("Connection to Entry Error, ext = " + e.getLocalizedMessage(), e);
			throw new RuntimeException(e);
		}
	}

	@Bean("dataSource")
	public DataSource dataSource() {
		this.initDefaultDataSource();
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public Dbcp2Config getDbcp2() {
		return dbcp2;
	}

	public void setDbcp2(Dbcp2Config dbcp2) {
		this.dbcp2 = dbcp2;
	}

	public static class Dbcp2Config {
		private int initialSize;
		private int maxTotal;
		private int minIdle;
		private long maxWaitMillis;
		private String validationQuery;

		public int getInitialSize() {
			return initialSize;
		}

		public void setInitialSize(int initialSize) {
			this.initialSize = initialSize;
		}

		public int getMaxTotal() {
			return maxTotal;
		}

		public void setMaxTotal(int maxTotal) {
			this.maxTotal = maxTotal;
		}

		public int getMinIdle() {
			return minIdle;
		}

		public void setMinIdle(int minIdle) {
			this.minIdle = minIdle;
		}

		public long getMaxWaitMillis() {
			return maxWaitMillis;
		}

		public void setMaxWaitMillis(long maxWaitMillis) {
			this.maxWaitMillis = maxWaitMillis;
		}

		public String getValidationQuery() {
			return validationQuery;
		}

		public void setValidationQuery(String validationQuery) {
			this.validationQuery = validationQuery;
		}

	}

}
