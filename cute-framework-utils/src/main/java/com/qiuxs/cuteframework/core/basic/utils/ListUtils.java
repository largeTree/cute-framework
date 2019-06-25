package com.qiuxs.cuteframework.core.basic.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	 * 将List中的元素以元素中某个字段为Key转为Map
	 * 
	 * 2019年6月15日 下午9:40:54
	 * @auther qiuxs
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
	@SuppressWarnings("unchecked")
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
