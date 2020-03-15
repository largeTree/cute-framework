package com.qiuxs.cuteframework.core.basic.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import com.qiuxs.cuteframework.core.basic.utils.ClassPathResourceUtil;

/**
 * 默认Properties配置
 * 功能描述: <p>  
 * 新增原因: TODO<p>  
 * 新增日期: 2019年12月30日 下午5:11:02 <p>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class DefaultPropertiesConfiguration extends AbstractConfiguration {

	public DefaultPropertiesConfiguration(String merge) {
		super(merge);
	}

	public void addProperties(Properties properties) {
		Set<Entry<Object, Object>> entrySet = properties.entrySet();
		for (Entry<Object, Object> entry : entrySet) {
			super.put(entry.getKey().toString(), entry.getValue().toString());
		}
	}

	public void addProperties(List<Properties> properties) {
		for (Properties prop : properties) {
			this.addProperties(prop);
		}
	}
	
	public void addPaths(List<String> paths) {
		for (String path : paths) {
			this.addPath(path);
		}
	}

	public void addPath(String path) {
		try {
			path = super.handlePath(path);
			Properties prop = null;
			if (path.startsWith(UConfigUtils.CLASSPATH_PREFIX)) {
				// classpath下的文件
				prop = new Properties();
				try {
					prop.load(ClassPathResourceUtil.getSingleResource(path).getInputStream());
				} catch (IOException e) {
					prop = null;
					log.error("load Properties[" + path + "] failed, ext = " + e);
				}
			} else {
				// 文件系统中的文件
				prop = new Properties();
				try {
					prop.load(new FileInputStream(path));
				} catch (IOException e) {
					prop = null;
					log.error("load Properties[" + path + "] failed, ext = " + e);
				}
			}
			if (prop == null) {
				return;
			}
			this.addProperties(prop);
			log.info(path + ", config loaded...");
		} catch (Exception e) {
			log.warn(path + ", config load failed...");
		}
	}
}
