package com.qiuxs.cuteframework.web.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qiuxs.cuteframework.core.basic.utils.JsonUtils;
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

	@Resource
	private ApiAuthService apiAuthService;

	@RequestMapping(value = "/api.do", produces = WebConstants.DEFAULT_REQUEST_PRODUCES)
	public String dispatcher(@RequestParam(name = WebConstants.REQ_P_API_KEY, required = true) String apiKey,
	        @RequestHeader(name = WebConstants.REQ_H_COMPRESS_TYPE, required = false) String compressType,
	        HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		long start = System.currentTimeMillis();
		
		// 获取apiConfig
		ApiConfig apiConfig = ApiConfigHolder.getApiConfig(apiKey);

		// 获取所有参数
		ReqParam params = RequestUtils.getRequestParams(request, compressType);
		super.fillClientInfo(params, request);
		super.fillServerInfo(params, request);
		
		log.info("request -> params = " + String.valueOf(params));

		// 检查sessionId
		this.apiAuthService.checkAndSetSession(apiConfig, params);

		// 授权检查
		this.apiAuthService.authCheck(apiConfig, params);

		// 调用Action
		String actionResult = this.doAction(apiConfig, params);

		// 根据压缩类型返回结果
		String compressedResult = this.compressResult(actionResult, compressType);
		
		log.info("response -> costMs = " + (System.currentTimeMillis() - start));
		
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
