package com.qiuxs.cuteframework.core.basic.utils;

import java.util.Collection;

public class CollectionUtils {
	/**
	 * 是否是空列表
	 * @param list
	 * @return
	 */
	public static boolean isNullOrEmpty(Collection<?> coll) {
		return coll == null ? true : coll.size() == 0;
	}
}
