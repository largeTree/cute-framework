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

/*@WebFilter(filterName = "authFilter", urlPatterns = {
        "/",
        "/sys/*",
        "/view/*"
})*/
public class AuthFilter implements Filter {

	private static Logger log = LogManager.getLogger(AuthFilter.class);

	private static final String LOGIN_PATH_KEY = "login-path";
	private static final String DEFAULT_LOGIN_PATH = "/login";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		boolean authFlag = false;
		HttpServletRequest req = (HttpServletRequest) request;
		try {
			Map<String, String> cookiesMap = RequestUtils.getCookies(req);
			String sessionId = cookiesMap.get("sid");
			if (StringUtils.isNotBlank(sessionId) && this.checkAuth(sessionId)) {
				authFlag = true;
			}
		} catch (Exception e) {
			log.error("Auth Request Failed ext = " + e.getLocalizedMessage(), e);
			this.goLogin(req, response);
		}

		if (!authFlag) {
			this.goLogin(req, response);
			return;
		} else {
			chain.doFilter(request, response);
		}

	}

	private void goLogin(HttpServletRequest req, ServletResponse resp) throws IOException {
		String requestURI = req.getRequestURI();
		String redirectPath = req.getContextPath() + this.getLoginPath() + "?redirect=" + requestURI + "&api=" + this.getLoginApiKey();
		((HttpServletResponse) resp).sendRedirect(redirectPath);
	}

	private String getLoginPath() {
		String loginPath = EnvironmentContext.getEnvValue(LOGIN_PATH_KEY);
		if (StringUtils.isBlank(loginPath)) {
			loginPath = DEFAULT_LOGIN_PATH;
		}
		if (!loginPath.startsWith("/")) {
			loginPath = "/" + loginPath;
		}
		return loginPath;
	}
	
	private String getLoginApiKey() {
		String loginApiKey = EnvironmentContext.getEnvValue("login-api-key");
		return loginApiKey;
	}

	private boolean checkAuth(String sessionId) {
		return StringUtils.isNotBlank(sessionId) && UserContext.isValid(sessionId);
	}

	@Override
	public void destroy() {

	}

}
