package com.qiuxs.cuteframework.web.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qiuxs.cuteframework.core.basic.utils.JsonUtils;
import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.cuteframework.core.context.StopWatchContext;
import com.qiuxs.cuteframework.tech.log.LogConstant;
import com.qiuxs.cuteframework.tech.log.LogUtils;
import com.qiuxs.cuteframework.web.WebConstants;
import com.qiuxs.cuteframework.web.action.ActionConstants;
import com.qiuxs.cuteframework.web.action.IAction;
import com.qiuxs.cuteframework.web.auth.ApiAuthService;
import com.qiuxs.cuteframework.web.bean.ActionResult;
import com.qiuxs.cuteframework.web.bean.ReqParam;
import com.qiuxs.cuteframework.web.controller.api.ApiConfig;
import com.qiuxs.cuteframework.web.controller.api.ApiConfigHolder;
import com.qiuxs.cuteframework.web.utils.RequestUtils;

@RestController
public class DefaultApiGatewayController extends BaseController {
	
	private static final Logger log = LogManager.getLogger(DefaultApiGatewayController.class);

	private ApiAuthService apiAuthService;

	@RequestMapping(value = "/api.do", produces = WebConstants.DEFAULT_REQUEST_PRODUCES)
	public String dispatcher(@RequestParam(name = WebConstants.REQ_P_API_KEY, required = true) String apiKey,
	        @RequestHeader(name = WebConstants.REQ_H_COMPRESS_TYPE, required = false) String compressType,
	        HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// 总耗时
		StopWatch sw = new StopWatch();
		sw.start();
		
		// 子任务耗时情况统计
		StopWatchContext.init();
		
		// 获取apiConfig
		ApiConfig apiConfig = ApiConfigHolder.getApiConfig(apiKey);

		// 获取所有参数
		ReqParam params = RequestUtils.getRequestParams(request, compressType);
		super.fillClientInfo(params, request);
		super.fillServerInfo(params, request);
		
		log.info("request -> params = " + String.valueOf(params));

		// 检查sessionId
		this.getApiAuthService().checkAndSetSession(apiConfig, params);

		// 授权检查
		this.getApiAuthService().authCheck(apiConfig, params);

		// 调用Action
		String actionResult = this.doAction(apiConfig, params);
		
		// 根据压缩类型返回结果
		String compressedResult = this.compressResult(actionResult, compressType);
		
		String logRes = actionResult;
		if (logRes.length() > 10000) {
			logRes = logRes.substring(0, 10000);
		}
		
		if (log.isDebugEnabled()) {
			StopWatch csw = StopWatchContext.get(false);
			if (csw != null) {
				log.debug(csw.prettyPrint());
			}
		} else if (EnvironmentContext.isDebug()) {
			StopWatch csw = StopWatchContext.get(false);
			if (csw != null) {
				log.info(StopWatchContext.get(false).prettyPrint());
			}
		}
		
		sw.stop();
		
		log.info("response -> costMs = " + sw.getTotalTimeMillis() + ", res = " + logRes);
		return compressedResult;
	}

	/**
	 * 调用Action方法
	 * @param apiConfig
	 * @param params
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public String doAction(ApiConfig apiConfig, ReqParam params) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		IAction action = apiConfig.getAction();
		Method method = apiConfig.getMethodObj();
		int paramCount = apiConfig.getParamCount();
		
		// action返回结果
		ActionResult actionResult;
		if (paramCount > 0) {
			// 构造参数
			Object[] args = new Object[apiConfig.getParamCount()];
			args[0] = params;
			if (args.length == 2) {
				args[1] = params.get(ActionConstants.PARAM_JSONPARAM);
			}
			// 调用action方法
			actionResult = (ActionResult) method.invoke(action, args);
		} else {
			// 调用无参Action方法
			actionResult = (ActionResult) method.invoke(action);
		}

		// 默认返回成功
		if (actionResult == null) {
			actionResult = ActionResult.SUCCESS_INSTANCE;
		}
		
		String globalId = LogUtils.getContextMap().get(LogConstant.COLUMN_GLOBALID);
		if (globalId != null) {
			actionResult.setGlobalId(Long.parseLong(globalId));
		}
		
		// 转为json
		return JsonUtils.toJSONString(actionResult);
	}
	
	/**
	 * 获取api授权服务
	 *  
	 * @author qiuxs  
	 * @return
	 */
	private ApiAuthService getApiAuthService() {
		// 为空的情况下 初始化一个
		if (this.apiAuthService == null) {
			this.apiAuthService = ApiAuthService.getApiAuthService();
		}
		return this.apiAuthService;
	}

	/**
	 * 压缩结果
	 * @param actionResult
	 * @param compressType
	 * @return
	 */
	private String compressResult(String actionResult, String compressType) {
		if (compressType == null) {
			return actionResult;
		}
		// TODO 支持压缩结果数据
		return actionResult;
	}

}
