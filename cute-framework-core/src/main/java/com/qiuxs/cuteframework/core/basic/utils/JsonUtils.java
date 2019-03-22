package com.qiuxs.cuteframework.core.basic.utils;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * json工具封装
 * 
 * @author qiuxs
 *
 */
public class JsonUtils {

	/**
	 * JavaBean转为JSONObject
	 * @author qiuxs
	 *
	 * @param obj
	 * @return
	 *
	 * 创建时间：2018年7月26日 下午10:12:50
	 */
	public static JSONObject toJSONObject(Object obj) {
		return (JSONObject) JSON.toJSON(obj);
	}

	/**
	 * Java数组或集合转为JSONArray
	 * @author qiuxs
	 *
	 * @param arr
	 * @return
	 *
	 * 创建时间：2018年7月26日 下午10:13:05
	 */
	public static JSONArray toJSONArray(Object arr) {
		return (JSONArray) JSON.toJSON(arr);
	}

	/**
	 * JavaBean转为JSON字符串
	 * @author qiuxs
	 *
	 * @param obj
	 * @return
	 *
	 * 创建时间：2018年7月26日 下午10:13:20
	 */
	public static String toJSONString(Object obj) {
		return JSONObject.toJSONString(obj,
		        SerializerFeature.IgnoreNonFieldGetter,
		        SerializerFeature.WriteNonStringKeyAsString,
		        SerializerFeature.WriteDateUseDateFormat, //日期输出成"yyyy-MM-dd HH:mm:ss"格式
		        SerializerFeature.DisableCircularReferenceDetect);
	}

	/**
	 * JSON字符串转为JSONObject
	 * @author qiuxs
	 *
	 * @param str
	 * @return
	 *
	 * 创建时间：2018年7月26日 下午10:13:34
	 */
	public static JSONObject parseJSONObject(String str) {
		return JSON.parseObject(str);
	}

	/**
	 * JSON字符串转为JSONArray
	 * @author qiuxs
	 *
	 * @param str
	 * @return
	 *
	 * 创建时间：2018年7月26日 下午10:13:47
	 */
	public static JSONArray parseJSONArray(String str) {
		return JSON.parseArray(str);
	}

	/**
	 * JSON字符串转为JavaBean
	 * @author qiuxs
	 *
	 * @param str
	 * @param clz
	 * @return
	 *
	 * 创建时间：2018年7月26日 下午10:14:05
	 */
	public static <T> T parseObject(String str, Class<T> clz) {
		return JSON.parseObject(str, clz);
	}
	
	/**
	 * JSON对象转为JavaBean
	 * @author qiuxs
	 *
	 * @param json
	 * @param clz
	 * @return
	 *
	 * 创建时间：2018年7月26日 下午10:14:05
	 */
	public static <T> T parseObject(JSONObject json, Class<T> clz) {
		return JSONObject.toJavaObject(json, clz);
	}

	/**
	 * JSON字符串转为List
	 * @author qiuxs
	 *
	 * @param str
	 * @param clz
	 * @return
	 *
	 * 创建时间：2018年7月26日 下午10:14:18
	 */
	public static <T> List<T> parseArray(String str, Class<T> clz) {
		return JSON.parseArray(str, clz);
	}
	
	/**
	 * JSON数组转为List
	 * @author qiuxs
	 *
	 * @param jsonArr
	 * @param clz
	 * @return
	 *
	 * 创建时间：2018年7月26日 下午10:14:18
	 */
	public static <T> List<T> parseArray(JSONArray jsonArr, Class<T> clz) {
		return parseArray(jsonArr.toJSONString(), clz);
	}

	/**
	 * 
	 * @author qiuxs
	 *
	 * @param obj
	 * @return
	 *
	 * 创建时间：2018年7月26日 下午10:14:30
	 */
	public static Object genJSON(Object... obj) {
		if (obj == null || obj.length == 0) {
			return null;
		}
		if (obj.length % 2 != 0) {
			ExceptionUtils.throwRuntimeException("参数个数必须为2的背倍数");
		}
		JSONObject json = new JSONObject();
		for (int i = 0; i < obj.length; i += 2) {
			json.put(String.valueOf(obj[i]), obj[i + 1]);
		}
		return json;
	}
}
