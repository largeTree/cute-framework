package com.qiuxs.cuteframework.web.auth;

import com.qiuxs.cuteframework.core.basic.bean.UserLite;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;
import com.qiuxs.cuteframework.core.context.UserContext;
import com.qiuxs.cuteframework.web.action.ActionConstants;
import com.qiuxs.cuteframework.web.bean.ReqParam;
import com.qiuxs.cuteframework.web.controller.api.ApiConfig;

/**
 * 接口认证服务
 * @author qiuxs
 *
 */
public class ApiAuthService {

	/**
	 * 会话验证
	 * @param apiConfig
	 * @param params
	 */
	public final void checkAndSetSession(ApiConfig apiConfig, ReqParam params) {
		String sessionId = params.getString(ActionConstants.PARAM_SESSION_ID);
		if (StringUtils.isBlank(sessionId)) {
			if (apiConfig.isLoginFlag()) {
				ExceptionUtils.throwLoginException("param_required", "sessionId");
			}
		} else {
			UserLite userLite = UserContext.getUserLite(sessionId, true);
			if (userLite == null && apiConfig.isLoginFlag()) {
				ExceptionUtils.throwLoginException("session_timeout");
			}

			if (userLite != null) {
				// 子类校验
				this.sessionCheckInner(apiConfig, params, userLite);
				UserContext.trigger(sessionId);
				UserContext.setUserLite(userLite);
			}
		}
	}
	
	/**
	 * 仅检查不自动抛出异常
	 *  
	 * @author qiuxs  
	 * @param sessionId
	 * @return
	 */
	public final boolean checkAndSetSession(String sessionId) {
		if (StringUtils.isBlank(sessionId)) {
			return false;
		}
		UserLite userLite = UserContext.getUserLite(sessionId);
		if (userLite == null) {
			return false;
		}
		this.sessionCheckInner(userLite);
		UserContext.setUserLite(userLite);
		return true;
	}
	
	protected void sessionCheckInner(UserLite userLite) { }
	
	protected void sessionCheckInner(ApiConfig apiConfig, ReqParam params, UserLite userLite) {}
	
	/**
	 * 认证检查
	 * @param apiConfig
	 * @param params
	 */
	public void authCheck(ApiConfig apiConfig, ReqParam params) {
		
	}
	
	/**
	 * 获取api授权服务
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public static ApiAuthService getApiAuthService() {
		// 为空的情况下 初始化一个
		// 先获取是否有注册的apiAuthService
		ApiAuthService bean = ApplicationContextHolder.getBean(ApiAuthService.class);
		if (bean == null) {
			// 没有注册的直接new一个默认的
			bean = new ApiAuthService();
		}
		return bean;
	}

}
