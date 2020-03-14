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
public abstract class AbstractSMSCaptchaSender<S extends ISMSSener> implements ISMSCaptchaSender {

	/**
	 * 短信发送器实例
	 */
	private S smsSender;

	/**
	 * 根据短信发送器直接构造验证码发送器
	 * @param sender
	 * 2019年3月28日 下午11:58:38
	 * @author qiuxs
	 */
	public AbstractSMSCaptchaSender(S sender) {
		this.smsSender = sender;
	}

	/**
	 * 获取短信发送器
	 * 
	 * 2019年3月28日 下午11:58:49
	 * @auther qiuxs
	 * @return
	 */
	protected S getSmsSender() {
		return smsSender;
	}

	/**
	 * 设置短信发送器
	 * 
	 * 2019年3月28日 下午11:58:57
	 * @auther qiuxs
	 * @param smsSender
	 */
	protected void setSmsSender(S smsSender) {
		this.smsSender = smsSender;
	}

	/**
	 * 2019年3月28日 下午11:59:33
	 * qiuxs
	 * @see com.qiuxs.captcha.sms.ISMSCaptchaSender#sendCaptchaByTemplate(java.lang.String, java.lang.String, java.util.Map)
	 */
	@Override
	public abstract void sendCaptchaByTemplate(String signName, String mobile, String templateId, Map<String, String> params, String captcha);

}
