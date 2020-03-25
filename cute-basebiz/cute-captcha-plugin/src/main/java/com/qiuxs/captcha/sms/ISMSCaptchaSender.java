package com.qiuxs.captcha.sms;

import java.util.Map;

/**
 * 短信验证码发送器
 * @author qiuxs
 * 2019年3月28日 下午11:14:41
 */
public interface ISMSCaptchaSender {

	/**
	 * 根据模板发送验证码短信
	 * 
	 * 2019年3月28日 下午11:17:57
	 * @auther qiuxs
	 * @param mobile
	 * 		手机号
	 * @param template
	 * 		短信模板id
	 * @param params
	 * 		额外的参数
	 * @param captcha
	 * 		验证码
	 */
	public void sendCaptchaByTemplate(String signName, String mobile, String templateId, Map<String,String> params, String captcha);

}
