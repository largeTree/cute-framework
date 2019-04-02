package com.qiuxs.captcha.config;

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
@ConfigurationProperties(prefix = CaptchaEnvironmentConfig.PREFIX)
public class CaptchaEnvironmentConfig {

	protected static final String PREFIX = "env.captcha";

	/** 默认超时时间 */
	private Long defaultTimeLimit = 60L;
	/** 默认拉黑理由 */
	private String defaultBalcklistReason = "In Blacklist";
	/** 短信验证码占位符 */
	private String capchaPlaceholder = "captcha";

	public Long getDefaultTimeLimit() {
		return defaultTimeLimit;
	}

	public void setDefaultTimeLimit(Long defaultTimeLimit) {
		this.defaultTimeLimit = defaultTimeLimit;
	}

	public String getDefaultBalcklistReason() {
		return defaultBalcklistReason;
	}

	public void setDefaultBalcklistReason(String defaultBalcklistReason) {
		this.defaultBalcklistReason = defaultBalcklistReason;
	}

	public String getCapchaPlaceholder() {
		return capchaPlaceholder;
	}

	public void setCapchaPlaceholder(String capchaPlaceholder) {
		this.capchaPlaceholder = capchaPlaceholder;
	}

}
