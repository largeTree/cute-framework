package com.qiuxs.cuteframework.core.basic.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
	 * @author qiuxs
	 *
	 * @param list
	 * @param fieldName
	 * @return
	 *
	 * 创建时间：2018年7月26日 下午10:16:01
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
			Field field = ReflectUtils.getAccessibleField(list.get(0).getClass(), fieldName);
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
	 * @author qiuxs
	 *
	 * @param vals
	 * @return
	 *
	 * 创建时间：2018年7月26日 下午10:19:56
	 */
	@SuppressWarnings("unchecked")
	public static <T> Optional<List<T>> genList(T... vals) {
		List<T> list = null;
		if (vals != null) {
			list = new ArrayList<>();
			for (T val : vals) {
				list.add(val);
			}
		}
		return Optional.ofNullable(list);
	}

}
