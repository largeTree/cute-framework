package com.qiuxs.captcha.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qiuxs.captcha.dao.CaptchaDao;
import com.qiuxs.captcha.entity.Captcha;
import com.qiuxs.captcha.service.ICaptchaService;
import com.qiuxs.cuteframework.web.action.BaseAction;

/**
 * 验证码Action
 *
 * 创建时间 ：2019-03-28 22:55:09
 * @author qiuxs
 * 
 */
 @Service
public class CaptchaAction extends BaseAction<Long, Captcha, CaptchaDao, ICaptchaService> {

	@Resource
	private ICaptchaService captchaService;

	@Override
	protected ICaptchaService getService() {
		return this.captchaService;
	}

}
