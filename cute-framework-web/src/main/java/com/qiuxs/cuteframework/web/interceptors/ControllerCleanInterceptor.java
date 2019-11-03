package com.qiuxs.cuteframework.web.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.core.context.TLVariableHolder;
import com.qiuxs.cuteframework.tech.mybatis.MyBatisManager;

/**
 * 功能描述: 请求开始和结束时清理一下线程变量<br/>
 * 新增原因: TODO<br/>
 * 新增日期: 2018年4月23日 下午9:59:38 <br/>
 * 
 * @author qiuxs
 * @version 1.0.0
 */
@Component
public class ControllerCleanInterceptor extends AbstractHandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	        throws Exception {
		TLVariableHolder.clear();
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		MyBatisManager.closeSession();
		TLVariableHolder.clear();
	}
	
	@Override
	public int getOrder() {
		// 优先级设置为高
		return AbstractHandlerInterceptor.PRIORITY_HIGH;
	}

}
