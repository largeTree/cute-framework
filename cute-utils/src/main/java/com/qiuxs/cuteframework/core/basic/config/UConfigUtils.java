package com.qiuxs.cuteframework.core.basic.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;

import com.qiuxs.cuteframework.core.basic.utils.ClassPathResourceUtil;
import com.qiuxs.cuteframework.core.basic.utils.ConsoleLogger;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;

/**
 * 统一配置工具
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2019年12月21日 下午3:00:03 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class UConfigUtils {

	public static final String FILE_SYS_PREFIX = "file://";
	public static final String CLASSPATH_PREFIX = "classpath";

	/** 入口配置文件 */
	private static final String ROOT_CONFIG_FILE = CLASSPATH_PREFIX + ":/root.xml";

	private static final String CONFIG_PATH = "path";

	private static final String CONFIG_FILE_TYPE = "type";
	private static final String CONFIG_FILE_TYPE_XML = "xml";
	private static final String CONFIG_FILE_TYPE_PROPERTIES = "properties";

	private static final String DEFAULT_CONFIG_ITEM = "default";
	private static final String EXTEND_CONFIG_ITEM = "extend";

	public static final String MERGE_TYPE_REPLACE = "replace";
	public static final String MERGE_TYPE_ORDER = "order";
	public static final String MERGE_TYPE_LAST_ONE = "lastOne";

	public static final String DOMAIN_DB = "db";
	public static final String DOMAIN_ENV = "env";
	public static final String DOMAIN_DUBBO = "dubbo";

	private static Map<String, IConfiguration> configDomains;

	public static IConfiguration getDomain(String domain) {
		if (configDomains == null) {
			init();
		}
		return configDomains.get(domain);
	}

	@SuppressWarnings("unchecked")
	private static void init() {
		if (configDomains != null) {
			return;
		}
		Map<String, IConfiguration> mapDomains = new HashMap<String, IConfiguration>();
		Document rootConfig = ClassPathResourceUtil.getResourceXmlDoc(ROOT_CONFIG_FILE);
		if (rootConfig == null) {
			ExceptionUtils.throwRuntimeException("无法找到根配置文[" + ROOT_CONFIG_FILE + "]");
		}

		Element rootElement = rootConfig.getRootElement();
		Iterator<Element> domains = rootElement.elementIterator("domain");
		while (domains.hasNext()) {
			Element domain = domains.next();
			String domainId = domain.attributeValue("id");
			String type = domain.attributeValue(CONFIG_FILE_TYPE);
			String merge = domain.attributeValue("merge");
			Element defaultConfig = domain.element(DEFAULT_CONFIG_ITEM);
			if (defaultConfig == null) {
				throw new IllegaRootConfigException("domain [ " + domainId + "] has no default config file");
			}
			String path = defaultConfig.attributeValue(CONFIG_PATH);
			if (path == null) {
				throw new IllegaRootConfigException("domain [ " + domainId + "] has no default config file");
			}
			
			List<String> paths = new ArrayList<String>();
			
			paths.add(path);

			Iterator<Element> extendConfig = domain.elementIterator(EXTEND_CONFIG_ITEM);
			while (extendConfig.hasNext()) {
				path = extendConfig.next().attributeValue(CONFIG_PATH);
				if (StringUtils.isNotBlank(path)) {
					paths.add(path);
				} else {
					ConsoleLogger.log("domain [" + domainId + "] extend path is blank");
				}
			}

			IConfiguration configuration;
			if (CONFIG_FILE_TYPE_XML.equals(type)) {
				configuration = initXML(domainId, merge, paths);
			} else if (CONFIG_FILE_TYPE_PROPERTIES.equals(type)) {
				configuration = initProperties(domainId, merge, paths);
			} else {
				throw new IllegaRootConfigException("unkone domain type[" + type + "]");
			}
			mapDomains.put(domainId, configuration);

		}
		configDomains = mapDomains;
	}

	public static String getFirstAvailableLocation(String domain) {
		IConfiguration configuration = configDomains.get(domain);
		return configuration.getFirstAvailableLocation();
	}

	/**
	 * 初始化properties配置文件
	 *  
	 * @author qiuxs  
	 * @param domainId
	 * @param merge
	 * @param paths
	 * @return
	 */
	private static IConfiguration initProperties(String domainId, String merge, List<String> paths) {
		DefaultPropertiesConfiguration configuration = new DefaultPropertiesConfiguration(merge);
		configuration.addPaths(paths);
		return configuration;
	}

	/**
	 * 初始化xml配置文件
	 *  
	 * @author qiuxs  
	 * @param domainId
	 * @param merge
	 * @param paths
	 * @return
	 */
	private static IConfiguration initXML(String domainId, String merge, List<String> paths) {
		DefaultXMLConfiguration configuration = new DefaultXMLConfiguration(merge);
		configuration.addPaths(paths);
		return configuration;
	}

}
