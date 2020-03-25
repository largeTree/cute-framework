package com.qiuxs.captcha.sms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;

/**
 * 短信验证码发送器注册中心
 * @author qiuxs
 * 2019年3月28日 下午11:25:24
 */
public class SMSCaptchaSenderRegisterCenter {

	/**
	 * 验证码发送器列表
	 */
	private static List<ISMSCaptchaSender> smsCaptchaSends;

	/**
	 * 手机号对应发送器索引
	 */
	private static Map<String, Integer> mobileSenderIdx = new HashMap<>();

	/**
	 * 注册一个短信验证码发送器
	 * 
	 * 2019年3月28日 下午11:30:58
	 * @auther qiuxs
	 * @param sender
	 */
	public static void register(ISMSCaptchaSender sender) {
		getSenders().add(sender);
	}

	/**
	 * 根据手机号选择一个发送器
	 * 
	 * 2019年3月28日 下午11:31:46
	 * @auther qiuxs
	 * @param mobile
	 */
	public static ISMSCaptchaSender chooseAnSender(String mobile) {
		List<ISMSCaptchaSender> senders = getSenders();
		if (senders.size() == 0) {
			throw new RuntimeException("No SMSCaptchaSender Registered");
		}
		Integer idx = mobileSenderIdx.get(mobile);
		if (idx == null) {
			idx = 0;
		}
		if (idx >= senders.size()) {
			idx = 0;
		}
		ISMSCaptchaSender sender = senders.get(idx);
		mobileSenderIdx.put(mobile, ++idx);
		return sender;
	}
	
	private static List<ISMSCaptchaSender> getSenders() {
		if (smsCaptchaSends == null) {
			List<ISMSCaptchaSender> beans = ApplicationContextHolder.getBeansForType(ISMSCaptchaSender.class);
			smsCaptchaSends = new ArrayList<ISMSCaptchaSender>(beans);
		}
		return smsCaptchaSends;
	}

}
