package com.qiuxs.captcha.sms.impl;

import java.util.Map;

import com.qiuxs.cuteframework.core.basic.utils.MapUtils;
import com.qiuxs.sms.sender.impl.UpyunSMSSender;

/**
 * 又拍云短信发送器
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年3月10日 下午2:32:05 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class UpyunSMSCaptchaSender extends AbstractSMSCaptchaSender<UpyunSMSSender> {

	public UpyunSMSCaptchaSender(UpyunSMSSender sender) {
		super(sender);
	}

	@Override
	public void sendCaptchaByTemplate(String signName, String mobile, String templateId, Map<String,String> params, String caption) {
		this.getSmsSender().sendSMSByTemplate(signName, mobile, templateId, MapUtils.genStringMap("vars", caption));
	}

}
