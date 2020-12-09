package com.qiuxs.cuteframework.core.timertask;

import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.cuteframework.core.context.TLVariableHolder;
import com.qiuxs.cuteframework.core.utils.notice.NoticeLogger;
import com.qiuxs.cuteframework.tech.microsvc.log.ApiLogUtils;

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
	
	public void runWithArg(String arg) {
		try {
			ApiLogUtils.initApiLog(task.getName(), EnvironmentContext.getAppName(), null, null, null, "");
			long startTime = System.currentTimeMillis();
			this.task.run(arg);
			long consumedTime = (System.currentTimeMillis() - startTime);
			this.consumerTime += consumedTime;
			successCount++;
		} catch (Throwable e) {
			failedCount++;
			String msg = this.task.getClass().getName() + ", execute Failed ext = " + e.getLocalizedMessage();
			log.error(msg, e);
			if (!EnvironmentContext.isDebug()) {
				NoticeLogger.error(msg, ExceptionUtils.getStackTrace(e));
			}
		} finally {
			TLVariableHolder.clear();
		}
	}

	@Override
	public void run() {
		this.runWithArg(null);
	}
	
	MyTimerTask getTask() {
		return task;
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
