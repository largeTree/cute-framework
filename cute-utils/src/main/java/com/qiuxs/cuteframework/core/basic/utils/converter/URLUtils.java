package com.qiuxs.cuteframework.core.basic.utils.converter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.qiuxs.cuteframework.core.basic.Constants;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;

public class URLUtils {
	
	public static String decode(String url) {
		try {
			return URLDecoder.decode(url, Constants.DEFAULT_CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}
	
	public static String encode(String str) {
		try {
			return URLEncoder.encode(str, Constants.DEFAULT_CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}
	
}
