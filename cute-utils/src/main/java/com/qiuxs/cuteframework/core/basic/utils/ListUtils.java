package com.qiuxs.cuteframework.core.basic.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.qiuxs.cuteframework.core.basic.constants.SymbolConstants;
import com.qiuxs.cuteframework.core.basic.utils.reflect.FieldUtils;

/**
 * List相关工具
 * @author qiuxs
 * 
 * 创建时间 ： 2018年7月26日 下午10:15:49
 *
 */
public class ListUtils extends CollectionUtils {
	
	/**
	 * 字符串转为Long列表,使用逗号分隔
	 *  
	 * @author qiuxs  
	 * @param token
	 * @return
	 */
	public static List<Long> stringToLongList(String token) {
		return stringToLongList(token, SymbolConstants.SEPARATOR_COMMA);
	}
	
	/**
	 * 字符串转为Long列表
	 *  
	 * @author qiuxs  
	 * @param token
	 * @param separator
	 * @return
	 */
	public static List<Long> stringToLongList(String token, String separator) {
		return stringToList(token, separator, Long::parseLong);
	}
	
	/**
	 * 字符串转为Integer列表,使用逗号分隔
	 *  
	 * @author qiuxs  
	 * @param token
	 * @return
	 */
	public static List<Integer> stringToIntegerLis(String token) {
		return stringToIntegerList(token, SymbolConstants.SEPARATOR_COMMA);
	}
	
	/**
	 * 字符串转为Integer列表
	 *  
	 * @author qiuxs  
	 * @param token
	 * @param separator
	 * @return
	 */
	public static List<Integer> stringToIntegerList(String token, String separator) {
		return stringToList(token, separator, Integer::parseInt);
	}

	/**
	 * 字符串转为列表
	 *  
	 * @author qiuxs  
	 * @param token
	 * @return
	 */
	public static <T> List<T> stringToList(String token, String separator, Function<String, T> parser) {
		if (StringUtils.isBlank(token)) {
			return emptyList();
		}
		// 仅当为null时置为默认逗号，否则都是用次分隔符
		if (separator == null) {
			separator = SymbolConstants.SEPARATOR_COMMA;
		}
		String[] split = token.split(separator);
		return asList(split, parser);
	}

	/**
	 * 字符串数组转为对应类型的列表
	 *  
	 * @author qiuxs  
	 * @param <T>
	 * @param split
	 * @param parser
	 * @return
	 */
	public static <T> List<T> asList(String[] arr, Function<String, T> parser) {
		List<T> list = new ArrayList<>();
		for (String item : arr) {
			list.add(parser.apply(item));
		}
		return list;
	}

	/**
	 * 空列表
	 * @return
	 */
	public static <T> List<T> emptyList() {
		return Collections.emptyList();
	}

	/**
	 * 将List中的元素以元素中某个字段为Key转为Map
	 * 
	 * 2019年6月15日 下午9:40:54
	 * @author qiuxs
	 * @param list
	 * @param fieldName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> listToMap(List<V> list, String fieldName) {
		Map<K, V> map = new HashMap<>();
		if (list == null || list.size() == 0) {
			return map;
		}
		// 处理List中元素为Map的情况
		if (list.get(0) instanceof Map) {
			for (V v : list) {
				map.put(((Map<?, K>) v).get(fieldName), v);
			}
		} else {
			Field field = FieldUtils.getAccessibleField(list.get(0).getClass(), fieldName);
			for (V v : list) {
				try {
					map.put((K) field.get(v), v);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}

	/**
	 * 数组转为ArrayList
	 * 
	 * 2019年6月15日 下午9:40:39
	 * @auther qiuxs
	 * @param vals
	 * @return
	 */
	@SafeVarargs
	public static <T> List<T> genList(T... vals) {
		List<T> list = new ArrayList<>();
		if (vals != null) {
			list = new ArrayList<>();
			for (T val : vals) {
				list.add(val);
			}
		}
		return list;
	}

	/**
	 * 把list转换为一个用逗号分隔的字符串
	 * 
	 * 2019年6月15日 下午9:40:46
	 * @auther qiuxs
	 * @param list
	 * @return
	 */
	public static String listToString(Collection<?> list) {
		if (isNotEmpty(list)) {
			StringBuilder sb = new StringBuilder();
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				String strItem;
				Object item = iter.next();
				if (item != null) {
					strItem = item.toString();
				} else {
					strItem = "";
				}
				sb.append(strItem).append(",");
			}
			sb.setLength(sb.length() - 1);
			return sb.toString();
		} else {
			return "";
		}
	}

}
