package com.qiuxs.cuteframework.tech.spring.config;

import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.qiuxs.cuteframework.core.basic.config.IConfiguration;
import com.qiuxs.cuteframework.core.basic.config.UConfigUtils;

/**
 * 自定义配置项，支持在applicationContext.xml中读取properties文件
 * @author qiuxs
 *
 */
public class CutePropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	private static Logger log = LogManager.getLogger(CutePropertyPlaceholderConfigurer.class);
	
	private Properties ctxProperties = new Properties();

	@Override
	protected void loadProperties(Properties props) throws IOException {
		super.loadProperties(props);
		IConfiguration domain = UConfigUtils.getDomain(UConfigUtils.DOMAIN_DB);
		props.putAll(domain.toMap());
		ctxProperties.putAll(props);
		log.info("db.driver = " + props.getProperty("db.driver") + ", db.url = " + props.getProperty("db.url") + ", db.user = " + props.getProperty("db.user"));
	}

	/***
	 * 获取上下文properties值
	 * 
	 * @param key
	 * @return
	 */
	public String getCtxProperty(String key) {
		return this.ctxProperties.getProperty(key);
	}
}
