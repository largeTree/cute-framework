package com.qiuxs.cuteframework.core.timertask;

import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.dingtalk.NoticeLogger;

/**
 * 
 * 功能描述: 定时任务包装 <p>  
 * 新增原因: TODO<p>  
 * 新增日期: 2019年11月6日 下午6:51:52 <p>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class MyTaskTaskWrapper extends TimerTask {

	private static Logger log = LogManager.getLogger(MyTaskTaskWrapper.class);

	private int successCount;
	private int failedCount;
	private long consumerTime;

	private MyTimerTask task;

	public MyTaskTaskWrapper(MyTimerTask task) {
		this.task = task;
	}

	@Override
	public void run() {
		try {
			long startTime = System.currentTimeMillis();
			this.task.run();
			long consumedTime = (System.currentTimeMillis() - startTime);
			this.consumerTime += consumedTime;
			successCount++;
		} catch (Throwable e) {
			failedCount++;
			String msg = this.task.getClass().getName() + ", execute Failed ext = " + e.getLocalizedMessage();
			log.error(msg, e);
			NoticeLogger.error(msg, ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * dump状态
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public JSONObject dump() {
		JSONObject status = new JSONObject();
		status.put("task", this.task);
		status.put("successCount", this.successCount);
		status.put("failedCount", this.failedCount);
		status.put("consumerTime", this.consumerTime);
		status.put("avgConsumer", this.consumerTime / this.successCount);
		return status;
	}

	@Override
	public String toString() {
		return "MyTaskTaskDesc [successCount=" + this.successCount + ", failedCount=" + this.failedCount + ", consumerTime=" + this.consumerTime + "]";
	}

}