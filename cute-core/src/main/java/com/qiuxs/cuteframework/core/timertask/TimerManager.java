package com.qiuxs.cuteframework.core.timertask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.qiuxs.cuteframework.core.basic.utils.ListUtils;
import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;
import com.qiuxs.cuteframework.tech.task.AsyncTaskExecutor;
import com.qiuxs.cuteframework.tech.task.RunnableAsyncTask;

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

	private static Map<String, MyTaskTaskWrapper> taskMap = new ConcurrentHashMap<String, MyTaskTaskWrapper>();
	
	/**
	 * 注册一个定时任务
	 *  
	 * @author qiuxs  
	 * @param taskName
	 * @param wrapper
	 */
	public static void registerTask(String taskName, MyTaskTaskWrapper wrapper) {
		taskMap.put(taskName, wrapper);
	}
	
	/**
	 * 手动执行一个定时任务
	 *  
	 * @author qiuxs  
	 * @param taskName
	 */
	public static void manualInvoke(String taskName) {
		MyTaskTaskWrapper taskWrapper = taskMap.get(taskName);
		if (taskWrapper != null) {
			AsyncTaskExecutor.execute(new RunnableAsyncTask<Object>(null) {
				@Override
				public void execute(Object preparParam) {
					taskWrapper.run();
				}
			}, false);
		}
	}
	
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
