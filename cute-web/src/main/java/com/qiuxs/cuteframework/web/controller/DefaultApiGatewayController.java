package com.qiuxs.cuteframework.web.controller;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qiuxs.cuteframework.web.WebConstants;
import com.qiuxs.cuteframework.web.controller.api.ApiConfig;
import com.qiuxs.cuteframework.web.controller.api.ApiConfigHolder;

@RestController
public class DefaultApiGatewayController extends AbstractApiGatewayController {

	@RequestMapping(value = "/api.do", produces = WebConstants.DEFAULT_REQUEST_PRODUCES)
	public String dispatcher(@RequestParam(name = WebConstants.REQ_P_API_KEY, required = true) String apiKey,
	        @RequestHeader(name = WebConstants.REQ_H_COMPRESS_TYPE, required = false) String compressType,
	        HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// 获取apiConfig
		ApiConfig apiConfig = ApiConfigHolder.getApiConfig(apiKey);

		String actionResult = super.invokeApi(request, compressType, apiConfig);
		
		String compressedResult = super.compressResult(actionResult, compressType);
		return compressedResult;
	}

}
