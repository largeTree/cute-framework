package com.qiuxs.cuteframework.web.filter;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;
import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.cuteframework.core.persistent.util.IDGenerateUtil;
import com.qiuxs.cuteframework.tech.log.LogConstant;
import com.qiuxs.cuteframework.tech.log.LogUtils;
import com.qiuxs.cuteframework.tech.microsvc.log.ApiLogUtils;
import com.qiuxs.cuteframework.web.WebConstants;
import com.qiuxs.cuteframework.web.log.entity.RequestLog;
import com.qiuxs.cuteframework.web.log.service.IRequestLogService;
import com.qiuxs.cuteframework.web.utils.RequestUtils;

/**
 * 功能描述: 请求开始时设置日志全局变量<br/>
 * 新增原因: TODO<br/>
 * 新增日期: 2018年4月23日 下午9:59:38 <br/>
 * 
 * @author qiuxs
 * @version 1.0.0
 */
/*@WebFilter(filterName = "logFilter", urlPatterns = { 
		"/*"
})*/
public class LogFilter implements Filter {

	private static Logger log = LogManager.getLogger(LogFilter.class);

	private IRequestLogService requestLogService;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// 保证filter能够正常执行，每个操作都独立try/catch
		RequestLog reqLog = new RequestLog();
		try {
			// 开始时间
			reqLog.setReqStartTime(new Date());
			HttpServletRequest req = (HttpServletRequest) request;
			reqLog.setReqUrl(req.getRequestURL().toString());
			Map<String, String> kvMap = RequestUtils.splitQueryString(req.getQueryString());
			String apiKey = kvMap.get(WebConstants.REQ_P_API_KEY);
			// apiKey
			reqLog.setApiKey(apiKey);
			String ip = RequestUtils.getRemoteAddr((HttpServletRequest) request);
			// 来源IP
			reqLog.setReqIp(ip);
			if (apiKey != null) {
				LogUtils.putMDC(LogConstant.MDC_KEY_APIKEY, apiKey);
			}
			// 全局日志识别号
			Long globalId = IDGenerateUtil.getNextLongId(LogConstant.GLOBAL_ID_SEQ);
			reqLog.setGlobalId(globalId);
			
			// 初始化apiLog
			ApiLogUtils.initApiLog("gateway" + apiKey, LogConstant.APP_CLI, EnvironmentContext.getAppName(), globalId, ip, "");
		} catch (Throwable e) {
			log.error("put logMDC ext=" + e.getLocalizedMessage(), e);
		}

		Throwable chainEx = null;

		int status = RequestLog.FAILED;
		try {
			chain.doFilter(request, response);
			HttpServletResponse resp = (HttpServletResponse) response;
			status = resp.getStatus();
		} catch (Exception e) {
			chainEx = e;
			log.error("chain.doFilter ext=" + e.getLocalizedMessage(), e);
		}

		try {
			// 状态
			reqLog.setStatus(status);
			// 结束时间
			reqLog.setReqEndTime(new Date());
			reqLog.setServerId(EnvironmentContext.getServerId());
			this.getRequestLogService().save(reqLog);
		} catch (Throwable e) {
			log.error("save RequestLog ext=" + e.getLocalizedMessage(), e);
		} finally {
			LogUtils.clearMDC();
		}

		// 如果执行chain的过程中发生异常那么直接抛出
		if (chainEx != null) {
			if (chainEx instanceof IOException) {
				throw (IOException) chainEx;
			} else if (chainEx instanceof ServletException) {
				throw (ServletException) chainEx;
			} else {
				throw ExceptionUtils.unchecked(chainEx);
			}
		}
	}

	@Override
	public void destroy() {
	}

	private IRequestLogService getRequestLogService() {
		if (this.requestLogService == null) {
			requestLogService = ApplicationContextHolder.getBean(IRequestLogService.class);
		}
		return requestLogService;
	}

}
