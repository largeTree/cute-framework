package com.qiuxs.sms.sender.impl;

import java.util.Map;

import com.qiuxs.cuteframework.core.basic.config.UConfigUtils;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.MapUtils;
import com.qiuxs.cuteframework.core.basic.utils.http.HttpClientUtil;
import com.qiuxs.sms.sender.ISMSSener;

public class UpyunSMSSender implements ISMSSener {
	
	public static final String CONFIG_DOMAIN = "upyun";
	
	@Override
	public void sendSMS(String signName, String mobile, String content) {
		ExceptionUtils.throwRuntimeException("Unsupported");
	}

	@Override
	public void sendSMSByTemplate(String signName, String mobile, String templateId, Map<String, String> params) {
		params.put("template_id", templateId);
		params.put("mobile", mobile);
		HttpClientUtil.doPostRetJson(this.getUrl(), params, MapUtils.genStringMap("Authorization", this.getAuthorization()), true);
	}

	private String getAuthorization() {
		return UConfigUtils.getDomain(CONFIG_DOMAIN).getString("sms.authorization");
	}
	
	private String getUrl() {
		return UConfigUtils.getDomain(CONFIG_DOMAIN).getString("sms.url");
	}
	
}
