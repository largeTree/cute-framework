package com.qiuxs.cuteframework.web.providers;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.web.auth.ApiAuthService;

@Component
public class DefaultWebConditionalBeanProvider {

	/**
	 * 默认授权校验服务
	 *  
	 * @author qiuxs  
	 * @return
	 */
	@ConditionalOnMissingBean(ApiAuthService.class)
	@Bean
	public ApiAuthService defaultApiAuthService() {
		return new ApiAuthService();
	}

}
