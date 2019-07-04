package com.qiuxs.cuteframework.tech.mybatis.config;

import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = MybatisProperties.MYBATIS_PREFIX)
public class MyBatisConfig {
	/** 是否自动刷新Mapper文件 */
	private boolean autoRefresh;

	/** Mapper文件路径 */
	private String[] mapperLocations;

	public boolean isAutoRefresh() {
		return autoRefresh;
	}

	public void setAutoRefresh(boolean autoRefresh) {
		this.autoRefresh = autoRefresh;
	}

	public String[] getMapperLocations() {
		return mapperLocations;
	}

	public void setMapperLocations(String[] mapperLocations) {
		this.mapperLocations = mapperLocations;
	}

}
