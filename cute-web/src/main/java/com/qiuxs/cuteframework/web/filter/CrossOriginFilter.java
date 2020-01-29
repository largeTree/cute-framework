package com.qiuxs.cuteframework.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * 功能描述: 跨域filter<br/>  
 * 新增原因: 支持前后端分离且使用不同域名<br/>  
 * 新增日期: 2020年1月29日 下午8:10:56 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class CrossOriginFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse resp = (HttpServletResponse) response;
		resp.addHeader("Access-Control-Allow-Origin", "*");
		resp.addHeader("Access-Control-Allow-Headers", "User-Agent,Referer,Host,Connection,Access-Control-Request-Method,Access-Control-Request-Headers,Accept-Language,Accept-Encoding,Accept,Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With");
		resp.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}

}