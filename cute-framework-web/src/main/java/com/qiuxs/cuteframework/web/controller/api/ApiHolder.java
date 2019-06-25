package com.qiuxs.cuteframework.web.controller.api;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.basic.utils.reflect.MethodUtils;
import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;
import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.cuteframework.web.WebConstants;
import com.qiuxs.cuteframework.web.action.IAction;
import com.qiuxs.cuteframework.web.annotation.Api;

@Component
public class ApiHolder implements ApplicationListener<ContextRefreshedEvent> {

	private static Logger log = LogManager.getLogger(ApiHolder.class);

	/** Api缓存 */
	private StrictApiMap apiMap = new StrictApiMap();

	/**
	 * 获取指定接口
	 * @param apiKey
	 * @return
	 */
	public ApiConfig getApiConfig(String apiKey) {
		return apiMap.get(apiKey);
	}

	/**
	 * 判断是否存在某个接口
	 * @param apiKey
	 * @return
	 */
	public boolean containsApi(String apiKey) {
		return apiMap.containsKey(apiKey);
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		this.refreshApiConfig(event.getApplicationContext());
	}

	/**
	 * 刷新接口配置
	 * @param context
	 */
	private void refreshApiConfig(ApplicationContext context) {
		StrictApiMap apiMap = new StrictApiMap();
		String[] actionNames = context.getBeanNamesForType(IAction.class);
		for (String beanName : actionNames) {
			IAction action = context.getBean(beanName, IAction.class);
			Class<? extends IAction> clz = action.getClass();
			String actionName = clz.getSimpleName();
			// 简化ActionName
			actionName = StringUtils.firstToLowerCase(actionName.replace("Action", ""));
			List<Method> apiMethods = MethodUtils.getDeclaredMethods(clz, Api.class, true);
			for (Iterator<Method> iter = apiMethods.iterator(); iter.hasNext();) {
				Method method = iter.next();
				// 忽略权限验证
				method.setAccessible(true);
				Api api = method.getAnnotation(Api.class);
				ApiConfig apiConfig = new ApiConfig();
				String apiKey = api.value();
				
				// 自动生成ApiKey
				if (WebConstants.AUTO_API_KEY.equals(apiKey)) {
					apiKey = this.autoApiKey(actionName, method.getName());
				}
				
				apiConfig.setKey(apiKey);
				apiConfig.setDesc(api.desc());
				apiConfig.setLoginFlag(api.login());
				apiConfig.setAuthFlag(api.authFlag());
				apiConfig.setBean(action);
				apiConfig.setMethod(method);
				apiConfig.setParamCount(method.getParameterCount());
				apiMap.put(apiKey, apiConfig);
				log.info("inited Api Key = [" + apiKey + "]");
			}
		}
		this.apiMap = apiMap;
	}

	/**
	 * 自动生成ApiKey
	 * 
	 * 2019年3月22日 下午9:24:39
	 * @auther qiuxs
	 * @param actionName
	 * @param methodName
	 * @return
	 */
	private String autoApiKey(String actionName, String methodName) {
		return new StringBuilder(actionName).append("-").append(methodName).toString();
	}
	
	/**
	 * 用于存储ApiConfig的严格Map
	 * @author qiuxs
	 *
	 */
	private class StrictApiMap extends HashMap<String, ApiConfig> {
		private static final long serialVersionUID = -7054632393156510860L;

		public ApiConfig get(Object key) {
			// debug模式每次都自动刷新apiConfig
			if (EnvironmentContext.getEnvContext().isDebug()) {
				ApiHolder.this.refreshApiConfig(ApplicationContextHolder.getApplicationContext());
			}
			ApiConfig val = super.get(key);
			if (val == null) {
				ExceptionUtils.throwLogicalException(WebConstants.ErrorCode.API_KEY_NOT_EXISTS, "api_key_not_exists", key);
			}
			return val;
		}

		public ApiConfig put(String key, ApiConfig value) {
			ApiConfig oldVal = super.put(key, value);
			if (oldVal != null) {
				throw new RuntimeException("duplicate apiKey [ " + key + " ]");
			}
			return null;
		};
	}

}
