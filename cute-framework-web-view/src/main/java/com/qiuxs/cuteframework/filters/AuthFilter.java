package com.qiuxs.cuteframework.filters;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.web.utils.RequestUtils;

@WebFilter(filterName = "authFilter", urlPatterns = {
        "/view/*"
})
public class AuthFilter implements Filter {

	private static Logger log = LogManager.getLogger(AuthFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		boolean authFlag = false;
		try {
			HttpServletRequest req = (HttpServletRequest) request;
			Map<String, String> cookiesMap = RequestUtils.getCookies(req);
			String auth = cookiesMap.get("auth");
			if (StringUtils.isNotBlank(auth) && this.checkAuth(auth)) {
				authFlag = true;
			}
		} catch (Exception e) {
			log.error("Auth Request Failed ext = " + e.getLocalizedMessage(), e);
		}

		if (!authFlag) {
			HttpServletResponse resp = (HttpServletResponse) response;
			resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		} else {
			chain.doFilter(request, response);
		}

	}

	private boolean checkAuth(String auth) {
		return StringUtils.isNotBlank(auth);
	}

	@Override
	public void destroy() {

	}

}
