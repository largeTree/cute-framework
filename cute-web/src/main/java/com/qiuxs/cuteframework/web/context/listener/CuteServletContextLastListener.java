package com.qiuxs.cuteframework.web.context.listener;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qiuxs.cuteframework.core.listener.lc.IWebLifecycle;
import com.qiuxs.cuteframework.core.listener.lc.WebLifecycleContainer;

public class CuteServletContextLastListener implements ServletContextListener {
	
	private static Logger log = LogManager.getLogger(CuteServletContextLastListener.class);
	
	/** 系统是否进入Last Destroy阶段 */
	private static boolean lastDestroy = false;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		List<IWebLifecycle> lifecycles = WebLifecycleContainer.getLifecycles();
		for (IWebLifecycle lifecycle : lifecycles) {
			try {
				lifecycle.lastInit();
			} catch (Exception e) {
				log.error("Lifecycle@" + lifecycle.getClass() + " started Faild ext = " + e.getLocalizedMessage(), e);
			}
		}

		log.info("SpringContextStarted....");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// 标记已进入lastDestory阶段
		lastDestroy = true;
		
		List<IWebLifecycle> lifecycles = WebLifecycleContainer.getLifecycles();
		for (int i = lifecycles.size() - 1; i >= 0; i--) {
			IWebLifecycle lifecycle = lifecycles.get(i);
			try {
				lifecycle.lastDestory();
			} catch (Exception e) {
				log.error("Lifecycle@" + lifecycle.getClass() + ", destroy Failed ext = " + e.getLocalizedMessage(), e);
			}
		}
	}
	
	/**
	 * 系统当前是否进入了Last Destroy阶段
	 * @return 系统进入Last Destroy阶段返回true，否则返回false
	 */
	public static boolean isLastDestroy() {
		return lastDestroy;
	}

}
