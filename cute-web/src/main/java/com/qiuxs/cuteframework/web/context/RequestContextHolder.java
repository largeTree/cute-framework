package com.qiuxs.cuteframework.web.context;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

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
		requestContext = new RequestContext(null);
		return requestContext;
	}

	public static void initRequestContext(ServletRequest request) {
		RequestContext rc = new RequestContext((HttpServletRequest) request);
		TLVariableHolder.setVariable(REQ_CTX_TL_KEY, rc);
	}

	public Map<String, String> getHeaders() {
		return getContext().getHeaders();
	}

	public String getHeader(String header) {
		return getContext().getHeader(header);
	}

	public Map<String, String> getCookies() {
		return getContext().getCookies();
	}

	public String getCookies(String key) {
		return getContext().getCookies(key);
	}

	public String getCliIp() {
		return getContext().getCliIp();
	}

	public String getCharacterEnchoding() {
		return getContext().getCharacterEnchoding();
	}

	public String getMethod() {
		return getContext().getMethod();
	}

	public String getProtocol() {
		return getContext().getProtocol();
	}

	public String getQueryString() {
		return getContext().getQueryString();
	}

	public Map<String, String> getQueryParams() {
		return getContext().getQueryParams();
	}

	public String getQueryParams(String key) {
		return getContext().getQueryParams(key);
	}

	public String getContentType() {
		return getContext().getContentType();
	}

	public String getScheme() {
		return getContext().getScheme();
	}

	public String getServerName() {
		return getContext().getServerName();
	}

	public int getServerPort() {
		return getContext().getServerPort();
	}

	public String getContextPath() {
		return getContext().getContextPath();
	}

}
