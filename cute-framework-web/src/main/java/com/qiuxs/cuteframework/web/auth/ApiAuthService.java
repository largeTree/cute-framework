package com.qiuxs.cuteframework.web.auth;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.web.controller.api.ApiConfig;

/**
 * 接口认证服务
 * @author qiuxs
 *
 */
@Service
public class ApiAuthService {

	/**
	 * 会话验证
	 * @param apiConfig
	 * @param params
	 */
	public void checkAndSetSession(ApiConfig apiConfig, Map<String, String> params) {
		params.get("");
	}
	
	/**
	 * 认证检查
	 * @param apiConfig
	 * @param params
	 */
	public void authCheck(ApiConfig apiConfig, Map<String, String> params) {
		
	}
	
}
