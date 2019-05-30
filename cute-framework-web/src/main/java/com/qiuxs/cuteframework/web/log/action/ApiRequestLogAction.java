package com.qiuxs.cuteframework.web.log.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.web.action.BaseAction;

import com.qiuxs.cuteframework.web.log.entity.ApiRequestLog;
import com.qiuxs.cuteframework.web.log.dao.ApiRequestLogDao;
import com.qiuxs.cuteframework.web.log.service.IApiRequestLogService;

/**
 * 请求日志记录Action
 *
 * 创建时间 ：2019-05-30 22:26:46
 * @author qiuxs
 * 
 */
 @Service
public class ApiRequestLogAction extends BaseAction<Long, ApiRequestLog, ApiRequestLogDao, IApiRequestLogService> {

	@Resource
	private IApiRequestLogService apirequestlogService;

	@Override
	protected IApiRequestLogService getService() {
		return this.apirequestlogService;
	}

}
