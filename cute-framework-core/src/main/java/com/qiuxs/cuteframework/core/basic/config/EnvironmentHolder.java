package com.qiuxs.cuteframework.core.basic.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.Resource;

import com.qiuxs.cuteframework.core.basic.config.uconfig.ex.UConfigException;

/**
 * 环境配置
 * @author qiuxs
 *
 */
public class EnvironmentHolder {

	private static final String ENV_ENVIRONMENT = "environment";
	private static final String UCONFIG_PATH = "uconfig_path";

	private static final String ENVIRONMENT_PATH = "classpath:environment.xml";
	private static final Map<String, String> ENVIRONMENT = new HashMap<>();

	static {
		refresh();
	}

	/**
	 * 获取当前环境
	 * @return
	 */
	public static String currentEnvironment() {
		return ENVIRONMENT.get(ENV_ENVIRONMENT);
	}

	/**
	 * 获取一个环境配置
	 * @param name
	 * @return
	 */
	public static String getEnv(String name) {
		return ENVIRONMENT.get(name);
	}

	/**
	 * 获取当前uconfig.xml路径
	 * @return
	 */
	public static String getUconfigPath() {
		return ENVIRONMENT.get(UCONFIG_PATH);
	}

	/**
	 * 刷新环境配置缓存
	 */
	public static void refresh() {
		if (ENVIRONMENT.size() > 0) {
			ENVIRONMENT.clear();
		}
		SAXReader reader = new SAXReader();
		Resource res = ClasspathResourceUtils.getResource(ENVIRONMENT_PATH);
		try {
			Document environment = reader.read(res.getFile());
			Element rootElement = environment.getRootElement();
			@SuppressWarnings("unchecked")
			Iterator<Element> elementIterator = rootElement.elementIterator();
			while (elementIterator.hasNext()) {
				Element element = elementIterator.next();
				ENVIRONMENT.put(element.getName(), element.getTextTrim());
			}
		} catch (DocumentException e) {
			throw new UConfigException("配置格式错误========>" + e.getLocalizedMessage(), e);
		} catch (IOException e) {
			throw new UConfigException("配置文件不存在========>" + e.getLocalizedMessage(), e);
		}
	}

}