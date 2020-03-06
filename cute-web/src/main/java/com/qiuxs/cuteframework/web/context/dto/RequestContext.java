package com.qiuxs.cuteframework.web.context.dto;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.web.utils.RequestUtils;

/**  
 * 功能描述: 缓存请求信息<br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年3月5日 下午4:14:56 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class RequestContext implements Serializable {

	private static final Logger log = LogManager.getLogger(RequestContext.class);

	private static final long serialVersionUID = -5782844797748465133L;

	/** 是否有效 */
	private final boolean valid;
	/** 请求头 */
	private final Map<String, String> headers;
	/** cookies */
	private final Map<String, String> cookies;
	/** 客户端IP */
	private final String cliIp;
	/** 请求编码 */
	private final String characterEnchoding;
	/** 请求方法 */
	private final String method;
	/** 请求协议及版本如 HTTP/1.1 */
	private final String protocol;
	/** 查询字符串 */
	private final String queryString;
	/** queryString解析成的map */
	private final Map<String, String> queryParams;
	/** 请求体类型 */
	private final String contentType;
	/** 请求协议，http/https/ftp */
	private final String scheme;
	/** 服务器名 */
	private final String serverName;
	/** 服务器端口 */
	private final int serverPort;
	/** 上下文地址 */
	private final String contextPath;

	public RequestContext(HttpServletRequest request) {
		if (request == null) {
			this.headers = Collections.emptyMap();
			this.cookies = Collections.emptyMap();
			this.queryParams = Collections.emptyMap();
			this.method = null;
			this.queryString = null;
			this.valid = false; // 无效的
			this.serverPort = 0;
			this.contentType = null;
			this.scheme = null;
			this.protocol = null;
			this.serverName = null;
			this.cliIp = "";
			this.contextPath = "";
			this.characterEnchoding = "";
			return;
		}
		// 请求头
		Enumeration<String> headerNames = request.getHeaderNames();
		this.headers = new HashMap<>();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			headers.put(headerName, request.getHeader(headerName));
		}
		
		String _cliIp = null;
		// 客户端ip
		try {
			_cliIp = RequestUtils.getRemoteAddr(request);
		} catch (UnknownHostException e) {
			log.error("getCliIp Failed, ext = " + e.getLocalizedMessage(), e);
		}
		this.cliIp = _cliIp;

		// 请求编码
		this.characterEnchoding = request.getCharacterEncoding();

		// cookies
		this.cookies = RequestUtils.getCookies(request);

		// queryString
		this.queryString = request.getQueryString();
		if (StringUtils.isNotBlank(queryString)) {
			this.queryParams = new HashMap<String, String>();
			String[] k_vs = this.queryString.split("&");
			for (String k_v : k_vs) {
				String[] kv = k_v.split("=");
				this.queryParams.put(kv[0], kv[1]);
			}
		} else {
			this.queryParams = Collections.emptyMap();
		}

		this.method = request.getMethod();
		this.protocol = request.getProtocol();
		this.contentType = request.getContentType();
		this.scheme = request.getScheme();
		this.serverName = request.getServerName();
		this.serverPort = request.getServerPort();
		this.contextPath = request.getContextPath();
		this.valid = true; // 置为有效
	}

	public boolean isValid() {
		return this.valid;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public String getHeader(String header) {
		return this.headers.get(header);
	}

	public Map<String, String> getCookies() {
		return cookies;
	}

	public String getCookies(String key) {
		return this.cookies.get(key);
	}

	public String getCliIp() {
		return cliIp;
	}

	public String getCharacterEnchoding() {
		return characterEnchoding;
	}

	public String getMethod() {
		return method;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getQueryString() {
		return queryString;
	}

	public Map<String, String> getQueryParams() {
		return queryParams;
	}

	public String getQueryParams(String key) {
		return this.queryParams.get(key);
	}

	public String getContentType() {
		return contentType;
	}

	public String getScheme() {
		return scheme;
	}

	public String getServerName() {
		return serverName;
	}

	public int getServerPort() {
		return serverPort;
	}

	public String getContextPath() {
		return contextPath;
	}

}