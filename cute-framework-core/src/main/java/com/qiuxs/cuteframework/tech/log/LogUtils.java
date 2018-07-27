package com.qiuxs.cuteframework.tech.log;

import org.slf4j.MDC;

public class LogUtils {

	public static void putMDC(String key, String val) {
		MDC.put(key, val);
	}

	public static void clearMDC() {
		MDC.clear();
	}

}
