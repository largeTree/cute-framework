package com.qiuxs.cuteframework.tech.log;

import java.util.Map;

import org.slf4j.MDC;

public class LogUtils {

	public static void putMDC(String key, String val) {
		MDC.put(key, val);
	}

	public static void clearMDC() {
		MDC.clear();
	}

	public static Map<String, String> getContextMap() {
		return MDC.getCopyOfContextMap();
	}
	
	public static void setContextMap(Map<String, String> contextMap) {
		MDC.setContextMap(contextMap);
	}
}
