package com.qiuxs.cuteframework.tech.log;

import java.util.Map;

import org.slf4j.MDC;

import com.qiuxs.cuteframework.tech.microsvc.log.ApiLogProp;

public class LogUtils {

	public static void putMDC(ApiLogProp apiLogProp) {
		putMDC("ip", apiLogProp.getClientIp());
		putMDC(LogConstant.COLUMN_GLOBALID, String.valueOf(apiLogProp.getGlobalId()));
	}
	
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
