package com.qiuxs.cuteframework.view.config;

import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.cuteframework.web.controller.BaseController;

/**
 * 视图配置
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年2月4日 下午6:53:55 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class ViewConfig {
	
	private static final String WELCOME_PATH = "welcome-path";
	private static final String DEFAULT_WELCOME_PATH = "/view";
	
	private static final String LOGIN_API_KEY = "login-api-key";
	
	
	/**
	 * 登陆页，会话失效后重定向到此处
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public static String getLoginPath() {
		return BaseController.getLoginPath();
	}
	
	/**
	 * 欢迎页，登陆成功后重定向到此处
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public static String getWelcomePath() {
		String welcomePath = EnvironmentContext.getString(WELCOME_PATH, DEFAULT_WELCOME_PATH);
		return StringUtils.handlePath(welcomePath);
	}
	
	/**
	 * 登陆接口号，传递到登陆页面，共登录页调用
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public static String getLoginApiKey() {
		String loginApiKey = EnvironmentContext.getString(LOGIN_API_KEY);
		return loginApiKey;
	}

}
