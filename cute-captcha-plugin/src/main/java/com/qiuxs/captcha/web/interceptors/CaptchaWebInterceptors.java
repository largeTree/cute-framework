package com.qiuxs.captcha.web.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.qiuxs.captcha.context.CaptchaWebContext;
import com.qiuxs.cuteframework.web.interceptors.AbstractHandlerInterceptor;
import com.qiuxs.cuteframework.web.utils.RequestUtils;

/**
 * 验证码插件web请求拦截器
 * 1.设置请求IP地址
 * @author qiuxs
 * 2019年4月2日 下午10:23:25
 */
@Component
public class CaptchaWebInterceptors extends AbstractHandlerInterceptor {

	private static final Logger log = LogManager.getLogger(CaptchaWebInterceptors.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		try {
			String ip = RequestUtils.getRemoteAddr(request);
			CaptchaWebContext.setVariable(CaptchaWebContext.IP, ip);
		} catch (Throwable e) {
			log.error("Set CaptchaWebContext Failed , ext = " + e.getLocalizedMessage(), e);
		}
		return super.preHandle(request, response, handler);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		try {
			CaptchaWebContext.clear();
		} catch (Throwable e) {
			log.error("Clear CaptchaWebContext Failed , ext = " + e.getLocalizedMessage(), e);
		}
		super.afterCompletion(request, response, handler, ex);
	}

}
