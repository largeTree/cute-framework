package com.qiuxs.cuteframework.common.biz.func.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.common.biz.func.dao.FuncDao;
import com.qiuxs.cuteframework.common.biz.func.entity.Func;
import com.qiuxs.cuteframework.common.biz.func.helper.FuncInitHelper;
import com.qiuxs.cuteframework.common.biz.func.service.IFuncService;
import com.qiuxs.cuteframework.core.basic.code.tree.TreeItem;
import com.qiuxs.cuteframework.core.basic.utils.MapUtils;
import com.qiuxs.cuteframework.web.action.BaseAction;
import com.qiuxs.cuteframework.web.annotation.Api;
import com.qiuxs.cuteframework.web.bean.ActionResult;

/**
 * 功能菜单表Action
 *
 * 创建时间 ：2019-07-11 22:36:23
 * @author qiuxs
 * 
 */
 @Service
public class FuncAction extends BaseAction<String, Func, FuncDao, IFuncService> {

	@Resource
	private IFuncService funcService;
	
	@Resource
	private FuncInitHelper funcInitHelper;

	@Api(value = "func-initFunc", login = false)
	public ActionResult initFunc() {
		this.funcInitHelper.init();
		return ActionResult.SUCCESS_INSTANCE;
	}
	
	@Api("func-tree")
	public ActionResult funcTree(Map<String, String> params) {
		boolean includeSub = MapUtils.getBooleanValue(params, "includeSub", false);
		String parentId = MapUtils.getString(params, "parentId");
		List<TreeItem> funcTree = this.funcService.funcTree(parentId, includeSub);
		return new ActionResult(funcTree);
	}
	
	@Override
	protected IFuncService getService() {
		return this.funcService;
	}

}
