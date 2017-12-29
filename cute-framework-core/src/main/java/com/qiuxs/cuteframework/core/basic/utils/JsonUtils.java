package com.qiuxs.cuteframework.core.basic.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * json工具封装
 * @author qiuxs
 *
 */
public class JsonUtils {
	
	/**
	 * javaBean转JSONObject
	 * @param bean
	 * @return
	 */
	public static JSONObject bean2JSONObject(Object bean) {
		return (JSONObject) JSON.toJSON(bean);
	}
	
}
