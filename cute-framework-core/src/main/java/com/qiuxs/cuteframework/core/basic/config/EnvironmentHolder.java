package com.qiuxs.cuteframework.core.basic.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 环境配置
 * @author qiuxs
 *
 */
@ConfigurationProperties(prefix = "environment")
public class EnvironmentHolder {

	/** 当前运行环境 */
	private String currentEnv;

	private Map<String, Map<String, String>> envProps = new HashMap<String, Map<String, String>>();

	public String getCurrentEnv() {
		return currentEnv;
	}

	public void setCurrentEnv(String currentEnv) {
		this.currentEnv = currentEnv;
	}

	public Map<String, Map<String, String>> getEnvProps() {
		return envProps;
	}

	public void setEnvProps(Map<String, Map<String, String>> envProps) {
		this.envProps = envProps;
	}

	public String getEnvProp(String key) {
		return getMap().get(key);
	}

	private Map<String, String> getMap() {
		return this.envProps.get(this.getCurrentEnv());
	}
}