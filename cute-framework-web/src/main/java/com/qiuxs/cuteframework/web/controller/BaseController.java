package com.qiuxs.cuteframework.web.controller;

import java.net.UnknownHostException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.web.WebConstants.HttpHeader;
import com.qiuxs.cuteframework.web.action.ActionConstants;
import com.qiuxs.cuteframework.web.log.entity.RequestLog;
import com.qiuxs.cuteframework.web.utils.RequestUtils;

/**
 * 控制器基类
 * 
 * @author qiuxs
 *
 */
public abstract class BaseController {

	protected static Logger log = LogManager.getLogger(BaseController.class);
	
	@ExceptionHandler
	public String handlerException(Throwable e, HttpServletResponse response) {
		response.addIntHeader(HttpHeader.STATUS.value(), RequestLog.FAILED);
		JSONObject error = ExceptionUtils.logError(log, e);
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
		String ctxPath = request.getContextPath();
		String requestUrl = request.getRequestURL().toString();
		String hostPort = StringUtils.substringBefore(requestUrl, ctxPath + "/");

		requestUrl = hostPort + ctxPath;
		params.put(ActionConstants.REQUEST_URL, requestUrl);
		params.put(ActionConstants.REQUEST_SCHEME, request.getScheme());
		
		hostPort = StringUtils.substringAfter(hostPort, "://");
		String[] h_p = hostPort.split(":");
		if (h_p.length > 0) {
			params.put(ActionConstants.SERVER_HOST, h_p[0]);
		}
		if (h_p.length > 1) {
			params.put(ActionConstants.REQUEST_PORT, h_p[1]);
		}
	}
}
