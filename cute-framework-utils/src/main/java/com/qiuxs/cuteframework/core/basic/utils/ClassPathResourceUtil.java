package com.qiuxs.cuteframework.core.basic.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class ClassPathResourceUtil {
	/** 能查询到多个UrlResource，包含最外层项目和依赖的jar包中classpath路径下的所有xx文件 */
	public static final String CLASSPATH_ALL_URL_PREFIX = "classpath*:";
	/**
	 * 只能查询到一个ClassPathResource，URL则由JDK或Tomcat(如ClassLoader.getResource())解析文件名得到，若最外层项目没有xx，则沿着依赖链查找依赖的jar中
	 */
	public static final String CLASSPATH_URL_PREFIX = "classpath:";

	// private static final String MODULE_MYENV = "myenv";

	/**
	 * 读取单个资源，顺序：外层项目优先->直接依赖的jar(->一级间接依赖的jar->二级间接依赖的jar->...)同级之间的顺序由pom.xml指定(依赖ClassLoader的加载顺序).
	 * <li>classpath*:xx 返回的是第一顺位的UrlResource，</li>
	 * <li>classpath:xx
	 * 返回的是单个ClassPathResource，URL查询顺序同classpath*:，具体由JDK或Tomcat(如ClassLoader.getResource())解析文件名得到</li>
	 * <li>绝对路径 返回的是单个UrlResource，URL=new URL(path)</li>
	 * 
	 * <p>
	 * 示例： 假如项目依赖：ec-incubator->ec-base->ec-utils，
	 * <p>
	 * 情形1：每个项目都有myenv.xml.
	 * <li>classpath*:xx ec-incubator#myenv.xml</li>
	 * <li>classpath:xx ec-incubator#myenv.xml</li>
	 * <p>
	 * 情形2：ec-incubator没有myenv.xml，ec-base和ec-utils都有myenv.xml.
	 * <li>classpath*:xx ec-base#myenv.xml</li>
	 * <li>classpath:xx ec-base#myenv.xml</li>
	 * 
	 * @param path
	 *            支持前缀"file://"、"classpath:"、"classpath*"（没有前缀等同于该前缀）；路径
	 *            支持Spring的通配符 示例：com/hzeool/☀☀/config/*.xml
	 * @return
	 */
	public static Resource getSingleResource(String path) {
		List<Resource> resources = getResourceList(false, path);
		if (resources.size() > 0) {
			return resources.get(0);
		}
		return null;
	}

	/**
	 * 读取资源列表，顺序：外层项目优先->...后面的顺序没找到规律(依赖ClassLoader的加载顺序).
	 * <li>classpath*:xx 返回的是多个UrlResource。</li>
	 * <li>classpath:xx
	 * 返回的是单个ClassPathResource，URL查询顺序同classpath*:，具体由JDK或Tomcat(如ClassLoader.getResource())解析文件名得到</li>
	 * <li>绝对路径 返回的是单个UrlResource，URL=new URL(path)</li>
	 * 
	 * <p>
	 * 示例：
	 * ec-slh2:直接依赖ec-gateway(->ec-base->ec-fdn->ec-utils)，ec-config-api(ec-utils)和ec-mq-client(->ec-base->ec-fdn->ec-utils)
	 * 查询：若ec-slh2没有msg/msg.properties，结果数组中就没有ec-slh2 Tomcat：
	 * <li>classpath*:msg/msg.properties：[ec-slh2,ec-base,ec-config-api,ec-fdn,ec-gateway,ec-mq-client,ec-utils]</li>
	 * <li>classpath:msg/msg.properties：[ec-slh2]</li> Jetty：
	 * <li>classpath*:msg/msg.properties：[ec-slh2,ec-gateway,ec-base,ec-fdn,ec-utils,ec-config-api,ec-mq-client]</li>
	 * <li>classpath:msg/msg.properties：[ec-slh2]</li>
	 * 
	 * @param paths
	 *            支持前缀"file://"、"classpath:"、"classpath*"（没有前缀等同于该前缀）；路径
	 *            支持Spring的通配符 示例：com/hzeool/☀☀/config/*.xml
	 * @return
	 */
	public static List<Resource> getResourceList(String... paths) {
		return getResourceList(true, paths);
	}

	/**
	 * 获取多个资源文件，默认包含jar中的资源文件
	 * @author qiuxs
	 *
	 * @param paths
	 * @return
	 *
	 * 创建时间：2018年7月26日 下午10:10:36
	 */
	public static List<Resource> getResourceList(List<String> paths) {
		if (paths == null) {
			return null;
		}
		return getResourceList(true, paths.toArray(new String[paths.size()]));
	}
	
	/**
	 * 获取多个资源文件
	 * 可指定是否获取jar中的资源
	 * @author qiuxs
	 *
	 * @param includeAll
	 * @param paths
	 * @return
	 *
	 * 创建时间：2018年7月26日 下午10:09:54
	 */
	private static List<Resource> getResourceList(boolean includeAll, String... paths) {
		String prefix = includeAll ? CLASSPATH_ALL_URL_PREFIX : CLASSPATH_URL_PREFIX;
		ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
		List<Resource> listResource = new ArrayList<Resource>();
		for (String path : paths) {
			// 没有已知的前缀，则添加prefix前缀
			if (!path.startsWith(CLASSPATH_ALL_URL_PREFIX) && !path.startsWith(CLASSPATH_URL_PREFIX)) {
				path = prefix + path;
			}
			try {
				Resource[] arrResource = resourceResolver.getResources(path);
				if (arrResource != null) {
					listResource.addAll(Arrays.asList(arrResource));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return listResource;
	}

	/**
	 * 从资源中读取xml文档
	 * 
	 * @param res
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 * @throws Exception
	 */
	public static Document getResourceXmlDoc(Resource res) throws IOException, DocumentException {
		InputStream is = res.getInputStream();
		try {
			SAXReader reader = new SAXReader();
			Document doc = null;
			doc = reader.read(is);
			return doc;
		} finally {
			is.close();
		}
	}

	/**
	 * 从某个资源中读取xml文档
	 * 
	 * @param path
	 *            格式如:myenv/myenv.xml
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 * @throws Exception
	 */
	public static Document getResourceXmlDoc(String path) throws IOException, DocumentException {
		Resource res = getSingleResource(path);
		if (res == null) {
			throw new RuntimeException("Could not find classpath:" + path);
		}
		return getResourceXmlDoc(res);
	}

	/**
	 * 根据classpath路径获取绝对路径
	 * 
	 * @author qiuxs
	 * @param cpPath
	 * @return
	 */
	public static String getAbsolutePath(String cpPath) {
		Resource res = getSingleResource(cpPath);
		if (res == null) {
			return null;
		}
		try {
			return res.getURL().toString();
		} catch (IOException e) {
			return null;
		}
	}
}
