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

		for (int i = 0; i < keyValues.length - 1; i+=2) {
			map.put(keyValues[i] == null ? null : String.valueOf(keyValues[i]), keyValues[i + 1]);
		}

		return map;
	}
	
	/**
	 * 
	 *  
	 * @author qiuxs  
	 * @param keyValues
	 * @return
	 */
	public static Map<String, String> genStringMap(Object...keyValues) {
		if (keyValues == null || keyValues.length == 0) {
			return new HashMap<String, String>();
		}
		if (keyValues.length % 2 != 0) {
			throw new RuntimeException("Key/Value 必须成对出现");
		}
		Map<String, String> map = new HashMap<String, String>();

		for (int i = 0; i < keyValues.length - 1; i+=2) {
			map.put(keyValues[i] == null ? null : String.valueOf(keyValues[i]), String.valueOf(keyValues[i + 1]));
		}

		return map;
	}

	/**
	 * 获取String值 为Null时 返回 Null
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
	 * 获取long值，不存在时返回默认值
	 * 
	 * 2019年4月29日 下午6:45:20
	 * @auther qiuxs
	 * @param map
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static long getLongValue(Map<String, ?> map, String key, long defaultValue) {
		Long l = getLong(map, key);
		return l == null ? defaultValue : l.longValue();
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
	
	/**
	 * 获取Boolean值，为空时返回null
	 * @author qiuxs
	 *
	 * @param map
	 * @param key
	 * @return
	 *
	 * 创建时间：2018年9月20日 下午11:02:42
	 */
	public static Boolean getBoolean(Map<String, ?> map, String key) {
		return TypeAdapter.toBoolean(map.get(key));
	}

	/**
	 * 获取boolean值，为空时抛出异常
	 * @author qiuxs
	 *
	 * @param map
	 * @param key
	 * @return
	 *
	 * 创建时间：2018年9月20日 下午11:04:41
	 */
	public static Boolean getBooleanMust(Map<String, ?> map, String key) {
		Boolean val = getBoolean(map, key);
		checkNull(val, key);
		return val;
	}
	
	/**
	 * 获取boolean值，不存在时返回默认值
	 * @author qiuxs
	 *
	 * @param map
	 * @param key
	 * @param defaultVal
	 * @return
	 *
	 * 创建时间：2018年9月20日 下午11:05:59
	 */
	public static boolean getBooleanValue(Map<String, ?> map, String key, boolean defaultVal) {
		Boolean val = getBoolean(map, key);
		return val == null ? defaultVal : val;
	}
	
	private static void checkNull(Object val, String key) {
		if (val == null) {
			ExceptionUtils.throwLogicalException("param_required", key);
		}
	}

}
