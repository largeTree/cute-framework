package com.qiuxs.cuteframework.view.interceptors;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.qiuxs.cuteframework.core.basic.utils.ListUtils;
import com.qiuxs.cuteframework.view.config.CuteViewConfiguration;
import com.qiuxs.cuteframework.web.interceptors.AbstractHandlerInterceptor;

/**
 * 视图拦截器
 * @author qiuxs
 *
 */
@Component
public class ViewInterceptor extends AbstractHandlerInterceptor {

	@Resource
	private CuteViewConfiguration viewConfiguration;

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public List<String> getPathPatterns() {
		return ListUtils.genList("/view/**");
	}

}
