package com.qiuxs.captcha.sms.impl;

import java.util.Map;

import com.qiuxs.cuteframework.core.basic.utils.MapUtils;
import com.qiuxs.sms.sender.impl.AlibabaSMSSender;

/**
 * 阿里云短信发送器
 * 
 * @author qiuxs
 * 2019年3月28日 下午11:37:27
 */
public class AlibabaSMSCaptchaSender extends AbstractSMSCaptchaSender<AlibabaSMSSender> {

	/**
	 * 直接使用短信发送器构造
	 * @param sender
	 * 2019年3月28日 下午11:47:20
	 * @author qiuxs
	 */
	public AlibabaSMSCaptchaSender(AlibabaSMSSender sender) {
		super(sender);
	}

	@Override
	public void sendCaptchaByTemplate(String signName, String mobile, String templateId, Map<String, String> params, String captcha) {
		this.getSmsSender().sendSMSByTemplate(signName, mobile, templateId, MapUtils.genStringMap("captcha"));
	}

}
