package com.qiuxs.cuteframework.core.basic.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ListUtils extends CollectionUtils {
	
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
