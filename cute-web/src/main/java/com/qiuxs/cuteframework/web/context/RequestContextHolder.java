package com.qiuxs.cuteframework.web.context;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.context.TLVariableHolder;
import com.qiuxs.cuteframework.web.context.dto.RequestContext;

/**
 * 
 * 功能描述: 请求信息保持对象<br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年3月5日 下午4:58:35 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class RequestContextHolder {
	
	private static final String REQ_CTX_TL_KEY = "tl_req_ctx";

	public static RequestContext getContext() {
		RequestContext requestContext = TLVariableHolder.getVariable(REQ_CTX_TL_KEY);
		if (requestContext == null) {
			requestContext = new RequestContext(null);
		}
		return requestContext;
	}

	public static void initRequestContext(ServletRequest request) {
		RequestContext rc = new RequestContext((HttpServletRequest) request);
		TLVariableHolder.setVariable(REQ_CTX_TL_KEY, rc);
	}

	public static Map<String, String> getHeaders() {
		return getContext().getHeaders();
	}

	public static String getHeader(String header) {
		return getContext().getHeader(header);
	}

	public static Map<String, String> getCookies() {
		return getContext().getCookies();
	}

	public static String getCookies(String key) {
		return getContext().getCookies(key);
	}

	public static String getCliIp() {
		return getContext().getCliIp();
	}

	public static String getCharacterEnchoding() {
		return getContext().getCharacterEnchoding();
	}

	public static String getMethod() {
		return getContext().getMethod();
	}

	public static String getProtocol() {
		return getContext().getProtocol();
	}

	public static String getQueryString() {
		return getContext().getQueryString();
	}

	public static Map<String, String> getQueryParams() {
		return getContext().getQueryParams();
	}

	public static String getQueryParams(String key) {
		return getContext().getQueryParams(key);
	}

	public static String getContentType() {
		return getContext().getContentType();
	}

	public static String getScheme() {
		return getContext().getScheme();
	}

	public static String getServerName() {
		return getContext().getServerName();
	}
	
	public static String getServerHost() {
		return getContext().getServerHost();
	}
	
	public static int getServerPort() {
		return getContext().getServerPort();
	}

	public static String getContextPath() {
		return getContext().getContextPath();
	}

	public static String getRequestUrl() {
		return getContext().getRequestUrl();
	}

	public static String getReferer() {
		String referer = getHeader("Referer");
		if (StringUtils.isBlank(referer)) {
			referer = getHeader("referer");
		}
		return referer;
	}

}
