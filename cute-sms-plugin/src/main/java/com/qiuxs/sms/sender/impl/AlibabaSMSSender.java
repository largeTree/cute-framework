package com.qiuxs.sms.sender.impl;

import java.util.Map;

import com.qiuxs.sms.sender.ISMSSener;

/**
 * 阿里云短信发送器实现
 * @author qiuxs
 * 2019年3月28日 下午11:42:03
 */
public class AlibabaSMSSender implements ISMSSener {
	
	/**
	 * 2019年3月28日 下午11:44:47
	 * qiuxs
	 * @see com.qiuxs.sms.sender.ISMSSener#sendSMS(java.lang.String, java.lang.String)
	 */
	@Override
	public void sendSMS(String mobile, String content) {
		throw new UnsupportedOperationException("Alibaba SMS Service not suppored send by content");
	}

	/**
	 * 2019年3月28日 下午11:44:55
	 * qiuxs
	 * @see com.qiuxs.sms.sender.ISMSSener#sendSMSByTemplate(java.lang.String, java.lang.String, java.util.Map)
	 */
	@Override
	public void sendSMSByTemplate(String mobile, String templateId, Map<String, String> params) {

	}

}
