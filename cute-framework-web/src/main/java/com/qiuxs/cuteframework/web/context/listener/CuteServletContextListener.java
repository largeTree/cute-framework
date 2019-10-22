package com.qiuxs.cuteframework.web.context.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qiuxs.cuteframework.web.context.WebContext;

@WebListener
public class CuteServletContextListener implements ServletContextListener {
	private static Logger log = LogManager.getLogger(CuteServletContextListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		WebContext.setCtxPath(sce.getServletContext().getContextPath());
		WebContext.setServletContext(sce.getServletContext());
		log.info("contextInitialized with ctxPath : " + WebContext.getCtxPath());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
