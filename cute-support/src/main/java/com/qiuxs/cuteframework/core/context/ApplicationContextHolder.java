package com.qiuxs.cuteframework.core.context;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.qiuxs.cuteframework.core.basic.utils.ClassUtils;

/**
 * 
 * 功能描述: SpringContext<br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2018年4月23日 下午10:07:14 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class ApplicationContextHolder {
	
	private static Logger log = LogManager.getLogger(ApplicationContextHolder.class);

	private static ApplicationContext applicationContext;

	public static void setApplicationContext(ApplicationContext applicationContext) {
		ApplicationContextHolder.applicationContext = applicationContext;
	}

	/**
	 * 获取Spring上下文
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return ApplicationContextHolder.applicationContext;
	}

	/**
	 * 根据BeanName获取bean
	 * @author qiuxs  
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		Object bean = null;
		try {
			bean = getApplicationContext().getBean(name);
		} catch (Throwable e) {
			log.error("getBean[" + name + "], failed, ext = " + e.getLocalizedMessage(), e);
		}
		return (T) bean;
	}

	/**
	 * 根据BeanName获取指定类型的bean
	 * @param name
	 * @param type
	 * @return
	 */
	public static <T> T getBean(String name, Class<T> type) {
		return getApplicationContext().getBean(name, type);
	}

	/**
	 * 根据Bean类型获取bean
	 *  
	 * @author qiuxs  
	 * @param clz
	 * @return
	 */
	public static <T> T getBean(Class<T> type) {
		return getApplicationContext().getBean(type);
	}

	/**
	 * 根据Bean类型获取所有BeanName集合
	 *  
	 * @author qiuxs  
	 * @param type
	 * @return
	 */
	public static String[] getBeanNamesForType(Class<?> type) {
		return getApplicationContext().getBeanNamesForType(type);
	}

	/**
	 * 获取有指定注解的BeanName集合
	 *  
	 * @author qiuxs  
	 * @param annotationType
	 * @return
	 */
	public static String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType) {
		return getApplicationContext().getBeanNamesForAnnotation(annotationType);
	}

	/**
	 * 获取指定类型的所有子类Bean
	 * @author qiuxs
	 *
	 * @param type
	 * @return
	 *
	 * 创建时间：2018年8月17日 下午8:34:41
	 */
	public static <T> List<T> getBeansForType(Class<T> type) {
		String[] beanNames = getBeanNamesForType(type);
		List<T> beans = new ArrayList<>();
		for (String name : beanNames) {
			beans.add(getBean(name, type));
		}
		return beans;
	}
}
