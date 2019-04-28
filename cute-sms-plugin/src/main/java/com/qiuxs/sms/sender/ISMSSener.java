package com.qiuxs.sms.sender;

import java.util.Map;

/**
 * 短信发送器
 * @author qiuxs
 * 2019年3月28日 下午11:41:23
 */
public interface ISMSSener {
	
	/**
	 * 根据内容发送短信
	 * 
	 * 2019年3月28日 下午11:14:48
	 * @auther qiuxs
	 * @param mobile
	 * 		手机号
	 * @param content
	 * 		短信内容
	 */
	public void sendSMS(String signName, String mobile, String content);

	/**
	 * 根据模板发送短信
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
	public void sendSMSByTemplate(String signName, String mobile, String templateId, Map<String, String> params);
	
}
