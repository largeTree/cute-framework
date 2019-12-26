package com.qiuxs.cuteframework.web.context.listener;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qiuxs.cuteframework.core.listener.lc.IWebLifecycle;
import com.qiuxs.cuteframework.core.listener.lc.WebLifecycleContainer;
import com.qiuxs.cuteframework.web.context.WebContext;

public class CuteServletContextFirstListener implements ServletContextListener {
	private static Logger log = LogManager.getLogger(CuteServletContextFirstListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		WebContext.setCtxPath(sce.getServletContext().getContextPath());
		WebContext.setServletContext(sce.getServletContext());

		List<IWebLifecycle> lifecycles = WebLifecycleContainer.getLifecycles();
		for (IWebLifecycle lc : lifecycles) {
			try {
				lc.firstInit();
			} catch (Throwable e) {
				log.error(lc.getClass().getName() + ", firstInit ext = " + e.getLocalizedMessage(), e);
			}
		}

		log.info("contextInitialized with ctxPath : " + WebContext.getCtxPath());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		List<IWebLifecycle> lifecycles = WebLifecycleContainer.getLifecycles();
		for (int i = lifecycles.size() - 1; i >= 0; i--) {
			IWebLifecycle lc = lifecycles.get(i);
			try {
				lc.firstDestoryed();
			} catch (Exception e) {
				log.error(lc.getClass().getName() + ", firstDestoryed ext = " + e.getLocalizedMessage(), e);
			}
		}
	}

}
