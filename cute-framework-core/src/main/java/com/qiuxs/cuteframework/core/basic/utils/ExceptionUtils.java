package com.qiuxs.cuteframework.core.basic.utils;

public class ExceptionUtils {
	
	/**
	 * 对异常包装一层RuntimeException
	 * @param e
	 * @return
	 */
	public static RuntimeException unchecked(Exception e) {
		return new RuntimeException(e);
	}
	
}
