package com.qiuxs.cuteframework.core.basic.utils;

import java.lang.reflect.Array;

public class ArrayUtils {

	/**
	 * 判断数组是否是Null或者长度为0
	 * 
	 * @param arr
	 * @return
	 */
	public static boolean isNullOtEmpty(Object arr) {
		if (arr == null) {
			return true;
		}
		if (arr.getClass().isArray()) {
			return Array.getLength(arr) == 0;
		}
		throw new IllegalArgumentException("arr is not array");
	}

}
