package com.qiuxs.captcha.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qiuxs.captcha.dao.CaptchaBlacklistDao;
import com.qiuxs.captcha.entity.CaptchaBlacklist;
import com.qiuxs.captcha.service.ICaptchaBlacklistService;
import com.qiuxs.cuteframework.web.action.BaseAction;

/**
 * Action
 *
 * 创建时间 ：2019-03-28 22:55:09
 * @author qiuxs
 * 
 */
 @Service
public class CaptchaBlacklistAction extends BaseAction<Long, CaptchaBlacklist, CaptchaBlacklistDao, ICaptchaBlacklistService> {

	@Resource
	private ICaptchaBlacklistService captchablacklistService;

	@Override
	protected ICaptchaBlacklistService getService() {
		return this.captchablacklistService;
	}

}
