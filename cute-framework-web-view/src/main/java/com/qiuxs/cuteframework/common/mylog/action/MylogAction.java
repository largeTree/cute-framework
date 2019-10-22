package com.qiuxs.cuteframework.common.mylog.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.common.mylog.dao.MylogDao;
import com.qiuxs.cuteframework.common.mylog.entity.Mylog;
import com.qiuxs.cuteframework.common.mylog.service.IMylogService;
import com.qiuxs.cuteframework.web.action.BaseAction;

/**
 * Action
 *
 * 创建时间 ：2019-07-25 22:32:08
 * @author qiuxs
 * 
 */
 @Service
public class MylogAction extends BaseAction<Long, Mylog, MylogDao, IMylogService> {

	@Resource
	private IMylogService mylogService;

	@Override
	protected IMylogService getService() {
		return this.mylogService;
	}

}
