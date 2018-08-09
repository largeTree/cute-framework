package com.qiuxs.cuteframework.web.controller.handler;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qiuxs.cuteframework.core.basic.utils.ReflectUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.web.annotation.Api;
import com.qiuxs.cuteframework.web.controller.BaseController;
import com.qiuxs.cuteframework.web.controller.api.ApiConfig;

public class HandlerMappinHolder {

	private static Logger log = LogManager.getLogger(HandlerMappinHolder.class);

	/** Api配置缓存 */
	private static Map<Class<? extends BaseController>, Map<String, ApiConfig>> MAP_API_CONFIG = new ConcurrentHashMap<>();
	
	/** 类对应的前缀缓存 */
	private static Map<Class<? extends BaseController>, String[]> MAP_API_PREF = new ConcurrentHashMap<>();

	/**
	 * 注册控制器类
	 * @author qiuxs
	 *
	 * @param ctlClass
	 *
	 * 创建时间：2018年8月6日 下午9:27:55
	 */
	public static void register(BaseController ctl) {
		Class<? extends BaseController> ctlClass = ctl.getClass();
		RequestMapping requestMapping = ctlClass.getAnnotation(RequestMapping.class);
		// Api前缀
		String[] mappings = requestMapping.value();
		MAP_API_PREF.put(ctlClass, mappings);
		
		List<Method> apiMethods = ReflectUtils.getDeclaredMethods(ctlClass, Api.class, true);
		Map<String, ApiConfig> handlerMappings = MAP_API_CONFIG.get(ctlClass);
		if (handlerMappings == null) {
			handlerMappings = new HashMap<>();
			MAP_API_CONFIG.put(ctlClass, handlerMappings);
		}
		for (Iterator<Method> iter = apiMethods.iterator(); iter.hasNext();) {
			Method method = iter.next();
			Api api = method.getAnnotation(Api.class);
			ApiConfig apiConfig = new ApiConfig();
			String apiKey = api.value();
			if (StringUtils.isBlank(apiKey)) {
				// 默认使用方法名作为ApiKey
				apiKey = method.getName();
			}
			apiConfig.setKey(apiKey);
			apiConfig.setParameters(method.getParameters());
			apiConfig.setDesc(api.desc());
			apiConfig.setLoginFlag(api.loginFlag());
			apiConfig.setAuthFlag(api.authFlag());
			apiConfig.setMethod(method);
			handlerMappings.put(apiKey, apiConfig);
			if (log.isDebugEnabled()) {
				log.debug(StringUtils.append("ApiMapping \"{[",Arrays.toString(mappings) , apiKey, "] desc=[",
						apiConfig.getDesc(), "]}\""));
			}
		}
	}

	public static ApiConfig getApiConfig(Class<? extends BaseController> clz, String apiKey) {
		Map<String, ApiConfig> apiConfigs = MAP_API_CONFIG.get(clz);
		if (apiConfigs == null) {
			if (log.isDebugEnabled()) {
				log.debug(StringUtils.append("No ApiConfigs For Class<", clz.getName(), ">"));
			}
			return null;
		}
		ApiConfig apiConfig = apiConfigs.get(apiKey);
		if (apiConfig == null) {
			if (log.isDebugEnabled()) {
				log.debug(StringUtils.append("No ApiConfigs For Key<", apiKey, "> in Class<", clz.getName(), ">"));
			}
		}
		return apiConfig;
	}

	public static String[] getMappings(Class<? extends BaseController> clz) {
		String[] mappings = MAP_API_PREF.get(clz);
		return mappings;
	}
	
}
