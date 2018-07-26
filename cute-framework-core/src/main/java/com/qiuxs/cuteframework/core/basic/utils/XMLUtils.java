package com.qiuxs.cuteframework.core.basic.utils;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * XML相关工具
 * @author qiuxs
 * 
 * 创建时间 ： 2018年7月26日 下午10:06:13
 *
 */
public class XMLUtils {

	private static Logger log = LogManager.getLogger(XMLUtils.class);

	/**
	 * XML字符串转为JavaBean
	 * @author qiuxs
	 *
	 * @param xml
	 * @param clz
	 * @return
	 *
	 * 创建时间：2018年7月26日 下午10:07:00
	 */
	public static <T> T XMLStringToJavaBean(String xml, Class<T> clz) {
		try {
			Document document = DocumentHelper.parseText(xml);
			return XMLElmentToJavaBean(document.getRootElement(), clz);
		} catch (DocumentException e) {
			log.error("Parse XML To Bean Error ext=" + e.getLocalizedMessage(), e);
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * XML节点转为JavaBean
	 * @author qiuxs
	 *
	 * @param element
	 * @param clz
	 * @return
	 *
	 * 创建时间：2018年7月26日 下午10:07:15
	 */
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

	/**
	 * JavaBean转为XML字符串
	 * @author qiuxs
	 *
	 * @param obj
	 * @param rootElementTag
	 * @return
	 *
	 * 创建时间：2018年7月26日 下午10:07:34
	 */
	public static String beanToXMLString(Object obj, String rootElementTag) {
		Document doc = DocumentHelper.createDocument();
		Element rootElement = doc.addElement(rootElementTag);
		fillBeanToXMLElement(obj, rootElement);
		return doc.asXML();
	}

	/**
	 * 将JavaBean中的属性填充至XML节点
	 * @author qiuxs
	 *
	 * @param obj
	 * @param element
	 *
	 * 创建时间：2018年7月26日 下午10:08:03
	 */
	public static void fillBeanToXMLElement(Object obj, Element element) {
		try {
			Class<?> clz = obj.getClass();
			List<Field> fields = ReflectUtils.getDeclaredFieldsNoDup(clz);
			for (Field field : fields) {
				field.setAccessible(true);
				String fieldName = field.getName();
				Class<?> fieldType = field.getType();
				Element childElement = element.addElement(fieldName);
				Object val = field.get(obj);
				if (fieldType.isPrimitive() || ReflectUtils.isPrimitivePackagingClass(fieldType) || fieldType.isAssignableFrom(String.class)) {
					childElement.setText(val == null ? "" : val.toString());
				} else {
					fillBeanToXMLElement(val, childElement);
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			log.error("fillBeanToXMLElement Error ext=" + e.getLocalizedMessage(), e);
			throw ExceptionUtils.unchecked(e);
		}
	}

}
