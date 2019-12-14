package com.qiuxs.cuteframework.core.timertask;

import java.util.ArrayList;
import java.util.List;

import com.qiuxs.cuteframework.core.basic.utils.ListUtils;
import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;

/**
 * 
 * 功能描述: <p>  
 * 新增原因: TODO<p>  
 * 新增日期: 2019年11月6日 下午7:11:56 <p>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class TimerManager {

	private static List<MyTimer> timers;

	/**
	 * 销毁所有定时任务
	 *  
	 * @author qiuxs
	 */
	public static void destory() {
		if (ListUtils.isEmpty(timers)) {
			return;
		}
		for (MyTimer timer : timers) {
			timer.stop();
		}
	}

	/**
	 * 初始化定时任务
	 *  
	 * @author qiuxs
	 */
	public static void init() {
		String[] names = ApplicationContextHolder.getBeanNamesForType(MyTimerTask.class);
		if (names.length == 0) {
			return;
		}
		List<MyTimer> timers = new ArrayList<>();
		for (String name : names) {
			MyTimerTask task = ApplicationContextHolder.getBean(name, MyTimerTask.class);
			MyTimer myTimer = new MyTimer(task);
			myTimer.start();
			timers.add(myTimer);
		}
		TimerManager.timers = timers;
	}

}
