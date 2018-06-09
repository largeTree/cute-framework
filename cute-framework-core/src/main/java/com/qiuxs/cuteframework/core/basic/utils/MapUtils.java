package com.qiuxs.cuteframework.core.basic.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Map封装工具类
 * 
 * @author qiuxs
 *
 */
public class MapUtils {

	/**
	 * 快速生成Map
	 * 
	 * @param keyValues
	 * @return
	 */
	public static Map<String, Object> genMap(Object... keyValues) {
		if (keyValues == null || keyValues.length == 0) {
			return new HashMap<String, Object>();
		}
		if (keyValues.length % 2 != 0) {
			throw new RuntimeException("Key/Value 必须成对出现");
		}
		Map<String, Object> map = new HashMap<String, Object>();

		for (int i = 0; i < keyValues.length - 1; i++) {
			map.put(keyValues[i] == null ? null : String.valueOf(keyValues[i]), keyValues[i + 1]);
		}

		return map;
	}

	/**
	 * 获取String值 为Null时 返回 "null"
	 * 
	 * @param map
	 * @param key
	 * @return
	 */
	public static String getString(Map<String, ?> map, String key) {
		Object obj = map.get(key);
		return obj == null ? null : obj.toString();
	}

	/**
	 * 获取String值 不存在或值为Null时 抛出异常
	 * 
	 * @param map
	 * @param key
	 * @return
	 * @throws NumberFormatException
	 */
	public static String getStringMust(Map<String, ?> map, String key) throws NullPointerException {
		String val = getString(map, key);
		checkNull(val, key);
		return val;
	}

	/**
	 * 获取Integer值 当值为Null时返回Null，值为非整形数字时 抛出异常
	 * 
	 * @param map
	 * @param key
	 * @return
	 * @throws NumberFormatException
	 */
	public static Integer getInteger(Map<String, ?> map, String key) throws NumberFormatException {
		return TypeAdapter.toInteger(map.get(key));
	}

	/**
	 * 获取Integer值 当值为Null时，抛出异常
	 * 
	 * @param map
	 * @param key
	 * @return
	 */
	public static Integer getIntegerMust(Map<String, ?> map, String key)
			throws NumberFormatException, NullPointerException {
		Integer val = getInteger(map, key);
		checkNull(key, key);
		return val;
	}

	/**
	 * 获取Long值 当值为Null时返回Null，值为非整形数字时 抛出异常
	 * 
	 * @param map
	 * @param key
	 * @return
	 * @throws NumberFormatException
	 */
	public static Long getLong(Map<String, ?> map, String key) throws NumberFormatException {
		return TypeAdapter.toLong(map.get(key));
	}

	/**
	 * 获取Long值 当值为Null时，抛出异常
	 * 
	 * @param configItems
	 * @param key
	 * @return
	 */
	public static Long getLongMust(Map<String, ?> map, String key) throws NumberFormatException, NullPointerException {
		Long val = getLong(map, key);
		checkNull(val, key);
		return val;
	}

	private static void checkNull(Object val, String key) {
		if (val == null) {
			ExceptionUtils.throwLogicalException("param_required", key);
		}
	}

}
