package com.qiuxs.captcha.sms;

import java.util.Map;

/**
 * 短信验证码发送器
 * @author qiuxs
 * 2019年3月28日 下午11:14:41
 */
public interface ISMSCaptchaSender {

	/**
	 * 发送验证码短信
	 * 
	 * 2019年3月28日 下午11:14:48
	 * @auther qiuxs
	 * @param mobile
	 * 		手机号
	 * @param content
	 * 		验证码短信内容
	 */
	public void sendCaptcha(String mobile, String content);

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
	 * 		模板参数
	 */
	public void sendCaptchaByTemplate(String mobile, String templateId, Map<String, String> params);

}
