package com.qiuxs.cuteframework.web.context;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoader;

import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.cuteframework.core.listener.lc.IWebLifecycle;
import com.qiuxs.cuteframework.core.listener.lc.WebLifecycleContainer;
import com.qiuxs.cuteframework.core.log.Console;
import com.qiuxs.cuteframework.tech.log.Log4j2ConfigurationFactory;

public class CuteWebApplicationInitializer implements WebApplicationInitializer {
	
	private static final Logger log = LoggerFactory.getLogger(CuteWebApplicationInitializer.class);
	
	private static final String CONTEXT_CONFIG_LOCATION_DEFAULT = "classpath*:applicationContext*.xml classpath*:/config/applicationContext*.xml";

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		System.out.println("CuteWebApplicationInitializer onStartup started!");
		//log4j2配置
		System.setProperty("WORKDIR", servletContext.getRealPath("/"));
		ConfigurationFactory.setConfigurationFactory(Log4j2ConfigurationFactory.getInstance());
		Console.log.info("Force Init Log4j2 Immediately!");

		//dubbo开关：需要web.xml中去掉context-param的contextConfigLocation
		String dubboEnable = EnvironmentContext.getString("middleware_dubbo_switch");
		if ("false".equals(dubboEnable)) {
			servletContext.setInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, CONTEXT_CONFIG_LOCATION_DEFAULT);
		} else {
			servletContext.setInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, CONTEXT_CONFIG_LOCATION_DEFAULT + " classpath*:/dubbo/applicationContext*.xml");
		}
		
		// 初始化，并执行firstInit
		init(servletContext);

		Console.log.info("[{}] CuteWebApplicationInitializer onStartup finished!", EnvironmentContext.getAppName());
	}

	private void init(ServletContext servletContext) {
		WebContext.setCtxPath(servletContext.getContextPath());
		WebContext.setServletContext(servletContext);

		List<IWebLifecycle> lifecycles = WebLifecycleContainer.getLifecycles();
		for (IWebLifecycle lc : lifecycles) {
			try {
				lc.firstInit();
			} catch (Throwable e) {
				log.error(lc.getClass().getName() + ", firstInit ext = " + e.getLocalizedMessage(), e);
			}
		}
	}

}
