package com.qiuxs.cuteframework.core.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.qiuxs.cuteframework.core.basic.bean.UserLite;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;

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

	/** 会话缓存 */
	private static final Map<String, UserLite> SESSION_HOLDER = new ConcurrentHashMap<>();
	/** 用户ID和sessionId对应关系 */
	private static final Map<Long, String> USER_ID_SESSION_ID_HOLDER = new ConcurrentHashMap<>();

	/**
	 * 添加一个会话信息
	 * 
	 * @param userLite
	 */
	public static void addUserLite(UserLite userLite) {
		getSessionMap().put(userLite.getSessionId(), userLite);
		Map<Long, String> idSessionIdMap = getIdSessionIdMap();
		// 放入新的对应关系
		String oldSessionId = idSessionIdMap.put(userLite.getUserId(), userLite.getSessionId());
		if (StringUtils.isNotBlank(oldSessionId)) {
			// 存在旧会话时，移除旧会话
			getSessionMap().remove(oldSessionId);
		}

	}

	/**
	 * 从本地线程变量获取会话信息
	 * 
	 * @return
	 */
	public static UserLite getUserLite() {
		UserLite userLite = getUserLiteInner();
		if (userLite == null) {
			ExceptionUtils.throwLoginException();
		}
		return userLite;
	}

	/**
	 * 内部调用
	 * 
	 * @return
	 */
	private static UserLite getUserLiteInner() {
		return ThreadLocalVariableHolder.getVariable(TL_USER_LITE);
	}

	/**
	 * 从缓存中获取会话信息并保存到本地线程变量
	 * 
	 * @param sessionId
	 * @return
	 */
	public static UserLite getUserLite(String sessionId) {
		UserLite userLite = getUserLite(sessionId, false);
		setUserLite(userLite);
		return userLite;
	}

	/**
	 * 从缓存中获取会话信息
	 * 
	 * @param sessionId
	 * @param ignoreException
	 * @return
	 */
	public static UserLite getUserLite(String sessionId, boolean ignoreException) {
		UserLite userLite = getSessionMap().get(sessionId);
		if (!ignoreException && userLite == null) {
			ExceptionUtils.throwLoginException();
		}
		return userLite;
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
	 * 获取会话缓存Map
	 * 
	 * @return
	 */
	private static Map<String, UserLite> getSessionMap() {
		return SESSION_HOLDER;
	}

	/**
	 * 获取用户ID和sessionId对应关系Map
	 * 
	 * @return
	 */
	private static Map<Long, String> getIdSessionIdMap() {
		return USER_ID_SESSION_ID_HOLDER;
	}
}
