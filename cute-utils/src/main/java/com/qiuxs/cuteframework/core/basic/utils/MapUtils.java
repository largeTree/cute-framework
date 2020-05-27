package com.qiuxs.cuteframework.core.basic.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qiuxs.cuteframework.core.basic.utils.reflect.FieldUtils;

/**
 * Map封装工具类
 * 
 * @author qiuxs
 *
 */
public class MapUtils {
	
	private static Logger log = LogManager.getLogger(MapUtils.class);

	/**
	 * 收集指定字段组成新列表
	 *  
	 * @author qiuxs  
	 * @param list
	 * @param fields
	 * @return
	 */
	public static List<Map<String, Object>> collectList(List<? extends Object> list, String fields) {
		return collectList(list, CollectionUtils.stringToSet(fields));
	}
	
	/**
	 * 收集指定字段组成新列表
	 *  
	 * @author qiuxs  
	 * @param list
	 * @param fields
	 * @return
	 */
	public static List<Map<String, Object>> collectList(List<? extends Object> list, Set<String> fieldNames) {
		if (ListUtils.isNullOrEmpty(list)) {
			return Collections.emptyList();
		}
		if (CollectionUtils.isEmpty(fieldNames)) {
			throw new RuntimeException("请指定需要收集的字段列表");
		}
		List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>(list.size());
		Class<? extends Object> clz = list.get(0).getClass();
		List<Field> allFields = FieldUtils.getDeclaredFieldsNoDup(clz);
		List<Field> neededFields = new ArrayList<Field>(fieldNames.size());
		for (Field f : allFields) {
			if (fieldNames.contains(f.getName())) {
				FieldUtils.makeAccessible(f);
				neededFields.add(f);
			}
		}
		for (Object item : list) {
			Map<String, Object> mapItem = new HashMap<String, Object>(neededFields.size());
			for (Field f : neededFields) {
				try {
					Object val = f.get(item);
					mapItem.put(f.getName(), val);
				} catch (Exception e) {
					log.warn("getValue failed, name = " + f.getName(), e);
				}
			}
			newList.add(mapItem);
		}
		return newList;
	}
	
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
		Object val = map.get(key);
		if (val instanceof String) {
			return (String) val;
		}
		return val == null ? null : val.toString();
	}

	/**
	 * 获取String值，不存在时返回默认值
	 *  
	 * @author qiuxs  
	 * @param map
	 * @param key
	 * @param defVal
	 * @return
	 */
	public static String getString(Map<String, ?> map, String key, String defVal) {
		String string = getString(map, key);
		if (string == null) {
			string = defVal;
		}
		return string;
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
		Object val = map.get(key);
		if (val instanceof Integer) {
			return (Integer) val;
		}
		return TypeAdapter.toInteger(val);
	}
	
	/**
	 * 获取int值、不存在返回默认值
	 *  
	 * @author qiuxs  
	 * @param map
	 * @param key
	 * @param defVal
	 * @return
	 */
	public static int getIntValue(Map<String, ?> map, String key, int defVal) {
		Integer integer = getInteger(map, key);
		if (integer == null) {
			integer = defVal;
		}
		return integer;
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
		Object val = map.get(key);
		if (val instanceof Long) {
			return (Long) val;
		}
		String strVal = getString(map, key);
		if (StringUtils.isNotBlank(strVal)) {
			return TypeAdapter.toLong(strVal);
		}
		return null; 
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
		Object val = map.get(key);
		if (val instanceof Boolean) {
			return (Boolean) val;
		}
		return TypeAdapter.toBoolean(val);
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
	 * 获取BigDecimal值
	 *  
	 * @author qiuxs  
	 * @param map
	 * @param string
	 * @return 
	 */
	public static BigDecimal getBigDecimal(Map<String, ?> map, String key) {
		String val = getString(map, key);
		if (StringUtils.isBlank(val)) {
			return null;
		}
		return new BigDecimal(val);
	}
	
	/**
	 * 获取BigDeimal值，不存在抛出异常
	 *  
	 * @author qiuxs  
	 * @param reqParam
	 * @param key
	 * @return
	 */
	public static BigDecimal getBigDecimalMust(Map<String, ?> map, String key) {
		String str = getStringMust(map, key);
		return new BigDecimal(str);
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
	
	/**
	 * 检查是否为空
	 *  
	 * @author qiuxs  
	 * @param val
	 * @param key
	 */
	private static void checkNull(Object val, String key) {
		if (val == null) {
			ExceptionUtils.throwLogicalException("param_required", key);
		}
	}

	/**
	 * javaBean转map
	 *  
	 * @author qiuxs  
	 * @param request
	 * @return
	 */
	public static Map<String, String> bean2Map(Object request) {
		if (request == null) {
			return Collections.emptyMap();
		}
		List<Field> fields = FieldUtils.getDeclaredFieldsNoDup(request.getClass());
		Map<String, String> map = new HashMap<String, String>();
		for (Field field : fields) {
			if (Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			field.setAccessible(true);
			try {
				Object val = field.get(request);
				if (val != null) {
					map.put(field.getName(), String.valueOf(val));
				}
			} catch (ReflectiveOperationException e) {
				log.warn("get field value failed, ext = " + e.getLocalizedMessage());
			}
		}
		return map;
	}

	/**
	 * 获取日期
	 *  
	 * @author qiuxs  
	 * @param params
	 * @param key
	 * @return
	 */
	public static Date getDate(Map<String, ?> params, String key) {
		Object val = params.get(key);
		if (val instanceof Date) {
			return (Date) val;
		}
		String strDate = getString(params, key);
		if (StringUtils.isBlank(strDate)) {
			return null;
		}
		if (strDate.length() > 10) {
			return DateFormatUtils.parseTime(strDate);
		} else {
			return DateFormatUtils.parseDate(strDate);
		}
	}

}
