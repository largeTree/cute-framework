package com.qiuxs.cuteframework.core.timertask;

import java.util.Timer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.basic.utils.DateFormatUtils;

/**
 * 
 * 功能描述: 定时器包装<p>  
 * 新增原因: TODO<p>  
 * 新增日期: 2019年11月6日 下午6:50:52 <p>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class MyTimer {

	private static Logger log = LogManager.getLogger(MyTimer.class);

	private final MyTimerTask task;
	private final MyTaskTaskWrapper taskWrapper;
	private Timer timer = new Timer(true);

	public MyTimer(MyTimerTask task) {
		this.task = task;
		this.taskWrapper = new MyTaskTaskWrapper(this.task);
		TimerManager.registerTask(task.getName(), taskWrapper);
	}

	/**
	 * 启动任务
	 *  
	 * @author qiuxs
	 */
	public void start() {
		if (task.isEnabled()) {
			log.info("Start TimerTask[" + this.task.getClass().getName() + "], firstTime = " + DateFormatUtils.formatTime(this.task.getFirstTime()) + ", period = "
					+ this.task.getPeriod());
			timer.schedule(this.taskWrapper, this.task.getFirstTime(), this.task.getPeriod());
		} else {
			log.info("TimerTask[" + this.task.getClass().getName() + "], not enable...");
		}
	}

	/**
	 * 停止任务
	 *  
	 * @author qiuxs
	 */
	public void stop() {
		this.timer.cancel();
	}

	/**
	 * dump状态
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public JSONObject dump() {
		JSONObject taskStaus = this.taskWrapper.dump();
		JSONObject status = new JSONObject();
		status.put("task", taskStaus);
		status.put("timer", this.timer.toString());
		return status;
	}
}
