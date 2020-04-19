package com.qiuxs.cuteframework.web.common.action;

import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.core.timertask.TimerManager;
import com.qiuxs.cuteframework.web.action.IAction;
import com.qiuxs.cuteframework.web.bean.ActionResult;
import com.qiuxs.cuteframework.web.bean.ReqParam;

@Service
public class SystemAction implements IAction {
	
	/**
	 * 手动执行一个定时任务
	 *  
	 * @author qiuxs  
	 * @param params
	 * @return
	 */
	public ActionResult manualInvoke(ReqParam params) {
		String taskName = params.getStringMust("taskName");
		TimerManager.manualInvoke(taskName);
		return ActionResult.SUCCESS_INSTANCE;
	}
	
}
