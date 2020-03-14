package com.qiuxs.gconfig.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.web.action.BaseAction;

import com.qiuxs.gconfig.entity.ScGconfigOwnerVal;
import com.qiuxs.gconfig.dao.ScGconfigOwnerValDao;
import com.qiuxs.gconfig.service.IScGconfigOwnerValService;

/**
 * 全局配置所有者的值Action
 *
 * 创建时间 ：2020-03-04 10:23:40
 * @author qiuxs
 * 
 */
 @Service
public class ScGconfigOwnerValAction extends BaseAction<Long, ScGconfigOwnerVal, ScGconfigOwnerValDao, IScGconfigOwnerValService> {

	@Resource
	private IScGconfigOwnerValService scgconfigownervalService;

	@Override
	protected IScGconfigOwnerValService getService() {
		return this.scgconfigownervalService;
	}

}
