package com.qiuxs.cuteframework.core.basic.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;

public class BeanUtil extends BeanUtils {

	private static Logger log = LogManager.getLogger(BeanUtil.class);

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
