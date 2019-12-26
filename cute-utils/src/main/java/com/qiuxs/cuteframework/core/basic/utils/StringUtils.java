package com.qiuxs.cuteframework.core.basic.utils;

/**
 * 扩展的字符串工具
 * @author qiuxs
 * 
 * 创建时间 ： 2018年7月26日 下午10:22:52
 *
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

	/***
	 * 下划线命名转为驼峰命名
	 * 
	 * @param para
	 *        下划线命名的字符串
	 */

	public static String UnderlineToHump(String para) {
		StringBuilder result = new StringBuilder();
		String a[] = para.split("_");
		for (String s : a) {
			if (!para.contains("_")) {
				result.append(s);
				continue;
			}
			if (result.length() == 0) {
				result.append(s.toLowerCase());
			} else {
				result.append(s.substring(0, 1).toUpperCase());
				result.append(s.substring(1));
			}
		}
		return result.toString();
	}

	/***
	* 驼峰命名转为下划线命名
	 * 
	 * @param para
	 *        驼峰命名的字符串
	 */

	public static String HumpToUnderline(String para) {
		StringBuilder sb = new StringBuilder(para);
		int temp = 0;//定位
		if (!para.contains("_")) {
			for (int i = 0; i < para.length(); i++) {
				if (Character.isUpperCase(para.charAt(i))) {
					sb.insert(i + temp, "_");
					temp += 1;
				}
			}
		}
		return sb.toString().toLowerCase();
	}

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
