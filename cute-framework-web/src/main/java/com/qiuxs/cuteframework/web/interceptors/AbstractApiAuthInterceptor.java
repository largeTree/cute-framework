package com.qiuxs.cuteframework.web.interceptors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qiuxs.cuteframework.core.basic.bean.UserLite;
import com.qiuxs.cuteframework.core.basic.utils.ListUtils;
import com.qiuxs.cuteframework.web.WebConstants;
import com.qiuxs.cuteframework.web.utils.RequestUtils;

/***
 * 
 * 功能描述: 接口认证基类<br/>
 * 新增原因: TODO<br/>
 * 新增日期: 2018年4月23日 下午11:03:09 <br/>
 * 
 * @author qiuxs
 * @version 1.0.0
 */
public abstract class AbstractApiAuthInterceptor extends AbstractHandlerInterceptor {

	public static final String DEFAULT_API_PREFIX = "/api";
	public static final String DEFAULT_LOGIN_API_PATH = DEFAULT_API_PREFIX + "/login";
	private static final List<String> IGNORE_APIAUTH_PATH = new ArrayList<>();
	static {
		IGNORE_APIAUTH_PATH.add(WebConstants.SYS_CONTROLLER_PREFIX + "/**");
	}

	@Override
	public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	        throws Exception {
		this.getUserLite(request, RequestUtils.getRequestParams(request));
		return super.preHandle(request, response, handler);
	}

	@Override
	public final int getOrder() {
		return AbstractHandlerInterceptor.PRIORITY_MIDDLE;
	}

	/**
	 * 获取API前缀，仅拦截以此前缀开头的请求
	 * @return
	 */
	protected String getApiPrefix() {
		return DEFAULT_API_PREFIX;
	}

	@Override
	public Optional<List<String>> getPathPatterns() {
		return ListUtils.genList(DEFAULT_API_PREFIX + "/**");
	}

	/**
	 * 获取登陆api地址，用于对登陆接口忽略权限及会话验证
	 * 
	 * @author qiuxs
	 * @return
	 */
	protected String getLoginApiPath() {
		return DEFAULT_LOGIN_API_PATH;
	}

	@Override
	public final Optional<List<String>> getExcludes() {
		IGNORE_APIAUTH_PATH.add(getLoginApiPath());
		return Optional.ofNullable(IGNORE_APIAUTH_PATH);
	}

	protected abstract UserLite getUserLite(HttpServletRequest request, Map<String, String> params);
}
