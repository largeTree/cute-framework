package com.qiuxs.captcha.service;

import java.util.Map;

/**
 * 验证码组合服务类
 * 
 * @author qiuxs
 * 2019年3月28日 下午11:03:15
 */
public interface ICaptchaCombService {

	/**
	 * 根据短信模板发送验证码
	 * 
	 * 2019年3月28日 下午11:27:06
	 * @auther qiuxs
	 * @param mobile
	 * 		手机号
	 * @param templateId
	 * 		模板id
	 * @param params
	 * 		模板参数
	 */
	public void sendMobileCaptchaByTemplate(String signName, String mobile, String templateId, Map<String, String> params);
	
	/**
	 * 验证验证码是否正确
	 * 
	 * 2019年3月28日 下午11:08:28
	 * @auther qiuxs
	 * @param mobile
	 * 		手机号
	 * @param captcha
	 * 		验证码
	 */
	public boolean verifyCaptcha(String mobile, String captcha);
	
	/**
	 * 验证验证码是否正确
	 * 
	 * 2019年3月28日 下午11:09:26
	 * @auther qiuxs
	 * @param mobile
	 * 		手机号
	 * @param captcha
	 * 		验证码
	 * @param invalidFlag
	 * 		是否失效验证码
	 */
	public boolean verifyCaptcha(String mobile, String captcha, boolean invalidFlag);

	/**
	 * 生成一个验证码
	 *  
	 * @author qiuxs  
	 * @param mobile
	 * @return
	 */
	public String getCaptcha(String mobile);

}
