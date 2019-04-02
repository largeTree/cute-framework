package com.qiuxs.captcha.service.comb;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qiuxs.captcha.config.CaptchaEnvironmentConfig;
import com.qiuxs.captcha.context.CaptchaWebContext;
import com.qiuxs.captcha.entity.Captcha;
import com.qiuxs.captcha.service.ICaptchaBlacklistService;
import com.qiuxs.captcha.service.ICaptchaCombService;
import com.qiuxs.captcha.service.ICaptchaHistoryService;
import com.qiuxs.captcha.service.ICaptchaService;
import com.qiuxs.captcha.sms.ISMSCaptchaSender;
import com.qiuxs.captcha.sms.SMSCaptchaSenderRegisterCenter;

@Service
public class CaptchaCombService implements ICaptchaCombService {

	@Resource
	private CaptchaEnvironmentConfig captchaEnvironemtConfig;

	@Resource
	private ICaptchaService captchaService;

	@Resource
	private ICaptchaHistoryService captchaHistoryService;

	@Resource
	private ICaptchaBlacklistService captchaBlacklistService;

	@Override
	public void sendMobileCaptcha(String mobile, String template) {

	}

	@Override
	public void sendMobileCaptchaByTemplate(String mobile, String templateId, Map<String, String> params) {
		ISMSCaptchaSender<?> sender = SMSCaptchaSenderRegisterCenter.chooseAnSender(mobile);
		// 生成验证码
		String captcha = this.getCaptcha(mobile);
		if (params == null) {
			params = new HashMap<>();
		}
		// 设置到模板参数中
		params.put(this.captchaEnvironemtConfig.getCapchaPlaceholder(), captcha);
		sender.sendCaptchaByTemplate(mobile, templateId, params);
	}

	@Override
	public void verifyCaptcha(String mobile, String captcha) {

	}

	@Override
	public void veriftCaptcha(String mobile, String captcha, boolean invalidFlag) {

	}

	/**
	 * 获取一个验证码
	 * 
	 * 2019年4月2日 下午10:19:11
	 * @auther qiuxs
	 * @param mobile
	 * @return
	 */
	private String getCaptcha(String mobile) {
		// 根据拉黑规则拉黑指定手机号
		// 检查是否在黑名单内
		this.captchaBlacklistService.checkInBlacklist(mobile);

		Captcha captcha = this.captchaService.genCaptcha(mobile, CaptchaWebContext.getCliIP());
		return captcha.getCaptcha();
	}

}
