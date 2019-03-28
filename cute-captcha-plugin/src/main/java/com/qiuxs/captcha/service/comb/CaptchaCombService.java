package com.qiuxs.captcha.service.comb;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.qiuxs.captcha.service.ICaptchaCombService;

@Service
public class CaptchaCombService implements ICaptchaCombService {

	@Override
	public void sendMobileCaptcha(String mobile, String template) {

	}

	@Override
	public void sendMobileCaptchaByTemplate(String mobile, String templateId, Map<String, String> params) {

	}

	@Override
	public void verifyCaptcha(String mobile, String captcha) {

	}

	@Override
	public void veriftCaptcha(String mobile, String captcha, boolean invalidFlag) {

	}

}
