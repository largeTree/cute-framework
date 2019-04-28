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
	public void sendMobileCaptcha(String signName, String mobile, String template) {
		String captcha = this.getCaptcha(mobile);
		String content = template.replace(this.captchaEnvironemtConfig.getCapchaPlaceholder(), captcha);
		ISMSCaptchaSender sender = SMSCaptchaSenderRegisterCenter.chooseAnSender(mobile);
		sender.sendCaptcha(signName, mobile, content);
	}

	@Override
	public void sendMobileCaptchaByTemplate(String signName, String mobile, String templateId, Map<String, String> params) {
		ISMSCaptchaSender sender = SMSCaptchaSenderRegisterCenter.chooseAnSender(mobile);
		// 生成验证码
		String captcha = this.getCaptcha(mobile);
		if (params == null) {
			params = new HashMap<>();
		}
		// 设置到模板参数中
		params.put(this.captchaEnvironemtConfig.getCapchaPlaceholder(), captcha);
		sender.sendCaptchaByTemplate(signName, mobile, templateId, params);
	}

	/**
	 * 验证验证码正确性、并自动在正确的情况下将验证码移到历史记录中
	 * 
	 * 2019年4月9日 下午10:18:39
	 * qiuxs
	 * @see com.qiuxs.captcha.service.ICaptchaCombService#verifyCaptcha(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean verifyCaptcha(String mobile, String captcha) {
		return this.veriftCaptcha(mobile, captcha, true);
	}

	/**
	 * 验证验证码是否正确，不存或已过期时返回错误
	 * 
	 * 2019年4月9日 下午10:19:08
	 * qiuxs
	 * @see com.qiuxs.captcha.service.ICaptchaCombService#veriftCaptcha(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public boolean veriftCaptcha(String mobile, String captcha, boolean invalidFlag) {
		Captcha captchaBean = this.captchaService.getBySessionKey(mobile);
		if (captchaBean != null && captchaBean.checkExpire()) {
			// 已过期的移到历史记录
			this.captchaHistoryService.moveToHistory(captchaBean);
			captchaBean = null;
		}
		// 验证码不存在或已过期
		if (captchaBean == null) {
			return false;
		}
		// 验证结果
		boolean res = captchaBean.getCaptcha().equals(captcha);
		// 结果正确且失效
		if (res && invalidFlag) {
			this.captchaHistoryService.moveToHistory(captchaBean);
		}
		return res;
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
