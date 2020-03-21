package com.qiuxs.cuteframework.web.controller.api;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.core.io.Resource;

import com.qiuxs.cuteframework.core.basic.Constants;
import com.qiuxs.cuteframework.core.basic.utils.ClassPathResourceUtil;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.converter.XmlUtil;
import com.qiuxs.cuteframework.core.basic.utils.reflect.MethodUtils;
import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;
import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.cuteframework.web.WebConstants;
import com.qiuxs.cuteframework.web.action.IAction;
import com.qiuxs.cuteframework.web.bean.ReqParam;

public class ApiConfigHolder {

	private static Logger log = LogManager.getLogger(ApiConfigHolder.class);

	private static final String API_CONFIG_PATH = "classpath*:/config/apiConfig.xml";

	/** Api缓存 */
	private static Map<String, ApiConfig> apiMap;

	static {
		init();
	}

	/**
	 * 获取指定接口
	 * @param apiKey
	 * @return
	 */
	public static ApiConfig getApiConfig(String apiKey) {
		// debug模式每次都自动刷新apiConfig
		if (EnvironmentContext.isDebug()) {
			ApiConfigHolder.init();
		}
		
		ApiConfig apiConfig = apiMap.get(apiKey);
		// 填充action对象和方法对象
		if (apiConfig.getAction() == null || apiConfig.getMethodObj() == null) {
			IAction action = ApplicationContextHolder.getBean(apiConfig.getBean());
			String methodName = apiConfig.getMethod();
			Method method = MethodUtils.getPublicMethod(action.getClass(), methodName);
			int paramCount = 0;
			if (method == null) {
				method = MethodUtils.getPublicMethod(action.getClass(), methodName, ReqParam.class);
    			paramCount = 1;
			}
			if (method == null) {
				method = MethodUtils.getPublicMethod(action.getClass(), methodName, ReqParam.class, String.class);
				paramCount = 2;
			}
			if (method == null) {
				throw new RuntimeException("action_method_not_found");
			}
			apiConfig.setAction(action);
			apiConfig.setMethodObj(method);
			apiConfig.setParamCount(paramCount);
		}
		return apiConfig;
	}

	/**
	 * 判断是否存在某个接口
	 * @param apiKey
	 * @return
	 */
	public static boolean containsApi(String apiKey) {
		return apiMap.containsKey(apiKey);
	}

	private static void init() {
		Map<String, ApiConfig> tempApiMap = new StrictApiMap();
		List<Resource> apiConfigFiles = ClassPathResourceUtil.getResourceList(API_CONFIG_PATH);
		for (Resource res : apiConfigFiles) {
			try {
				Document doc = XmlUtil.readAsDocument(res);
				Element root = doc.getRootElement();
				@SuppressWarnings("unchecked")
				Iterator<Element> modules = root.elementIterator("module");
				while (modules.hasNext()) {
					Element module = modules.next();
					@SuppressWarnings("unchecked")
					Iterator<Element> apis = module.elementIterator("api");
					while (apis.hasNext()) {
						ApiConfig apiConfig = new ApiConfig();
						Element next = apis.next();
						XmlUtil.setBeanByElement(apiConfig, next);
						String login = next.attributeValue("login");
						apiConfig.setLoginFlag(Constants.TRUE_STR.equals(login));
						String auth = next.attributeValue("auth");
						apiConfig.setAuthFlag(Constants.TRUE_STR.equals(auth));
						String type = next.attributeValue("type");
						if (type == null) {
							apiConfig.setType(ApiConfig.API_TYPE_USER);
						} else {
							apiConfig.setType(Integer.parseInt(type));
						}
						tempApiMap.put(apiConfig.getKey(), apiConfig);
					}
				}
			} catch (Exception e) {
				log.error("Parse " + res + " failed, ext = " + e.getLocalizedMessage(), e);
				throw ExceptionUtils.unchecked(e);
			}
		}
		apiMap = tempApiMap;
	}

	/**
	 * 用于存储ApiConfig的严格Map
	 * @author qiuxs
	 *
	 */
	private static class StrictApiMap extends HashMap<String, ApiConfig> {
		private static final long serialVersionUID = -7054632393156510860L;

		public ApiConfig get(Object key) {
			ApiConfig val = super.get(key);
			if (val == null) {
				ExceptionUtils.throwLogicalException(WebConstants.ErrorCode.API_KEY_NOT_EXISTS, "api_key_not_exists", key);
			}
			return val;
		}

		public ApiConfig put(String key, ApiConfig value) {
			ApiConfig oldVal = super.put(key, value);
			if (oldVal != null) {
				throw new RuntimeException("Duplicate ApiKey [ " + key + " ]");
			}
			return oldVal;
		};
	}

}
