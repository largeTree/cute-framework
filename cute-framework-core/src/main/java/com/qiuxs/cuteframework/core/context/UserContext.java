package com.qiuxs.cuteframework.core.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.qiuxs.cuteframework.core.basic.bean.UserLite;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;

/**
 * 
 * 功能描述: 回话上下文<br/>
 * 新增原因: TODO<br/>
 * 新增日期: 2018年4月23日 下午10:42:09 <br/>
 * 
 * @author qiuxs
 * @version 1.0.0
 */
public class UserContext {

	private static final String TL_USER_LITE = "tl_user_lite";

	/** 回话缓存 */
	private static final Map<String, UserLite> SESSION_HOLDER = new ConcurrentHashMap<>();

	/**
	 * 添加一个会话信息
	 * 
	 * @param userLite
	 */
	public static void addUserLite(UserLite userLite) {
		SESSION_HOLDER.put(userLite.getSessionId(), userLite);
	}
	
	/**
	 * 获取会话信息，不存在时抛出异常
	 * @param sessionId
	 * @return
	 */
	public static UserLite getUserLite(String sessionId) {
		return getUserLite(sessionId, false);
	}

	/***
	 * 根据sessionId从缓存中获取会话信息并存入当前线程变量
	 * 
	 * @param sessionId
	 * @return
	 */
	public static UserLite getUserLite(String sessionId, boolean ignoreException) {
		Map<String, UserLite> sessionMap = getSessionMap();
		UserLite userLite = sessionMap.get(sessionId);
		if (!ignoreException && userLite == null) {
			ExceptionUtils.throwLoginException();
		}
		if (userLite != null) {
			setUserLite(userLite);
		}
		return userLite;
	}

	/**
	 * 获取会话缓存Map
	 * 
	 * @return
	 */
	private static Map<String, UserLite> getSessionMap() {
		return SESSION_HOLDER;
	}

	/**
	 * 设置当前线程的会话信息
	 * 
	 * @author qiuxs
	 * @param userLite
	 */
	public static void setUserLite(UserLite userLite) {
		ThreadLocalVariableHolder.setVariable(TL_USER_LITE, userLite);
	}

	/**
	 * 获取当前线程的会话信息
	 * 
	 * @author qiuxs
	 * @return
	 */
	public static UserLite getUserList() {
		return getUserLite(false);
	}

	/**
	 * 获取当前线程回话信息
	 * 
	 * @author qiuxs
	 * @param ignoreException
	 *            会话无效时是否抛出异常
	 * @return
	 */
	public static UserLite getUserLite(boolean ignoreException) {
		UserLite userLite = getUserLiteInner();
		if (!ignoreException && userLite == null) {
			ExceptionUtils.throwLoginException();
		}
		return userLite;
	}

	/**
	 * 内部调用
	 * 
	 * @author qiuxs
	 * @return
	 */
	private static UserLite getUserLiteInner() {
		return ThreadLocalVariableHolder.getVariable(TL_USER_LITE);
	}
}
