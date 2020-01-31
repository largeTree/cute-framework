package com.qiuxs.cuteframework.filters;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.cuteframework.core.context.UserContext;
import com.qiuxs.cuteframework.web.utils.RequestUtils;

public class AuthFilter implements Filter {

	private static Logger log = LogManager.getLogger(AuthFilter.class);

	private static final String LOGIN_PATH_KEY = "login-path";
	private static final String DEFAULT_LOGIN_PATH = "/login";
	
	private static final String WELCOME_PATH = "welcome-path";
	private static final String DEFAULT_WELCOME_PATH = "/view";
	
	private static final String LOGIN_API_KEY = "login-api-key";
	
	private static final String AUTHC_KEY = "Authc";
	private static final String COOKIE_SESSIONID = "sid";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		boolean authFlag = false;
		HttpServletRequest req = (HttpServletRequest) request;
		try {
			// 先取请求头
			String sessionId = req.getHeader(AUTHC_KEY);
			if (StringUtils.isBlank(sessionId)) {
				// 请求头没有的情况下，取cookie
				Map<String, String> cookiesMap = RequestUtils.getCookies(req);
				sessionId = cookiesMap.get(COOKIE_SESSIONID);
			}
			if (StringUtils.isNotBlank(sessionId) && this.checkAuth(sessionId)) {
				authFlag = true;
			}
		} catch (Exception e) {
			log.error("Auth Request Failed ext = " + e.getLocalizedMessage(), e);
		}

		if (!authFlag) {
			this.goLogin(req, response);
		} else {
			chain.doFilter(request, response);
		}

	}

	/**
	 * 重定向到登录页
	 *  
	 * @author qiuxs  
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void goLogin(HttpServletRequest req, ServletResponse resp) throws IOException {
		String redirectPath = req.getContextPath() + this.getLoginPath() + "?redirect=" + req.getContextPath() + this.getWelcomePath() + "&api=" + this.getLoginApiKey();
		((HttpServletResponse) resp).sendRedirect(redirectPath);
	}

	/**
	 * 登陆页，会话失效后重定向到此处
	 *  
	 * @author qiuxs  
	 * @return
	 */
	private String getLoginPath() {
		String loginPath = EnvironmentContext.getString(LOGIN_PATH_KEY, DEFAULT_LOGIN_PATH);
		return StringUtils.handlePath(loginPath);
	}
	
	/**
	 * 欢迎页，登陆成功后重定向到此处
	 *  
	 * @author qiuxs  
	 * @return
	 */
	private String getWelcomePath() {
		String welcomePath = EnvironmentContext.getString(WELCOME_PATH, DEFAULT_WELCOME_PATH);
		return StringUtils.handlePath(welcomePath);
	}
	
	/**
	 * 登陆接口号，传递到登陆页面，共登录页调用
	 *  
	 * @author qiuxs  
	 * @return
	 */
	private String getLoginApiKey() {
		String loginApiKey = EnvironmentContext.getString(LOGIN_API_KEY);
		return loginApiKey;
	}

	/**
	 * 检查会话是否有效
	 *  
	 * @author qiuxs  
	 * @param sessionId
	 * @return
	 */
	private boolean checkAuth(String sessionId) {
		return UserContext.isValid(sessionId);
	}

	@Override
	public void destroy() {

	}

}
