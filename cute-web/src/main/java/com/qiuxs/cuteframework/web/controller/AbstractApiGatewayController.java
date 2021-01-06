package com.qiuxs.cuteframework.web.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StopWatch;

import com.qiuxs.cuteframework.core.basic.utils.JsonUtils;
import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.cuteframework.core.context.StopWatchContext;
import com.qiuxs.cuteframework.tech.log.LogConstant;
import com.qiuxs.cuteframework.tech.log.LogUtils;
import com.qiuxs.cuteframework.web.action.ActionConstants;
import com.qiuxs.cuteframework.web.action.IAction;
import com.qiuxs.cuteframework.web.auth.ApiAuthService;
import com.qiuxs.cuteframework.web.bean.ActionResult;
import com.qiuxs.cuteframework.web.bean.ReqParam;
import com.qiuxs.cuteframework.web.controller.api.ApiConfig;
import com.qiuxs.cuteframework.web.utils.RequestUtils;

public class AbstractApiGatewayController extends BaseController {
	
	private ApiAuthService apiAuthService;
	
	protected String invokeApi(HttpServletRequest request, String compressType, ApiConfig apiConfig) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// 总耗时
		StopWatch sw = new StopWatch();
		sw.start();
		
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
		
		StopWatch csw = StopWatchContext.get(false);
		if (csw != null) {
			if (log.isDebugEnabled()) {
				log.debug(csw.prettyPrint());
			} else if (EnvironmentContext.isDebug()) {
				log.info(csw.prettyPrint());
			}
		}
		
		String logRes = actionResult;
		if (logRes.length() > 10000) {
			logRes = logRes.substring(0, 10000);
		}
		
		// 压缩结果
		String compressedResult = this.compressResult(actionResult, compressType);
		
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
	protected ApiAuthService getApiAuthService() {
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
	protected String compressResult(String actionResult, String compressType) {
		if (compressType == null) {
			return actionResult;
		}
		// TODO 支持压缩结果数据
		return actionResult;
	}

}
