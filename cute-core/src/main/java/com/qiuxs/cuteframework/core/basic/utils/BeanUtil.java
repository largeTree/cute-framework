package com.qiuxs.cuteframework.core.basic.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;

/**
 * 扩展Bean工具
 * @author qiuxs
 * 
 * 创建时间 ： 2018年7月26日 下午10:09:06
 *
 */
public class BeanUtil extends BeanUtils {

	private static Logger log = LogManager.getLogger(BeanUtil.class);

	/**
	 * 根据类名实例化一个对象
	 * @param className
	 * @return
	 */
	public static <T> T instantiateByName(String className) {
		@SuppressWarnings("unchecked")
		Class<T> clz = (Class<T>) ClassUtils.forName(className);
		return instantiateClass(clz);
	}
	
	/**
	 * 复制对象属性，忽略为Null的值
	 * @author qiuxs
	 *
	 * @param src
	 * @param target
	 * @param ignoreFields
	 *
	 * 创建时间：2018年7月26日 下午10:09:19
	 */
	public static void copyBeanIgnoreNullValue(Object src, Object target, String... ignoreFields) {
		if (src == null || target == null) {
			return;
		}
		PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(src.getClass());
		Set<String> setIgnoreFields = null;
		if (ignoreFields != null && ignoreFields.length > 0) {
			setIgnoreFields = new HashSet<>();
			for (String fieldName : ignoreFields) {
				setIgnoreFields.add(fieldName);
			}
		}
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			Method readMethod = propertyDescriptor.getReadMethod();
			Method writeMethod = propertyDescriptor.getWriteMethod();
			String fieldName = propertyDescriptor.getName();
			try {
				Object value = readMethod.invoke(src);
				if (value == null || setIgnoreFields.contains(fieldName)) {
					continue;
				}
				writeMethod.invoke(target, value);
			} catch (ReflectiveOperationException e) {
				log.warn("复制字段失败：" + fieldName, e);
			}
		}
	}

}