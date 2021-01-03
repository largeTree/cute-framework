package com.qiuxs.cuteframework.web.context.listener;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qiuxs.cuteframework.core.listener.lc.IWebLifecycle;
import com.qiuxs.cuteframework.core.listener.lc.WebLifecycleContainer;

public class CuteServletContextMiddleLoadListener implements ServletContextListener {

	private static Logger log = LoggerFactory.getLogger(CuteServletContextMiddleLoadListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		List<IWebLifecycle> lifecycles = WebLifecycleContainer.getLifecycles();
		for (IWebLifecycle lc : lifecycles) {
			try {
				lc.middleInit();
			} catch (Throwable e) {
				log.error(lc.getClass().getName() + ", firstInit ext = " + e.getLocalizedMessage(), e);
			}
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		List<IWebLifecycle> lifecycles = WebLifecycleContainer.getLifecycles();
		for (int i = lifecycles.size() - 1; i >= 0; i--) {
			IWebLifecycle lc = lifecycles.get(i);
			try {
				lc.middleDestoryed();
			} catch (Exception e) {
				log.error(lc.getClass().getName() + ", firstDestoryed ext = " + e.getLocalizedMessage(), e);
			}
		}
	}
}
