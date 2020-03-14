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
import com.qiuxs.cuteframework.view.config.ViewConfig;
import com.qiuxs.cuteframework.web.auth.ApiAuthService;
import com.qiuxs.cuteframework.web.utils.RequestUtils;

public class AuthFilter implements Filter {

	private static Logger log = LogManager.getLogger(AuthFilter.class);

	private static final String AUTHC_KEY = "Authc";
	private static final String COOKIE_SESSIONID = "sid";

	private ApiAuthService apiAuthService; 
	
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
			if (StringUtils.isNotBlank(sessionId) && this.getApiAuthService().checkAndSetSession(sessionId)) {
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
		String redirectPath = req.getContextPath() + ViewConfig.getLoginPath();
		((HttpServletResponse) resp).sendRedirect(redirectPath);
	}

	private ApiAuthService getApiAuthService() {
		if (this.apiAuthService == null) {
			this.apiAuthService = ApiAuthService.getApiAuthService();
		}
		return this.apiAuthService;
	}

	@Override
	public void destroy() {

	}

}
