package com.qiuxs.sms.sender.impl;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.qiuxs.sms.sender.ISMSSener;

/**
 * 阿里云短信发送器实现
 * @author qiuxs
 * 2019年3月28日 下午11:42:03
 */
public class AlibabaSMSSender implements ISMSSener {
	
	private static Logger log = LogManager.getLogger(AlibabaSMSSender.class);
	
	private static final String DOMAIN = "dysmsapi.aliyuncs.com";
	
	private IAcsClient client;
	
	public AlibabaSMSSender(String regionId, String appKey, String appSecret) {
		DefaultProfile profile = DefaultProfile.getProfile(regionId, appKey, appSecret);
		client = new DefaultAcsClient(profile);
	}
	
	/**
	 * 2019年3月28日 下午11:44:47
	 * qiuxs
	 * @see com.qiuxs.sms.sender.ISMSSener#sendSMS(java.lang.String, java.lang.String)
	 */
	@Override
	public void sendSMS(String signName, String mobile, String content) {
		throw new UnsupportedOperationException("Alibaba SMS Service not suppored send by content");
	}

	/**
	 * 2019年3月28日 下午11:44:55
	 * qiuxs
	 * @see com.qiuxs.sms.sender.ISMSSener#sendSMSByTemplate(java.lang.String, java.lang.String, java.util.Map)
	 */
	@Override
	public void sendSMSByTemplate(String signName, String mobile, String templateId, Map<String, String> params) {
		CommonRequest request = new CommonRequest();
		request.setProtocol(ProtocolType.HTTPS);
		request.setMethod(MethodType.POST);
		request.setDomain(DOMAIN);
		request.setVersion("2019-04-09");
		request.setAction("SendSms");
		request.putQueryParameter("RegionId", "cn-hangzhou");
		request.putQueryParameter("PhoneNumbers", mobile);
		request.putQueryParameter("SignName", signName);
		request.putQueryParameter("TemplateCode", templateId);
		request.putQueryParameter("TemplateParam", "{\"captcha\":\"123456\"}");
		try {
			CommonResponse commonResponse = this.client.getCommonResponse(request);
			log.info(commonResponse.getData());
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
