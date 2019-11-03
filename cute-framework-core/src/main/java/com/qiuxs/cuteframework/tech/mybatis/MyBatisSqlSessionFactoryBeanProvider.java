package com.qiuxs.cuteframework.tech.mybatis;

import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.qiuxs.cuteframework.tech.mybatis.tx.CuteSqlSessionFactoryBean;

@Component
public class MyBatisSqlSessionFactoryBeanProvider {

	private MybatisProperties mybatisProperties;

	@Resource
	public void setMybatisProperties(MybatisProperties mybatisProperties) {
		this.mybatisProperties = mybatisProperties;
	}

	private ResourceLoader resourceLoader;

	@Resource
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	private DatabaseIdProvider databaseIdProvider;

	@Resource
	public void setDatabaseIdProvider(ObjectProvider<DatabaseIdProvider> databaseIdProvider) {
		this.databaseIdProvider = databaseIdProvider.getIfAvailable();
	}

	private List<ConfigurationCustomizer> configurationCustomizers;

	@Resource
	public void setConfigurationCustomizersProvider(ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider) {
		this.configurationCustomizers = configurationCustomizersProvider.getIfAvailable();
	}

	private Interceptor[] interceptors;

	@Resource
	public void setInterceptorsProvider(ObjectProvider<Interceptor[]> interceptorsProvider) {
		this.interceptors = interceptorsProvider.getIfAvailable();
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean factory = new CuteSqlSessionFactoryBean();
		factory.setDataSource(dataSource);
		factory.setVfs(SpringBootVFS.class);
		if (StringUtils.hasText(this.mybatisProperties.getConfigLocation())) {
			factory.setConfigLocation(this.resourceLoader.getResource(this.mybatisProperties.getConfigLocation()));
		}
		Configuration configuration = this.mybatisProperties.getConfiguration();
		if (configuration == null && !StringUtils.hasText(this.mybatisProperties.getConfigLocation())) {
			configuration = new Configuration();
		}
		if (configuration != null && !CollectionUtils.isEmpty(this.configurationCustomizers)) {
			for (ConfigurationCustomizer customizer : this.configurationCustomizers) {
				customizer.customize(configuration);
			}
		}
		factory.setConfiguration(configuration);
		if (this.mybatisProperties.getConfigurationProperties() != null) {
			factory.setConfigurationProperties(this.mybatisProperties.getConfigurationProperties());
		}
		if (!ObjectUtils.isEmpty(this.interceptors)) {
			factory.setPlugins(this.interceptors);
		}
		if (this.databaseIdProvider != null) {
			factory.setDatabaseIdProvider(this.databaseIdProvider);
		}
		if (StringUtils.hasLength(this.mybatisProperties.getTypeAliasesPackage())) {
			factory.setTypeAliasesPackage(this.mybatisProperties.getTypeAliasesPackage());
		}
		if (StringUtils.hasLength(this.mybatisProperties.getTypeHandlersPackage())) {
			factory.setTypeHandlersPackage(this.mybatisProperties.getTypeHandlersPackage());
		}
		if (!ObjectUtils.isEmpty(this.mybatisProperties.resolveMapperLocations())) {
			factory.setMapperLocations(this.mybatisProperties.resolveMapperLocations());
		}

		return factory.getObject();
	}

}
