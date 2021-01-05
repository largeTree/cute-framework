package com.qiuxs.cuteframework.web.controller;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qiuxs.cuteframework.web.WebConstants;
import com.qiuxs.cuteframework.web.controller.api.ApiConfig;
import com.qiuxs.cuteframework.web.controller.api.ApiConfigHolder;

@RestController
public class RestfulApiGatewayController extends AbstractApiGatewayController {
	
	private static final String REST_API_PREFIX = "/rest";

	@RequestMapping(value = REST_API_PREFIX + "/*", produces = WebConstants.DEFAULT_REQUEST_PRODUCES)
	public String dispatcher(
			@RequestHeader(name = WebConstants.REQ_H_COMPRESS_TYPE, required = false) String compressType,
			HttpServletRequest request, HttpServletResponse response)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		String uri = request.getRequestURI().replace(request.getContextPath(), "").replace(REST_API_PREFIX, "");

		// 获取apiConfig
		ApiConfig apiConfig = ApiConfigHolder.getApiConfigByUri(uri);

		String compressedResult = super.invokeApi(request, compressType, apiConfig);
		return compressedResult;
	}

}
