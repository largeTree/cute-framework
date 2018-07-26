package com.qiuxs.cuteframework.core.basic.utils;

/**
 * 扩展的字符串工具
 * @author qiuxs
 * 
 * 创建时间 ： 2018年7月26日 下午10:22:52
 *
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

	/**
	 * 高效拼接字符串
	 * @author qiuxs
	 *
	 * @param strs
	 * @return
	 *
	 * 创建时间：2018年7月26日 下午10:24:05
	 */
	public static String append(String... strs) {
		StringBuilder sb = new StringBuilder();
		for (String str : strs) {
			sb.append(str);
		}
		return sb.toString();
	}
}
