package com.qiuxs.cuteframework.core.basic.config;

import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class ClasspathResourceUtils {

	private static PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

	public static Resource getResource(String path) {
		return new ClassPathResource(path);
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
