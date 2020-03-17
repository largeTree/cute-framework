package com.qiuxs.cuteframework.core.basic.utils;

import java.util.concurrent.TimeUnit;

public class ThreadUtils {

	/**
	 * 睡眠
	 *  
	 * @author qiuxs  
	 * @param ms
	 */
	public static void sleep(long ms) {
		try {
			TimeUnit.MILLISECONDS.sleep(ms);
		}catch (Exception e) {
		}
	}
	
}
