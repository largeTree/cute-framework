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
		private static Map<String, UserLite> SESSION_HOLDER = McFactory.getFactory().createMap("uc_session_map");
		/** 用户ID和sessionId对应关系 */
		private static Map<Long, String> USER_ID_SESSION_ID_HOLDER = McFactory.getFactory().createMap("uc_user_id_session_id_map");
		/** sessionId最后触发时间记录 */
		private static Map<String, Long> SESSION_LAST_TRIGGER = McFactory.getFactory().createMap("uc_session_id_last_trigger");
	}
	
	/** 用户类型：管理员 */
	public static final int USER_TYPE_ADMIN = 1 << 0;
	/** 用户类型：普通用户 */
	public static final int USER_TYPE_USER = 1 << 1;
	
	/** 默认会话过期时间 */
	private static final int DEFAULT_SESSION_TIME_MS = 30 * 60 * 1000;
	
	/**
	 * 根据用户Id获取会话
	 *  
	 * @author qiuxs  
	 * @param id
	 * @return
	 */
	public static UserLite getUserLiteByUserIdOpt(Long id) {
		String sessionId = getIdSessionIdMap().get(id);
		if (StringUtils.isNotBlank(sessionId)) {
			UserLite userLite = getUserLite(sessionId, true);
			return userLite;
		}
		return null;
	}

	/**
	 * 添加一个会话信息
	 * 
	 * @param userLite
	 */
	public static void addUserSession(UserLite userSession) {
		String sessionId = userSession.getSessionId();
		getSessionMap().put(sessionId, userSession);
		// 放入线程变量缓存
		setUserLite(userSession);
		Map<Long, String> idSessionIdMap = getIdSessionIdMap();
		Long userId = userSession.getUserId();
		if (!NumberUtils.isEmpty(userId)) {
    		// 放入新的对应关系
    		String oldSessionId = idSessionIdMap.get(userId);
    		if (StringUtils.isNotBlank(oldSessionId) && !oldSessionId.equals(sessionId)) {
    			// 存在旧会话时，移除旧会话
    			getSessionMap().remove(oldSessionId);
    		}
    		// 缓存userId和sessionId对应关系
			idSessionIdMap.put(userId, sessionId);
		}
		// 保存一下活跃时间
		trigger(sessionId);
	}
	
	/**
	 * 更新会话信息
	 *  
	 * @author qiuxs  
	 * @param userSession
	 */
	public static void setUserSession(UserLite userSession) {
		getSessionMap().put(userSession.getSessionId(), userSession);
	}
	
	/**
	 * 移除会话
	 * @author qiuxs
	 *
	 * @param sesionId
	 *
	 * 创建时间：2018年9月2日 下午10:13:23
	 */
	public static void removeSession(String sessionId) {
		UserLite userLite = getSessionMap().remove(sessionId);
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
	
	public static Long getUnitId() {
		Long unitId = getUnitIdOpt();
		if (unitId == null) {
			ExceptionUtils.throwLoginException();
		}
		return unitId;
	}
	
	public static Long getUnitIdOpt() {
		UserLite userLite = getUserLiteOpt();
		return userLite == null ? null : userLite.getUnitId();
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends UserLite> T getUserSessionOpt() {
		return (T) getUserLiteOpt();
	}

	/**
	 * sessionId是否有效
	 *  
	 * @author qiuxs  
	 * @param sessionId
	 * @return
	 */
	public static boolean isValid(String sessionId) {
		// 支持设置为负数意为，永不过期
		Long lastTrigger = UserContext.getLastTrigger(sessionId);
		boolean valid = true;
		
		// 等于0
		if (lastTrigger == null) {
			valid = false;
		}
		if (lastTrigger >= 0) {
			// 验证是否过期，默认
			int session_timeout_ms = EnvironmentContext.getIntValue("session_timeout_ms", DEFAULT_SESSION_TIME_MS);
			if ((System.currentTimeMillis() - lastTrigger) > session_timeout_ms) {
				UserLite userLite = getSessionMap().remove(sessionId);
				getIdSessionIdMap().remove(userLite.getUserId());
				valid = false;
			}
		}
		// 还有效，更新一下活跃时间
		if (valid) {
			trigger(sessionId);
		}
		return valid;
	}

	private static Long getLastTrigger(String sessionId) {
		return Holder.SESSION_LAST_TRIGGER.get(sessionId);
	}

	public static void trigger(String sessionId) {
		Holder.SESSION_LAST_TRIGGER.put(sessionId, System.currentTimeMillis());
	}

	/**
	 * 获取角色ID
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public static Long getRoleId() {
		Long roleId = getRoleIdOpt();
		if (roleId == null) {
			ExceptionUtils.throwLoginException();
		}
		return roleId;
	}

	/**
	 * 获取角色ID，失败不报错
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public static Long getRoleIdOpt() {
		UserLite userLite = getUserLiteOpt();
		return userLite == null ? null : userLite.getRoleId();
	}

	public static void mockUser(Long unitId) {
		UserLite userLite = new UserLite();
		userLite.setUnitId(unitId);
		setUserLite(userLite);
	}
}
