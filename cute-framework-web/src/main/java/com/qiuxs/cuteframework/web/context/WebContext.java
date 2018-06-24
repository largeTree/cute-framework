package com.qiuxs.cuteframework.web.context;

import javax.servlet.ServletContext;

/**
 * Web上下文缓存
 * @author qiuxs
 *
 */
public class WebContext {
	private static String ctxPath;
	private static ServletContext servletContext;

	public static String getCtxPath() {
		return ctxPath;
	}

	public static void setCtxPath(String ctxPath) {
		WebContext.ctxPath = ctxPath;
	}

	public static ServletContext getServletContext() {
		return servletContext;
	}

	public static void setServletContext(ServletContext servletContext) {
		WebContext.servletContext = servletContext;
	}

}
