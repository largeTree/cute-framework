package com.qiuxs.cuteframework.core.basic.config.uconfig;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.springframework.core.io.Resource;

import com.qiuxs.cuteframework.core.basic.bean.IMergeable;
import com.qiuxs.cuteframework.core.basic.config.ClasspathResourceUtils;
import com.qiuxs.cuteframework.core.basic.config.uconfig.ex.UConfigException;
import com.qiuxs.cuteframework.core.basic.config.uconfig.impl.JSONConfiguration;
import com.qiuxs.cuteframework.core.basic.config.uconfig.impl.PropertiesConfiguration;
import com.qiuxs.cuteframework.core.basic.config.uconfig.impl.XMLConfiguration;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.ListUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;

/**
 * 配置缓存
 * @author qiuxs
 *
 */
public class ConfigCache {

	/** 配置文件类型：xml配置 */
	private static final String CONFIG_TYPE_XML = "xml";
	/** 配置文件类型：json配置 */
	private static final String CONFIG_TYPE_JSON = "json";
	/** 配置文件类型：properties配置 */
	private static final String CONFIG_TYPE_PROPERTIES = "properties";

	/** 配置顺序类型：取最后一个生效的配置 */
	private static final String ORDER_TYPE_LASTONE = "lastone";
	/** 配置顺序类型：后出现的优先(缺省值) */
	private static final String ORDER_TYPE_REPLACE = "replace";

	/** 各配置域缓存 */
	private Map<String, IConfiguration> configDomains = new HashMap<>();
	/** 配置域配置信息 */
	private Map<String, Element> domainElementCachae = new HashMap<>();

	/**
	 * 统一配置入口文件位置
	 */
	private Element uconfigElement;

	public ConfigCache(Element uconfig) {
		this.uconfigElement = uconfig;
		this.refresh();
	}

	/**
	 * 获取一个配置域
	 * @param key
	 * @return
	 */
	public IConfiguration getDomain(String key) {
		return this.configDomains.get(key);
	}

	/**
	 * 刷新配置缓存
	 */
	@SuppressWarnings("unchecked")
	public void refresh() {
		// 如已存在的话就清除一下
		if (this.configDomains != null && this.configDomains.size() > 0) {
			this.configDomains.clear();
		}
		if (this.uconfigElement == null) {
			throw new UConfigException("uconfigElement must not be Null");
		}
		Iterator<Element> domains = this.uconfigElement.elementIterator();
		while (domains.hasNext()) {
			Element domain = domains.next();
			// 初始化配置域
			initDomain(domain);
			// 缓存配置域节点信息
			domainElementCachae.put(domain.attributeValue("id"), domain);
		}
	}

	/**
	 * 初始化一个配置域
	 * @param domainId
	 * @param type
	 * @param order
	 * @param items
	 */
	private void initDomain(Element domain) {
		String domainId = domain.attributeValue("id");
		String type = domain.attributeValue("type");
		String order = domain.attributeValue("order");
		@SuppressWarnings("unchecked")
		List<Element> items = domain.elements();
		if (ListUtils.isNullOrEmpty(items)) {
			return;
		}
		IConfiguration config = null;
		switch (order) {
		case ORDER_TYPE_LASTONE:
			config = initLastOne(type, items);
			break;
		case ORDER_TYPE_REPLACE:
		default:
			config = initReplace(type, items);
			break;
		}
		if (config != null) {
			this.configDomains.put(domainId, config);
		}
	}

	/**
	 * 以最后出现的有效值生效的方式初始化配置
	 * @param type
	 * @param items
	 * @return
	 */
	private IConfiguration initLastOne(String type, List<Element> items) {
		// 从后往前初始化配置
		for (int i = items.size() - 1; i >= 0; i--) {
			Element item = items.get(i);
			IConfiguration config = null;
			switch (type) {
			case CONFIG_TYPE_JSON:
				config = initOne(JSONConfiguration.class, item);
				break;
			case CONFIG_TYPE_PROPERTIES:
				config = initOne(PropertiesConfiguration.class, item);
				break;
			case CONFIG_TYPE_XML:
				config = initOne(XMLConfiguration.class, item);
				break;
			default:
				config = initCustomerConfiguration(type, item);
			}
			// 找到一个即返回
			if (config != null) {
				return config;
			}
		}
		return null;
	}

	/**
	 * 以后出现的值替换先出现的值的方式初始化配置
	 * @param type
	 * @param items
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private IConfiguration initReplace(String type, List<Element> items) {
		IConfiguration finalConfig = null;
		for (Element item : items) {
			IConfiguration currentConfig = null;
			switch (type) {
			case CONFIG_TYPE_JSON:
				currentConfig = initOne(JSONConfiguration.class, item);
				break;
			case CONFIG_TYPE_PROPERTIES:
				currentConfig = initOne(PropertiesConfiguration.class, item);
				break;
			case CONFIG_TYPE_XML:
				currentConfig = initOne(XMLConfiguration.class, item);
				break;
			default:
				currentConfig = initCustomerConfiguration(type, item);
			}

			if (finalConfig == null) {
				// 第一次进入
				finalConfig = currentConfig;
			} else if (finalConfig instanceof IMergeable) {
				// 后续实现合并接口的
				((IMergeable) finalConfig).merge(currentConfig);
			} else {
				// 后续未实现合并接口的
				finalConfig = currentConfig;
			}
		}
		return finalConfig;
	}

	/**
	 * 根据自定义类名初始化一个配置域
	 * @param type
	 * @param item
	 * @return
	 */
	private IConfiguration initCustomerConfiguration(String type, Element item) {
		if (StringUtils.isNotBlank(type)) {
			try {
				return initOne(Class.forName(type), item);
			} catch (ClassNotFoundException e) {
				throw new UConfigException("unkone config type" + e.getLocalizedMessage(), e);
			}
		}
		throw new UConfigException("domain type must not be null");
	}

	/**
	 * 初始化一个配置项的配置
	 * 如果存在多个，并实现了合并接口的 则直接合并多个
	 * 存在多个，但未实现合并接口的，返回最后一个
	 * @param clz
	 * @param item
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private IConfiguration initOne(Class<?> clz, Element item) {
		try {
			IConfiguration config = null;
			String path = item.attributeValue("path");
			Resource[] resources = ClasspathResourceUtils.getResources(path);
			if (resources == null || resources.length == 0) {
				return null;
			}
			Constructor<?> constructor = clz.getConstructor(InputStream.class);
			for (Resource res : resources) {
				if (res.getFile().exists()) {
					if (config == null) {
						// 第一个
						config = (IConfiguration) constructor.newInstance(res.getInputStream());
					} else if (config instanceof IMergeable) {
						// 实现了合并接口的后续
						((IMergeable) config).merge(constructor.newInstance(res.getInputStream()));
					} else {
						// 未实现合并接口的替换之前的
						config = (IConfiguration) constructor.newInstance(res.getInputStream());
					}
				}
			}
			return config;
		} catch (Exception e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

}
