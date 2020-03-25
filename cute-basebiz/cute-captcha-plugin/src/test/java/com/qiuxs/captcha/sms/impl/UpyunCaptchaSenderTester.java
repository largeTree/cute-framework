package com.qiuxs.captcha.sms.impl;

import java.util.HashMap;

import org.junit.Test;

import com.qiuxs.captcha.sms.ISMSCaptchaSender;
import com.qiuxs.cuteframework.core.basic.config.UConfigUtils;
import com.qiuxs.sms.sender.impl.UpyunSMSSender;

public class UpyunCaptchaSenderTester {

	private ISMSCaptchaSender sender = new UpyunSMSCaptchaSender(new UpyunSMSSender());
	
	@Test
	public void testSendCaptcha() {
		sender.sendCaptchaByTemplate("【购有折】", "17681690226", UConfigUtils.getDomain(UpyunSMSSender.CONFIG_DOMAIN).getString("captcha.template"), new HashMap<String, String>(), "123456");
	}
	
}
