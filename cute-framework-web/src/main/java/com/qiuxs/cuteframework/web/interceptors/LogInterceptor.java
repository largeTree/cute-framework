package com.qiuxs.cuteframework.web.interceptors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.qiuxs.cuteframework.tech.log.LogConstant;
import com.qiuxs.cuteframework.tech.log.LogGlobalIdGenerater;
import com.qiuxs.cuteframework.tech.log.LogUtils;
import com.qiuxs.cuteframework.web.utils.RequestUtils;

/**
 * 功能描述: 请求开始时设置日志全局变量<br/>
 * 新增原因: TODO<br/>
 * 新增日期: 2018年4月23日 下午9:59:38 <br/>
 * 
 * @author qiuxs
 * @version 1.0.0
 */
@Component
public class LogInterceptor extends AbstractHandlerInterceptor {

	private static Logger log = LogManager.getLogger(LogInterceptor.class);

	@Resource
	private LogGlobalIdGenerater globalIdGenerater;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		try {
			String ip = RequestUtils.getRemoteAddr(request);
			LogUtils.putMDC("ip", ip);
			LogUtils.putMDC(LogConstant.COLUMN_GLOBALID, String.valueOf(globalIdGenerater.nextId()));
		} catch (Throwable e) {
			log.error("ext=" + e.getLocalizedMessage(), e);
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		LogUtils.clearMDC();
	}

	@Override
	public int getOrder() {
		return 1;
	}

}
