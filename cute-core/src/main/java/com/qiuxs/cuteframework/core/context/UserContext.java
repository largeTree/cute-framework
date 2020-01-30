package com.qiuxs.cuteframework.core.context;

import java.util.Map;

import com.qiuxs.cuteframework.core.basic.bean.UserLite;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.NumberUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.tech.mc.McFactory;

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

	private static class Holder {
		/** 会话缓存 */
		private static Map<String, UserLite> SESSION_HOLDER = McFactory.getFactory().createMap(String.class, UserLite.class, 10, "uc_session_map");
		/** 用户ID和sessionId对应关系 */
		private static Map<Long, String> USER_ID_SESSION_ID_HOLDER = McFactory.getFactory().createMap(Long.class, String.class, 10, "uc_user_id_session_id_map");
	}
	
	/** 用户类型：管理员 */
	public static final int USER_TYPE_ADMIN = 0;
	/** 用户类型：普通用户 */
	public static final int USER_TYPE_USER = 1;
	
	/** 默认会话过期时间 */
	private static final int DEFAULT_SESSION_TIME_MS = 10 * 60 * 1000;

	/**
	 * 添加一个会话信息
	 * 
	 * @param userLite
	 */
	public static void addUserLite(UserLite userLite) {
		// 等于0的设置一下
		if (userLite.getLastTrigger() == 0) {
			userLite.setLastTrigger(System.currentTimeMillis());
		}
		getSessionMap().put(userLite.getSessionId(), userLite);
		// 放入线程变量缓存
		setUserLite(userLite);
		Map<Long, String> idSessionIdMap = getIdSessionIdMap();
		Long userId = userLite.getUserId();
		if (!NumberUtils.isEmpty(userId)) {
    		// 放入新的对应关系
    		String oldSessionId = idSessionIdMap.get(userId);
    		if (StringUtils.isNotBlank(oldSessionId)) {
    			// 存在旧会话时，移除旧会话
    			getSessionMap().remove(oldSessionId);
    		}
    		// 缓存userId和sessionId对应关系
			idSessionIdMap.put(userId, userLite.getSessionId());
		}
	}
	
	/**
	 * 移除会话
	 * @author qiuxs
	 *
	 * @param sesionId
	 *
	 * 创建时间：2018年9月2日 下午10:13:23
	 */
	public static void removeSession(String sesionId) {
		UserLite userLite = getSessionMap().get(sesionId);
		if (userLite != null) {
			getIdSessionIdMap().remove(userLite.getUserId());
		}
	}

	/**
	 * 获取会话信息，不存在时抛出异常
	 * 
	 * @return
	 */
	public static UserLite getUserLite() {
		UserLite userLite = getUserLiteOpt();
		if (userLite == null) {
			ExceptionUtils.throwLoginException();
		}
		return userLite;
	}

	/**
	 * 获取会话信息
	 * 
	 * @return
	 */
	public static UserLite getUserLiteOpt() {
		return TLVariableHolder.getVariable(TL_USER_LITE);
	}

	/**
	 * 从缓存中获取会话信息并保存到本地线程变量
	 * 
	 * @param sessionId
	 * @return
	 */
	public static UserLite getUserLite(String sessionId) {
		UserLite userLite = getUserLite(sessionId, false);
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
		TLVariableHolder.setVariable(TL_USER_LITE, userLite);
	}

	/**
	 * 获取会话缓存Map
	 * 
	 * @return
	 */
	private static Map<String, UserLite> getSessionMap() {
		return Holder.SESSION_HOLDER;
	}

	/**
	 * 获取用户ID和sessionId对应关系Map
	 * 
	 * @return
	 */
	private static Map<Long, String> getIdSessionIdMap() {
		return Holder.USER_ID_SESSION_ID_HOLDER;
	}

	/**
	 * 获取当前用户ID
	 * @author qiuxs
	 *
	 * @return
	 *
	 * 创建时间：2018年8月8日 下午9:21:23
	 */
	public static Long getUserId() {
		Long userId = getUserIdOpt();
		if (userId == null) {
			ExceptionUtils.throwLoginException();
		}
		return userId;
	}

	public static Long getUserIdOpt() {
		UserLite userLite = getUserLiteOpt();
		return userLite == null ? null : userLite.getUserId();
	}

	/**
	 * sessionId是否有效
	 *  
	 * @author qiuxs  
	 * @param sessionId
	 * @return
	 */
	public static boolean isValid(String sessionId) {
		Map<String, UserLite> sessionMap = getSessionMap();
		UserLite userLite = sessionMap.get(sessionId);
		if (userLite == null) {
			return false;
		}
		long lastTrigger = userLite.getLastTrigger();
		// 支持设置为负数意为，永不过期
		if (lastTrigger >= 0) {
			// 验证是否过期，默认
			int session_timeout_ms = EnvironmentContext.getIntValue("session_timeout_ms", DEFAULT_SESSION_TIME_MS);
			if ((System.currentTimeMillis() - lastTrigger) > session_timeout_ms) {
				getIdSessionIdMap().remove(userLite.getUserId());
				getSessionMap().remove(sessionId);
				return false;
			}
			// 更新一下最后触发时间
			userLite.setLastTrigger(System.currentTimeMillis());
			sessionMap.put(sessionId, userLite);
		}
		return true;
	}
}
