package com.qiuxs.cuteframework.tech.spring.boot.config;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

/**
 * 用来使PropertySource注解支持读取yml
 * @author qiuxs
 *
 */
public class YamlPropertySourceFactory extends DefaultPropertySourceFactory {

	@Override
	public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
		String sourceName = name != null ? name : resource.getResource().getFilename();
		if (sourceName.endsWith(".yml") || sourceName.endsWith(".yaml")) {
			Properties propertiesFromYaml = loadYml(resource);
			return new PropertiesPropertySource(sourceName, propertiesFromYaml);
		} else if (!resource.getResource().exists()) {
			return new PropertiesPropertySource(sourceName, new Properties());
		} else {
			return super.createPropertySource(name, resource);
		}
	}

	/**
	 * 将yml解析成properties对象
	 * @param resource
	 * @return
	 * @throws IOException
	 */
	private Properties loadYml(EncodedResource resource) throws IOException {
		YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
		factory.setResources(resource.getResource());
		factory.afterPropertiesSet();
		return factory.getObject();
	}

}
