package com.qiuxs.cuteframework.web.context.listener;

import javax.servlet.ServletContext;

import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;

public class CuteServletContextMiddleLoadListener extends ContextLoaderListener {
	@Override
	protected void customizeContext(ServletContext sc, ConfigurableWebApplicationContext wac) {
		super.customizeContext(sc, wac);
		ApplicationContextHolder.setApplicationContext(wac);
	}
}
