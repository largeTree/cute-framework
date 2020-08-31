package com.qiuxs.cuteframework.view.action;

import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.core.basic.ex.LogicException;
import com.qiuxs.cuteframework.view.pagemodel.Page;
import com.qiuxs.cuteframework.view.pagemodel.config.PageModeConfiguration;
import com.qiuxs.cuteframework.web.action.IAction;
import com.qiuxs.cuteframework.web.bean.ActionResult;
import com.qiuxs.cuteframework.web.bean.ReqParam;

@Service
public class PageModeAction implements IAction {
	
	/**
	 * 获取页面配置信息
	 *  
	 * @author qiuxs  
	 * @param params
	 * @return
	 */
	public ActionResult getPageMode(ReqParam params) {
		Page page = PageModeConfiguration.getPage(params.getStringMust("pageId"));
		if (page == null) {
			throw new LogicException("page not found");
		}
		return ActionResult.of(page);
	}

}
