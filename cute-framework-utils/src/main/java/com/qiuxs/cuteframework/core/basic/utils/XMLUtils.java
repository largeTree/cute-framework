package com.qiuxs.cuteframework.core.basic.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.qiuxs.cuteframework.core.basic.utils.annotation.XMLListItem;

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
	public static <T> T xmlStringToJavaBean(String xml, Class<T> clz) {
		try {
			Document document = DocumentHelper.parseText(xml);
			return xmlElmentToJavaBean(document.getRootElement(), clz);
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
	@SuppressWarnings("unchecked")
	public static <T> T xmlElmentToJavaBean(Element element, Class<T> clz) {
		try {
			T bean;
			if (ReflectUtils.isSimpleType(clz)) {
				bean = (T) TypeAdapter.adapter(element.getTextTrim(), clz);
			} else {
				bean = clz.newInstance();
				List<Field> classFields = ReflectUtils.getDeclaredFieldsNoDup(clz);
				for (Field field : classFields) {
					field.setAccessible(true);
					String fieldName = field.getName();
					Element childElement = element.element(fieldName);
					if (childElement == null || Modifier.isStatic(field.getModifiers())) {
						continue;
					}
					Object value = parseItemValue(childElement, field);
					field.set(bean, value);
				}
			}
			return bean;
		} catch (ReflectiveOperationException e) {
			log.error("XMLElement to Bean Error ext=" + e.getLocalizedMessage(), e);
			throw ExceptionUtils.unchecked(e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Object parseItemValue(Element element, Field field) throws ReflectiveOperationException {
		Class<?> fieldType = field.getType();
		Object value;
		// 简单类型直接取当前节点中的字符串转为对应类型
		if (ReflectUtils.isSimpleType(fieldType)) {
			value = TypeAdapter.adapter(element.getTextTrim(), fieldType);
		} else if (fieldType.isAssignableFrom(List.class)) {
			// List字段转为List
			XMLListItem listItem = field.getAnnotation(XMLListItem.class);
			String itemName = null;
			if (listItem != null) {
				itemName = listItem.value();
				value = listItem.listType().newInstance();
			} else {
				value = new ArrayList<>();
			}
			List list = (List) value;
			List<Element> listItems;
			if (itemName != null) {
				listItems = element.elements(itemName);
			} else {
				listItems = element.elements();
			}
			if (!ListUtils.isNullOrEmpty(listItems)) {
				Class<?> itemClass = ReflectUtils.getListFieldParameterizedType(field);
				for (Iterator<Element> iterator = listItems.iterator(); iterator.hasNext();) {
					Element item = iterator.next();
					list.add(xmlElmentToJavaBean(item, itemClass));
				}
			}
		} else {
			value = xmlElmentToJavaBean(element, fieldType);
		}
		return value;
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
				if (Modifier.isStatic(field.getModifiers())) {
					continue;
				}
				Element childElement = element.addElement(fieldName);
				Object val = field.get(obj);
				if (ReflectUtils.isSimpleType(fieldType)) {
					childElement.setText(val == null ? "" : val.toString());
				} else if (fieldType.isAssignableFrom(List.class)) {
					XMLListItem listItem = field.getAnnotation(XMLListItem.class);
					List<?> list = (List<?>) val;
					String itemName;
					if (listItem != null) {
						itemName = listItem.value();
					} else {
						itemName = "item";
					}
					for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
						Object itemVal = iterator.next();
						fillBeanToXMLElement(itemVal, childElement.addElement(itemName));
					}
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
