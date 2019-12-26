package com.qiuxs.cuteframework.core.timertask;

import com.qiuxs.cuteframework.core.listener.lc.IWebLifecycle;

/**
 * 
 * 功能描述: 定时任务生命周期钩子<p>  
 * 新增原因: TODO<p>  
 * 新增日期: 2019年11月6日 下午7:11:31 <p>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class TimerLifcycle implements IWebLifecycle {

	@Override
	public int order() {
		return 3;
	}

	@Override
	public void lastInit() {
		TimerManager.init();
	}

	@Override
	public void lastDestory() {
		TimerManager.destory();
	}

}
