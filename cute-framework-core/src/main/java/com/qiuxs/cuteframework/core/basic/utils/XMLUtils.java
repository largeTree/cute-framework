package com.qiuxs.cuteframework.core.basic.utils;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class XMLUtils {

	private static Logger log = LogManager.getLogger(XMLUtils.class);

	public static <T> T XMLStringToJavaBean(String xml, Class<T> clz) {
		try {
			Document document = DocumentHelper.parseText(xml);
			return XMLElmentToJavaBean(document.getRootElement(), clz);
		} catch (DocumentException e) {
			log.error("Parse XML To Bean Error ext=" + e.getLocalizedMessage(), e);
			throw ExceptionUtils.unchecked(e);
		}
	}

	public static <T> T XMLElmentToJavaBean(Element element, Class<T> clz) {
		try {
			T bean = clz.newInstance();
			List<Field> classFields = ReflectUtils.getDeclaredFieldsNoDup(clz);
			for (Field field : classFields) {
				field.setAccessible(true);
				String fieldName = field.getName();
				Element childElement = element.element(fieldName);
				Class<?> fieldType = field.getType();
				Object value;
				if (fieldType.isPrimitive() || ReflectUtils.isPrimitivePackagingClass(fieldType) || fieldType.isAssignableFrom(String.class)) {
					value = TypeAdapter.adapter(childElement.getTextTrim(), fieldType);
				} else {
					value = XMLElmentToJavaBean(childElement, fieldType);
				}
				field.set(bean, value);
			}
			return bean;
		} catch (InstantiationException | IllegalAccessException e) {
			log.error("XMLElement to Bean Error ext=" + e.getLocalizedMessage(), e);
			throw ExceptionUtils.unchecked(e);
		}
	}

	public static String beanToXMLString(Object obj) {
		return "";
	}

}
