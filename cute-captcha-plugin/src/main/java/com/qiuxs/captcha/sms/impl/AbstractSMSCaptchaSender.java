package com.qiuxs.captcha.sms.impl;

import java.util.Map;

import com.qiuxs.captcha.sms.ISMSCaptchaSender;
import com.qiuxs.sms.sender.ISMSSener;

/**
 * 抽象短信验证码发送器
 * @author qiuxs
 * 2019年3月28日 下午11:54:28
 * @param <S>
 */
public abstract class AbstractSMSCaptchaSender<S extends ISMSSener> implements ISMSCaptchaSender<S> {

	private S smsSender;

	public AbstractSMSCaptchaSender(S sender) {
		this.smsSender = sender;
	}

	protected S getSmsSender() {
		return smsSender;
	}

	protected void setSmsSender(S smsSender) {
		this.smsSender = smsSender;
	}

	@Override
	public void sendCaptcha(String mobile, String content) {
		this.getSmsSender().sendSMS(mobile, content);
	}

	@Override
	public void sendCaptchaByTemplate(String mobile, String templateId, Map<String, String> params) {
		this.getSmsSender().sendSMSByTemplate(mobile, templateId, params);
	}

}
