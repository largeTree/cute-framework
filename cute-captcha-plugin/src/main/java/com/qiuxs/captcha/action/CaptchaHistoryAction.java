package com.qiuxs.captcha.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.web.action.BaseAction;

import com.qiuxs.captcha.entity.CaptchaHistory;
import com.qiuxs.captcha.dao.CaptchaHistoryDao;
import com.qiuxs.captcha.service.ICaptchaHistoryService;

/**
 * 验证码历史记录Action
 *
 * 创建时间 ：2019-03-28 22:55:09
 * @author qiuxs
 * 
 */
 @Service
public class CaptchaHistoryAction extends BaseAction<Long, CaptchaHistory, CaptchaHistoryDao, ICaptchaHistoryService> {

	@Resource
	private ICaptchaHistoryService captchahistoryService;

	@Override
	protected ICaptchaHistoryService getService() {
		return this.captchahistoryService;
	}

}
