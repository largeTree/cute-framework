package com.qiuxs.cuteframework.web.interceptors;

import java.util.List;
import java.util.Optional;

import org.springframework.web.servlet.HandlerInterceptor;

import com.qiuxs.cuteframework.core.basic.utils.ListUtils;

public abstract class AbstractHandlerInterceptor implements HandlerInterceptor {

	/** 高优先级 */
	public static final int PRIORITY_HIGH = 10;
	/** 中等优先级 */
	public static final int PRIORITY_MIDDLE = 20;
	/** 低优先级 */
	public static final int PRIORITY_LOW = 30;

	/**
	 * 获取优先级
	 *  
	 * @author qiuxs  
	 * @return
	 * 		拦截器优先级 默认低优先级
	 */
	public int getOrder() {
		return PRIORITY_LOW;
	}

	/**
	 * 获取需要拦截的路径集合，默认拦截所有
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public Optional<List<String>> getPathPatterns() {
		return ListUtils.genList("/**");
	}

	/**
	 * 获取忽略的路径集合
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public Optional<List<String>> getExcludes() {
		return Optional.empty();
	}

}
