package com.qiuxs.cuteframework.core.basic.utils;

import java.lang.reflect.Array;

public class ArrayUtils extends org.apache.commons.lang3.ArrayUtils {

	/**
	 * 判断数组是否是Null或者长度为0
	 * 
	 * @param arr
	 * @return
	 */
	public static boolean isNullOrEmpty(Object arr) {
		if (arr == null) {
			return true;
		}
		if (arr.getClass().isArray()) {
			return Array.getLength(arr) == 0;
		}
		throw new IllegalArgumentException("arr is not array");
	}

}
