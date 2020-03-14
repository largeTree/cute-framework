package com.qiuxs.gconfig.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.web.action.BaseAction;

import com.qiuxs.gconfig.entity.ScGconfigOptions;
import com.qiuxs.gconfig.dao.ScGconfigOptionsDao;
import com.qiuxs.gconfig.service.IScGconfigOptionsService;

/**
 * 全局配置选项Action
 *
 * 创建时间 ：2020-03-04 10:23:40
 * @author qiuxs
 * 
 */
 @Service
public class ScGconfigOptionsAction extends BaseAction<Long, ScGconfigOptions, ScGconfigOptionsDao, IScGconfigOptionsService> {

	@Resource
	private IScGconfigOptionsService scgconfigoptionsService;

	@Override
	protected IScGconfigOptionsService getService() {
		return this.scgconfigoptionsService;
	}

}
