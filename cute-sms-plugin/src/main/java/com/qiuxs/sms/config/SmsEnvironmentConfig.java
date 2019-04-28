package com.qiuxs.sms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 
 * 验证码插件配置
 * 
 * @author qiuxs
 * 2019年4月2日 下午10:50:33
 */
@Component
@ConfigurationProperties(prefix = SmsEnvironmentConfig.PREFIX)
public class SmsEnvironmentConfig {

	protected static final String PREFIX = "cute.sms";

	private String aliSmsDomain = "dysmsapi.aliyuncs.com";

	public String getAliSmsDomain() {
		return aliSmsDomain;
	}

	public void setAliSmsDomain(String aliSmsDomain) {
		this.aliSmsDomain = aliSmsDomain;
	}

}
