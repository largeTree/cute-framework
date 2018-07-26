package com.qiuxs.cuteframework.core.basic.utils;

import java.util.Collection;

/**
 * 集合工具
 * @author qiuxs
 * 
 * 创建时间 ： 2018年7月26日 下午10:10:58
 *
 */
public class CollectionUtils {
	/**
	 * 是否是空集合
	 * @param list
	 * @return
	 */
	public static boolean isNullOrEmpty(Collection<?> coll) {
		return coll == null ? true : coll.size() == 0;
	}
}
