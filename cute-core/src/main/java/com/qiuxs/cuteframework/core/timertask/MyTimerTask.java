package com.qiuxs.cuteframework.core.timertask;

import java.util.Date;

/**
 * 
 * 功能描述: 定时任务定义，需要时实现此接口，并注册为SpringBean即可按配置定时执行 <p>  
 * 新增原因: TODO<p>  
 * 新增日期: 2019年11月6日 下午7:09:40 <p>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public interface MyTimerTask extends Runnable {

	static final int ONE_MIN = 60 * 1000;
	static final int ONE_HOUR = 60 * 60 * 1000;
	
	/**
	 * 是否启用
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public boolean isEnabled();

	/**
	 * 首次执行时间
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public Date getFirstTime();

	/**
	 * 执行周期
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public long getPeriod();

	/**
	 * 获取定时任务名称
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public default String getName() {
		return this.getClass().getSimpleName();
	}

}
