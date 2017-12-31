package com.qiuxs.cuteframework.core.context;

import java.lang.annotation.Annotation;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextHolder implements ApplicationContextAware {

	private static ApplicationContext context = null;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ApplicationContextHolder.context = applicationContext;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) context.getBean(name);
	}

	public static <T> T getBean(String name, Class<T> clz) {
		return context.getBean(name, clz);
	}

	public static <T> T getBean(Class<T> clz) {
		return context.getBean(clz);
	}

	public static String[] getBeanNamesForType(Class<?> clz) {
		return context.getBeanNamesForType(clz);
	}

	public static String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotation) {
		return context.getBeanNamesForAnnotation(annotation);
	}

}
