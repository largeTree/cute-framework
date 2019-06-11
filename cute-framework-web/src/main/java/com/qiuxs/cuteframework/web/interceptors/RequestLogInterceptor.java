package com.qiuxs.cuteframework.web.interceptors;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.core.basic.bean.Pair;
import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.cuteframework.core.context.TLVariableHolder;
import com.qiuxs.cuteframework.tech.task.AsyncTaskExecutor;
import com.qiuxs.cuteframework.tech.task.RunnableAsyncTask;
import com.qiuxs.cuteframework.web.WebConstants;
import com.qiuxs.cuteframework.web.log.entity.ApiRequestLog;
import com.qiuxs.cuteframework.web.log.service.IApiRequestLogService;
import com.qiuxs.cuteframework.web.utils.RequestUtils;

/**
 * 记录请求日志
 * @author qiuxs
 * 2019年5月30日 下午10:29:05
 */
// @Component
public class RequestLogInterceptor extends AbstractHandlerInterceptor {

	private static Logger log = LogManager.getLogger(RequestLogInterceptor.class);

	private static final String TL_KEY = "__tl_request_start_time";

	@Resource
	private IApiRequestLogService apiRequestLogService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		TLVariableHolder.setVariable(TL_KEY, new Date());
		return super.preHandle(request, response, handler);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		super.afterCompletion(request, response, handler, ex);
		this.saveRequestLog(request);
	}

	@Override
	public int getOrder() {
		// 优先级低于线程变量拦截器
		return AbstractHandlerInterceptor.PRIORITY_HIGH + 1;
	}

	/**
	 * 保存请求日志
	 * 
	 * 2019年5月30日 下午10:40:03
	 * @auther qiuxs
	 * @param request
	 */
	private void saveRequestLog(HttpServletRequest request) {
		Date startTime = TLVariableHolder.getVariable(TL_KEY);
		Date endTime = new Date();
		AsyncTaskExecutor.execute(new RunnableAsyncTask<Pair<Date, Date>>(new Pair<Date, Date>(startTime, endTime)) {
			@Override
			public void execute(Pair<Date, Date> preparParam) {
				String apiKey = request.getParameter(WebConstants.REQ_P_API_KEY);
				String reqIp = "";
				try {
					reqIp = RequestUtils.getRemoteAddr(request);
				} catch (Throwable e) {
					log.error("ext = " + e.getLocalizedMessage(), e);
				}
				String serverId = EnvironmentContext.getEnvContext().getServerId();
				String requestUrl = request.getRequestURL().toString();
				Date startTime = preparParam.getV1();
				Date endTime = preparParam.getV2();

				ApiRequestLog reqLog = new ApiRequestLog();
				reqLog.setApiKey(apiKey);
				reqLog.setServerId(serverId);
				reqLog.setReqIp(reqIp);
				reqLog.setReqUrl(requestUrl);
				reqLog.setReqStartTime(startTime);
				reqLog.setReqEndTime(endTime);
				RequestLogInterceptor.this.apiRequestLogService.save(reqLog);
			}
		}, false);
	}
}
