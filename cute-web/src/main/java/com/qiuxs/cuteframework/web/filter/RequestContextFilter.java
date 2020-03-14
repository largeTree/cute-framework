package com.qiuxs.cuteframework.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qiuxs.cuteframework.web.context.RequestContextHolder;

/**
 * 获取并缓存请求信息
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年3月5日 下午4:05:12 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class RequestContextFilter implements Filter {

	private static Logger log = LogManager.getLogger(RequestContextFilter.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			RequestContextHolder.initRequestContext(request);
		} catch (Throwable e) {
			log.error("init request context failed, ext = " + e.getLocalizedMessage(), e);
		}
		
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

}
