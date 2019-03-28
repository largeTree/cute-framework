package com.qiuxs.captcha.sms.impl;

import java.util.Map;

import com.qiuxs.captcha.sms.ISMSCaptchaSender;

/**
 * 阿里云短信发送器
 * 
 * @author qiuxs
 * 2019年3月28日 下午11:37:27
 */
public class AlibabaSMSCaptchaSender implements ISMSCaptchaSender {

	@Override
	public void sendCaptcha(String mobile, String content) {
		throw new UnsupportedOperationException("Alibaba SMS Service not suppored send by content");
	}

	@Override
	public void sendCaptchaByTemplate(String mobile, String templateId, Map<String, String> params) {
		
	}

}
