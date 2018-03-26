package com.qiuxs.cuteframework.core.basic.config.uconfig;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.qiuxs.cuteframework.core.basic.config.ClasspathResourceUtils;
import com.qiuxs.cuteframework.core.basic.config.EnvironmentHolder;
import com.qiuxs.cuteframework.core.basic.config.uconfig.ex.UConfigException;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;

public class UConfigUtils {

	/** 默认的统一配置入口文件 */
	private static final String DEFAULT_UCONFIG_PATH = "classpath:uconfig.xml";

	/** 统一配置缓存 */
	private static Map<String, ConfigCache> CONFIG_CACHE = new HashMap<>();

	static {
		refresh();
	}

	public static IConfiguration getDomain(String key) {
		return CONFIG_CACHE.get(EnvironmentHolder.currentEnvironment()).getDomain(key);
	}

	private static void refresh() {
		if (CONFIG_CACHE.size() > 0) {
			CONFIG_CACHE.clear();
		}
		String uconfigPath = EnvironmentHolder.getUconfigPath();
		if (StringUtils.isBlank(uconfigPath)) {
			uconfigPath = DEFAULT_UCONFIG_PATH;
		}
		SAXReader reader = new SAXReader();
		try {
			Document uconfig = reader.read(ClasspathResourceUtils.getResource(uconfigPath).getFile());
			@SuppressWarnings("unchecked")
			Iterator<Element> items = uconfig.getRootElement().elementIterator();
			while (items.hasNext()) {
				Element item = items.next();
				CONFIG_CACHE.put(item.attributeValue("env"), new ConfigCache(item));
			}
		} catch (Exception e) {
			throw new UConfigException("read uconfig.xml failed", e);
		}
	}

}
