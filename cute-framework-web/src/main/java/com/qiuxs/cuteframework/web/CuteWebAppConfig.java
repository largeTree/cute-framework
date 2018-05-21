package com.qiuxs.cuteframework.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;
import com.qiuxs.cuteframework.web.interceptors.AbstractHandlerInterceptor;

/**
 *
 * 功能描述: Web配置工具类<br/>
 * 新增原因: 1.支持web拦截器<br/>
 * 新增日期: 2018年4月15日 下午8:53:52 <br/>
 * 
 * @author qiuxs
 * @version 1.0.0
 */
@Component
public class CuteWebAppConfig extends WebMvcConfigurationSupport {

	@Override
	protected void addInterceptors(InterceptorRegistry registry) {
		// 自定义拦截器
		Optional<List<AbstractHandlerInterceptor>> customerHandlerInterceptors = getCustomerHandlerInterceptors();
		customerHandlerInterceptors.ifPresent((interceptors) -> {
			interceptors.forEach(interceptor -> {
				InterceptorRegistration interceptorReg = registry.addInterceptor(interceptor);
				Optional<List<String>> pathPatterns = interceptor.getPathPatterns();
				pathPatterns.ifPresent(paths -> {
					interceptorReg.addPathPatterns(paths);
				});
				Optional<List<String>> excludes = interceptor.getExcludes();
				excludes.ifPresent(paths -> {
					interceptorReg.excludePathPatterns(paths);
				});
				interceptorReg.order(interceptor.getOrder());
			});
		});
	}

	/**
	 * 根据抽象类型获取所有的自定义拦截器
	 * 
	 * @return
	 */
	private Optional<List<AbstractHandlerInterceptor>> getCustomerHandlerInterceptors() {
		String[] handlerInterceptorNames = ApplicationContextHolder.getBeanNamesForType(AbstractHandlerInterceptor.class);
		List<AbstractHandlerInterceptor> handlerInterceptors = null;
		int size = handlerInterceptorNames.length;
		if (size > 0) {
			handlerInterceptors = new ArrayList<>(size);
			for (String name : handlerInterceptorNames) {
				handlerInterceptors.add((AbstractHandlerInterceptor) ApplicationContextHolder.getBean(name));
			}
		}
		return Optional.ofNullable(handlerInterceptors);
	}

}
