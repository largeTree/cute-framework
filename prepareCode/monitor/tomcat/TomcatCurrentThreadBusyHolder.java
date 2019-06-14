package com.hzecool.frm.monitor.tomcat;

import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.hzecool.core.concurrent.utils.ThreadUtil;
import com.hzecool.core.log.logger.CommonLogger;
import com.hzecool.core.log.logger.Nagios;
import com.hzecool.fdn.utils.FileUtils;
import com.hzecool.frm.devOps.DevOpsParamHolder;

/**
 * 
 * 功能描述:  tomcat当前请求线程数获取对象
 * TODO：差异项。没有取unconfig中配置的值
 * 
 * 新增日期: 2017年5月2日 下午3:12:38 <br/>  
 *  
 * @author laisf   
 * @version 1.0.0
 */
public class TomcatCurrentThreadBusyHolder {
	//移到DevOpsParamHolder
//	private static int maxThreadBusy = 200;
	/**当前最新的同时请求线程数，定时刷新*/
	private static int currentThreadBusy = 0;
	
	/**定时刷新间隔时间*/
	private static final int  FREQUENCY_SENCOND = 2;
	
	private static final int  RETRY_DELAY_MS = 1500;

	/**
	 * 获取最新的请求线程数
	 *  
	 * @author laisf  
	 * @return
	 */
	public static int getCurrentThreadBusy(){
		return currentThreadBusy;
	}
	
	/**
	 * 启用定时刷新currentThreadBusy的定时器，
	 *  
	 * @author laisf  
	 * @return
	 */
	public static void startRefreshTimer(){
		ScheduledExecutorService executorService = 
				Executors.newScheduledThreadPool(1);
		executorService.scheduleWithFixedDelay(new RefreshTimerTask(), 0, FREQUENCY_SENCOND,
				TimeUnit.SECONDS);
	}
	
	/**
	 * 
	 * 功能描述: 定时获取 currentThreadBusy的任务
	 * 新增日期: 2017年5月2日 下午3:27:57 <br/>  
	 *  
	 * @author laisf   
	 * @version 1.0.0
	 */
	private static class RefreshTimerTask extends TimerTask{

		@Override
		public void run() {
			//先取一次currentThreadBusy， 如果超过阈值，稍停顿后再取一次，减少偶尔超过峰值的误报
			int currentThreadBusy = getCurrentThreadBusy();
			int maxNum = DevOpsParamHolder.getMaxThreadBusy();
			if(currentThreadBusy > maxNum){
				CommonLogger.logger.info("first thread busy:" 
						+ currentThreadBusy + ">" +  maxNum);
				ThreadUtil.sleep1(RETRY_DELAY_MS);
				currentThreadBusy = getCurrentThreadBusy();
			}
			TomcatCurrentThreadBusyHolder.currentThreadBusy = currentThreadBusy;
			if(currentThreadBusy > maxNum){
				Nagios.logger.info("current thread busy: " + currentThreadBusy + ">" +  maxNum);
				TomcatThreadListDumpUtil.dump();//将当前的线程dump下来提供分析
			}
		}
		
		/**
		 * 获取currentThreadBusy
		 *  
		 * @author laisf  
		 * @return
		 */
		private int getCurrentThreadBusy(){
			List<TomcatInformations> listThreadPool = 
					TomcatInformations.buildTomcatInformationsList();
			int currentThreadBusy = 0;
			for(TomcatInformations infors : listThreadPool){
				currentThreadBusy = infors.getCurrentThreadsBusy();
			}
			return currentThreadBusy;
		}
		
	}
}
