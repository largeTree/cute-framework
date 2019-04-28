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
	 * 首字母小写
	 * @param str
	 * @return
	 */
	public static String firstToLowerCase(String str) {
		return append(str.substring(0, 1).toLowerCase(), str.substring(1, str.length()));
	}

	/**
	 * 首字母大写
	 * @param str
	 * @return
	 */
	public static String firstToUpperCase(String str) {
		return append(str.substring(0, 1).toUpperCase(), str.substring(1, str.length()));
	}

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
