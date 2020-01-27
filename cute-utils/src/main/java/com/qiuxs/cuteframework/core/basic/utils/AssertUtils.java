package com.qiuxs.cuteframework.core.basic.utils;

/**
 * 断言工具
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年1月27日 下午12:27:50 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class AssertUtils {

	/**
	 * 判断不为空
	 *  
	 * @author qiuxs  
	 * @param obj
	 * @param msgId
	 * @param args
	 */
	public static void assertNotNull(Object obj, String msgId, Object... args) {
		if (obj == null) {
			ExceptionUtils.throwLogicalException(msgId, args);
		}
	}

	/**
	 * 判断为空
	 *  
	 * @author qiuxs  
	 * @param obj
	 * @param msgId
	 * @param args
	 */
	public static void assertNull(Object obj, String msgId, Object args) {
		if (obj != null) {
			ExceptionUtils.throwLogicalException(msgId, args);
		}
	}

}
