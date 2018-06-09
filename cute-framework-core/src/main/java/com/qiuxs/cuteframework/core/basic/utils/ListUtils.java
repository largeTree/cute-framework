package com.qiuxs.cuteframework.core.basic.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ListUtils extends CollectionUtils {

	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> listToMap(List<V> list, String fieldName) {
		Map<K, V> map = new HashMap<>();
		if (list == null || list.size() == 0) {
			return map;
		}
		Field field = ReflectUtils.getAccessibleField(list.get(0).getClass(), fieldName);
		for (V v : list) {
			try {
				map.put((K) field.get(v), v);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

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
