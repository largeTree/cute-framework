package com.qiuxs.cuteframework.core.basic.i18n;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties
@PropertySource(value = { "classpath:msg/msg.properties" }, ignoreResourceNotFound = true)
@Component
public class MessageResourceHolder {

	private Map<String, String> cn = new HashMap<>();
	private Map<String, String> en = new HashMap<>();

	public Map<String, String> getCn() {
		return cn;
	}

	public void setCn(Map<String, String> cn) {
		this.cn = cn;
	}

	public Map<String, String> getEn() {
		return en;
	}

	public void setEn(Map<String, String> en) {
		this.en = en;
	}

}
