package com.qiuxs.cuteframework.web.controller;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.basic.Constants;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.cuteframework.tech.log.LogConstant;
import com.qiuxs.cuteframework.tech.log.LogUtils;
import com.qiuxs.cuteframework.tech.task.AsyncTaskExecutor;
import com.qiuxs.cuteframework.tech.task.RunnableAsyncTask;
import com.qiuxs.cuteframework.web.action.ActionConstants;
import com.qiuxs.cuteframework.web.log.entity.ApiRequestLog;
import com.qiuxs.cuteframework.web.log.service.IApiRequestLogService;
import com.qiuxs.cuteframework.web.utils.RequestUtils;

/**
 * 控制器基类
 * 
 * @author qiuxs
 *
 */
public abstract class BaseController {

	protected static Logger log = LogManager.getLogger(BaseController.class);
	
	@Resource
	private IApiRequestLogService apiRequestLogService; 
	
	/**
	 * 准备请求日志
	 * 
	 * 2019年6月11日 下午8:31:26
	 * @auther qiuxs
	 * @param apiKey
	 * @param ip
	 * @param reqUrl
	 * @param startTime
	 */
	protected void prepareRequestLog(String apiKey, String reqUrl, long startTime) {
		LogUtils.putMDC(LogConstant.MDC_KEY_APIKEY, apiKey);
		LogUtils.putMDC(LogConstant.MDC_KEY_REQURL, reqUrl);
		LogUtils.putMDC(LogConstant.MDC_KEY_START_TIME, String.valueOf(startTime));
		LogUtils.putMDC(LogConstant.MDC_KEY_FLAG, Constants.TRUE_STR);
	}
	
	/**
	 * 记录请求日志
	 * 
	 * 2019年6月11日 下午8:14:52
	 * @auther qiuxs
	 */
	protected void logRequest(boolean successFlag) {
		Map<String, String> contextMap = LogUtils.getContextMap();
		String apiKey = contextMap.get(LogConstant.MDC_KEY_APIKEY);
		String reqUrl = contextMap.get(LogConstant.MDC_KEY_REQURL);
		String startTime = contextMap.get(LogConstant.MDC_KEY_START_TIME);
		String ip = contextMap.get(LogConstant.COLUMN_IP);
		Date endDate = new Date();
		AsyncTaskExecutor.execute(new RunnableAsyncTask<Object>(null) {
			@Override
			public void execute(Object preparParam) {
				ApiRequestLog reqLog = new ApiRequestLog();
				reqLog.setApiKey(apiKey);
				reqLog.setServerId(EnvironmentContext.getEnvContext().getServerId());
				reqLog.setReqIp(ip);
				reqLog.setReqUrl(reqUrl);
				reqLog.setReqStartTime(new Date(Long.parseLong(startTime)));
				reqLog.setReqEndTime(endDate);
				reqLog.setStatus(successFlag ? ApiRequestLog.SUCCESS : ApiRequestLog.FAILED);
				BaseController.this.apiRequestLogService.save(reqLog);
			}
		}, true);
	}

	@ExceptionHandler
	public String handlerException(Throwable e) {
		JSONObject error = ExceptionUtils.logError(log, e);
		// 记录日志
		this.logRequest(false);
		return error.toJSONString();
	}

	/**
	 * 填充客户端信息
	 * 
	 * @param params
	 * @param request
	 */
	protected void fillClientInfo(Map<String, String> params, HttpServletRequest request) {
		try {
			String remoteAddr = RequestUtils.getRemoteAddr(request);
			params.put(ActionConstants.CLIENT_IP, remoteAddr);
		} catch (UnknownHostException e) {
			log.error("ext=" + e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 填充服务端信息
	 * 
	 * @param params
	 * @param request
	 */
	protected void fillServerInfo(Map<String, String> params, HttpServletRequest request) {
		String remoteHost = request.getRemoteHost();
		params.put(ActionConstants.SERVER_HOST, remoteHost);
		int port = request.getLocalPort();
		params.put(ActionConstants.REQUEST_PORT, String.valueOf(port));
		StringBuilder requestUrl = new StringBuilder();
		requestUrl.append(request.getScheme()).append("://").append(remoteHost).append(":").append(port).append(request.getContextPath());
		params.put(ActionConstants.REQUEST_URL, requestUrl.toString());
	}
}
