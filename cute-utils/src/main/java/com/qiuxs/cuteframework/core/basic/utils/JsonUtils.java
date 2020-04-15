package com.qiuxs.cuteframework.core.basic.utils;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.DateCodec;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * json工具封装
 * 
 * @author qiuxs
 *
 */
public class JsonUtils {
	
	private static ParserConfig myParserConfig = new ParserConfig();
	static {
		myParserConfig.putDeserializer(Date.class, new DateCodec() {

			@Override
			protected <T> T cast(DefaultJSONParser parser, Type clazz, Object fieldName, Object value) {
				if ("--".equals(String.valueOf(value))) {
					return null;
				}
				return super.cast(parser, clazz, fieldName, value);
			}
			
		});
	}
	
	/**
	 * 复制json属性
	 *  
	 * @author qiuxs  
	 * @param src
	 * @param dest
	 * @param keys
	 */
	public static void copyProp(JSONObject src, JSONObject dest, Set<String> keys) {
		if(src == null || dest == null || CollectionUtils.isEmpty(keys)) {
			return;
		}
		for (String key : keys) {
			dest.put(key, src.get(key));
		}
	}
	
	/**
	 * 准换数组元素key的格式，
	 * 将zk_final_price 转为zkFinalPrice
	 * 2019年5月12日 上午11:48:03
	 * @auther qiuxs
	 * @param jArr
	 */
	public static void parseJSONArrayItemKey(JSONArray jArr) {
		if (ListUtils.isNullOrEmpty(jArr)) {
			return;
		}
		for (int i = 0;i<jArr.size();i++) {
			Object val = jArr.get(i);
			if (val instanceof JSONObject) {
				parseJSONObjectKey((JSONObject) val);
			} else {
				parseJSONArrayItemKey((JSONArray) val);
			}
		}
	}
	
	/**
	 * 转换json的key的格式，将zk_final_price 转为zkFinalPrice
	 * 
	 * 2019年5月12日 上午11:14:51
	 * @auther qiuxs
	 * @param jsonData
	 */
	public static void parseJSONObjectKey(JSONObject jsonData) {
		Set<String> keySet = new HashSet<>(jsonData.keySet());
		for (String key : keySet) {
			Object val = jsonData.remove(key);
			jsonData.put(StringUtils.UnderlineToHump(key), val);
		}
	}

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
		return JSONObject.parseObject(str);
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
		return JSON.parseObject(str, clz, myParserConfig);
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
		return JSON.parseObject(json.toJSONString(), clz, myParserConfig);
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
