package com.qiuxs.cuteframework.core.basic.config;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class ClasspathResourceUtils {

	private static ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

	public static Resource getResource(String path) {
		Resource[] resources = getResources(path);
		if (resources != null && resources.length > 0) {
			return resources[0];
		}
		return null;
	}

	public static Resource[] getResources(String path) {
		try {
			return resolver.getResources(path);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
