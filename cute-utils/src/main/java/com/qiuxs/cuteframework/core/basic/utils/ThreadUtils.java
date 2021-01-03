package com.qiuxs.cuteframework.core.basic.utils;

import java.lang.reflect.Array;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.qiuxs.cuteframework.core.basic.utils.reflect.FieldUtils;

public class ThreadUtils {

	/**
	 * 睡眠
	 *  
	 * @author qiuxs  
	 * @param ms
	 */
	public static void sleep(long ms) {
		try {
			TimeUnit.MILLISECONDS.sleep(ms);
		}catch (Exception e) {
		}
	}
	
	/**
	 * 获取指定线程中的某个线程变量的值，
	 *  
	 * @author fengdg  
	 * @param thread
	 * @param threadLocalKey 线程变量的键值，不是类中定义的属性名。如"global_id"
	 */
	public static Object getThreadLocalMapValue(Thread thread, String threadLocalKey) {
		Object threadLocalMap = FieldUtils.getFieldValueQuietly(thread, "threadLocals");
		if (threadLocalMap != null) {
			Object table = FieldUtils.getFieldValueQuietly(threadLocalMap, "table");
			for (int i = 0, length = Array.getLength(table); i < length; ++i) {
				final Object entry = Array.get(table, i);
				if (entry != null) {
					//ThreadLocal里取不到信息，从value取
					Object value = FieldUtils.getFieldValueQuietly(entry, "value");
					if (value != null && value instanceof Map) {
						Map<?, ?> valueMap = (Map<?, ?>) value;
						if (valueMap.containsKey(threadLocalKey)) {
							Object globalIdObj = valueMap.get(threadLocalKey);
							if (globalIdObj != null) {
								return globalIdObj;
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	public static String getStackTrace(StackTraceElement[] trace) {
		return getStackTrace(trace, null, null);
	}
	
    /**
     * 获取线程堆栈信息
     *  
     * @author fengdg  
     * @param thread
     */
	public static String getStackTrace(Thread thread) {
		StackTraceElement[] trace = thread.getStackTrace();
        return getStackTrace(trace);
	}
	
	public static String getStackTrace(StackTraceElement[] trace, String preClazzName, String lineSep) {
		if (lineSep == null) {
			lineSep = "\r\n";
		}
		StringBuilder sb = new StringBuilder();
        for (StackTraceElement traceElement : trace) {
        	if (StringUtils.isNotEmpty(preClazzName) && 
        			!StringUtils.startsWith(traceElement.getClassName(), preClazzName)) {
        		continue;
			}
        	sb.append("\tat " + traceElement).append(lineSep);
        }
        return sb.toString();
	}
	
}
