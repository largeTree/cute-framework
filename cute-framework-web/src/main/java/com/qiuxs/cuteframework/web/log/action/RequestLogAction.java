package com.qiuxs.cuteframework.web.log.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.web.action.BaseAction;
import com.qiuxs.cuteframework.web.log.dao.RequestLogDao;
import com.qiuxs.cuteframework.web.log.entity.RequestLog;
import com.qiuxs.cuteframework.web.log.service.IRequestLogService;

/**
 * 请求日志记录Action
 *
 * 创建时间 ：2019-05-30 22:26:46
 * @author qiuxs
 * 
 */
 @Service
public class RequestLogAction extends BaseAction<Long, RequestLog, RequestLogDao, IRequestLogService> {

	@Resource
	private IRequestLogService apirequestlogService;

	@Override
	protected IRequestLogService getService() {
		return this.apirequestlogService;
	}

}
