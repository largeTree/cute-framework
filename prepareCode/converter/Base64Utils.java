package com.hzecool.fdn.utils.converter;

import com.hzecool.fdn.utils.StringUtils;

/**
 * 
 * 功能描述: <p>  
 * 新增原因: TODO<p>  
 * 新增日期: 2019年5月30日 下午2:46:23 <p>  
 *  
 * @author fengdg   
 * @version 1.0.0
 */
public class Base64Utils extends org.springframework.util.Base64Utils {

	public static String encodeImageWithPrefix(byte[] bytes) {
		return encodeToStringWithPrefix(bytes, "data:image/png;base64,");
	}
	
	public static String encodeToStringWithPrefix(byte[] bytes, String prefix) {
		return prefix + org.springframework.util.Base64Utils.encodeToString(bytes);
	}
	
	public static byte[] decodeFromStringWithPrefix(byte[] base64) {
		return decodeFromStringWithPrefix(new String(base64));
	}
	
	public static byte[] decodeFromStringWithPrefix(String base64Str) {
		return Base64Utils.decodeFromString(StringUtils.substringAfter(base64Str, "base64,"));
	}
	
}
