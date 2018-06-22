package com.qiuxs.cuteframework.core.context;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 功能描述: 线程变量存储器<br/>  
 * 新增原因: 存储一些需要单线程共享的变量<br/>  
 * 新增日期: 2018年4月23日 下午9:52:57 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class TLVariableHolder {

	private static ThreadLocal<Map<String, Object>> thread_local_variables = new ThreadLocal<>();

	/**
	 * 保存一个线程变量
	 *  
	 * @author qiuxs  
	 * @param key
	 * @param value
	 */
	public static void setVariable(String key, Object value) {
		getThreadMap().put(key, value);
	}

	/**
	 * 获取线程变量
	 *  
	 * @author qiuxs  
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getVariable(String key) {
		return (T) getThreadMap().get(key);
	}

	/**
	 * 清空当前线程变量
	 *  
	 * @author qiuxs
	 */
	public static void clear() {
		Map<String, Object> threadLocalMap = thread_local_variables.get();
		if (threadLocalMap != null) {
			threadLocalMap.clear();
			thread_local_variables.set(null);
		}
	}

	/**
	 * 获取当前线程变量集合
	 *  
	 * @author qiuxs  
	 * @return
	 */
	private static Map<String, Object> getThreadMap() {
		Map<String, Object> threadLocalMap = thread_local_variables.get();
		if (threadLocalMap == null) {
			threadLocalMap = new HashMap<>();
			thread_local_variables.set(threadLocalMap);
		}
		return threadLocalMap;
	}

}
