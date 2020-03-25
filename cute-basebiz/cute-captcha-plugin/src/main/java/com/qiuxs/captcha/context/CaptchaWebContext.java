package com.qiuxs.captcha.context;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证码插件web上下文
 * @author qiuxs
 * 2019年4月2日 下午10:25:06
 */
public class CaptchaWebContext {

	private static ThreadLocal<Map<String, Object>> tlWebContext = new ThreadLocal<>();
	
	/** 客户端IP地址 */
	public static final String IP = "__cliIp";

	/**
	 * 获取请求IP地址
	 * 
	 * 2019年4月2日 下午10:32:35
	 * @auther qiuxs
	 * @return
	 */
	public static String getCliIP() {
		return (String) getVariable(IP);
	}
	
	/**
	 * 清空所有变量
	 * 
	 * 2019年4月2日 下午10:28:50
	 * @auther qiuxs
	 * @return
	 */
	public static Map<String, Object> clear() {
		Map<String, Object> map = getTlMap();
		tlWebContext.set(null);
		return map;
	}

	/**
	 * 获取一个变量
	 * 
	 * 2019年4月2日 下午10:28:03
	 * @auther qiuxs
	 * @param key
	 * @return
	 */
	public static Object getVariable(String key) {
		return getTlMap().get(key);
	}

	/**
	 * 设置一个变量
	 * 
	 * 2019年4月2日 下午10:27:42
	 * @auther qiuxs
	 * @param key
	 * @param val
	 * @return
	 */
	public static Object setVariable(String key, Object val) {
		return getTlMap().put(key, val);
	}

	/**
	 * 获取缓存Map
	 * 
	 * 2019年4月2日 下午10:27:09
	 * @auther qiuxs
	 * @return
	 */
	private static Map<String, Object> getTlMap() {
		Map<String, Object> map = tlWebContext.get();
		if (map == null) {
			map = new HashMap<>();
			tlWebContext.set(map);
		}
		return map;
	}

}
