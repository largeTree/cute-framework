package com.qiuxs.cuteframework.core.basic.utils.converter;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.namespace.QName;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.core.io.Resource;

import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.ListUtils;
import com.qiuxs.cuteframework.core.basic.utils.TypeAdapter;
import com.qiuxs.cuteframework.core.basic.utils.annotation.XMLListItem;
import com.qiuxs.cuteframework.core.basic.utils.io.IOUtils;
import com.qiuxs.cuteframework.core.basic.utils.reflect.FieldUtils;
import com.qiuxs.cuteframework.core.basic.utils.reflect.ReflectUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

/**
 * 
 * 功能描述: Xml数据转换器.
 * <p> 基于快速性选择jaxb；xStream相对简单，但性能较差
 * <p>新增原因: TODO
 * <p>新增日期: 2016年8月29日 上午11:05:53
 *  
 * @author fengdg   
 * @version 1.0.0
 */
public class XmlUtil {

	private static Logger log = LogManager.getLogger(XmlUtil.class);

	private static ConcurrentMap<Class<?>, JAXBContext> jaxbContexts = new ConcurrentHashMap<Class<?>, JAXBContext>();

	public static Document readAsDocument(String path) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(path));
			return readAsDocument(fis);
		} catch (Exception e) {
			throw ExceptionUtils.unchecked(e);
		} finally {
			IOUtils.closeQuietly(fis);
		}
	}

	/**
	 * 从资源中读取xml文档
	 * 
	 * @param res
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 * @throws Exception
	 */
	public static Document readAsDocument(Resource res) {
		InputStream is = null;
		try {
			is = res.getInputStream();
			return readAsDocument(is);
		} catch (Throwable e) {
			throw ExceptionUtils.unchecked(e);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	public static Document readAsDocument(InputStream in) {
		SAXReader reader = new SAXReader();
		Document doc = null;
		try {
			doc = reader.read(in);
		} catch (DocumentException e) {
			throw ExceptionUtils.unchecked(e);
		}
		return doc;
	}

	/**
	 * Java Object->Xml without encoding.
	 */
	public static String toXml(Object root) {
		Class<?> clazz = ReflectUtils.getCGLIBTargetClass(root);
		return toXml(root, clazz, null);
	}

	/**
	 * Java Object->Xml with encoding.
	 */
	public static String toXml(Object root, String encoding) {
		Class<?> clazz = ReflectUtils.getCGLIBTargetClass(root);
		return toXml(root, clazz, encoding);
	}

	/**
	 * Java Object->Xml without encoding.
	 */
	public static String toXml(Object root, Class<?> clazz) {
		return toXml(root, clazz, null);
	}

	/**
	 * Java Object->Xml with encoding.
	 */
	public static String toXml(Object root, Class<?> clazz, String encoding) {
		try {
			StringWriter writer = new StringWriter();
			createMarshaller(clazz, encoding).marshal(root, writer);
			return writer.toString();
		} catch (JAXBException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * Java Collection->Xml without encoding, 特别支持Root Element是Collection的情形.
	 */
	public static String toXml(Collection<?> root, String rootName, Class<?> clazz) {
		return toXml(root, rootName, clazz, null);
	}

	/**
	 * Java Collection->Xml with encoding, 特别支持Root Element是Collection的情形.
	 */
	public static String toXml(Collection<?> root, String rootName, Class<?> clazz, String encoding) {
		try {
			CollectionWrapper wrapper = new CollectionWrapper();
			wrapper.collection = root;

			JAXBElement<CollectionWrapper> wrapperElement = new JAXBElement<CollectionWrapper>(new QName(rootName), CollectionWrapper.class, wrapper);

			StringWriter writer = new StringWriter();
			createMarshaller(clazz, encoding).marshal(wrapperElement, writer);

			return writer.toString();
		} catch (JAXBException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * Xml->Java Object.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fromXml(String xml, Class<T> clazz) {
		try {
			StringReader reader = new StringReader(xml);
			return (T) createUnmarshaller(clazz).unmarshal(reader);
		} catch (JAXBException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * 创建Marshaller并设定encoding(可为null).
	 * 线程不安全，需要每次创建或pooling。
	 */
	public static Marshaller createMarshaller(Class<?> clazz, String encoding) {
		try {
			JAXBContext jaxbContext = getJaxbContext(clazz);

			Marshaller marshaller = jaxbContext.createMarshaller();

			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			if (StringUtils.isNotBlank(encoding)) {
				marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
			}

			return marshaller;
		} catch (JAXBException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * 创建UnMarshaller.
	 * 线程不安全，需要每次创建或pooling。
	 */
	public static Unmarshaller createUnmarshaller(Class<?> clazz) {
		try {
			JAXBContext jaxbContext = getJaxbContext(clazz);
			return jaxbContext.createUnmarshaller();
		} catch (JAXBException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	protected static JAXBContext getJaxbContext(Class<?> clazz) {
		Validate.notNull(clazz, "'clazz' must not be null");
		JAXBContext jaxbContext = jaxbContexts.get(clazz);
		if (jaxbContext == null) {
			try {
				jaxbContext = JAXBContext.newInstance(clazz, CollectionWrapper.class);
				jaxbContexts.putIfAbsent(clazz, jaxbContext);
			} catch (JAXBException ex) {
				throw new RuntimeException("Could not instantiate JAXBContext for class [" + clazz + "]: " + ex.getMessage(), ex);
			}
		}
		return jaxbContext;
	}

	/**
	 * 封装Root Element 是 Collection的情况.
	 */
	public static class CollectionWrapper {

		@XmlAnyElement
		protected Collection<?> collection;
	}

	public static String mapToCdataXml(Map<String, String> paramMap) {
		return mapToXml(paramMap, paramMap.keySet());
	}

	/**
	 * Map转xml
	 * 2017/02/17
	 * @author maozj  
	 * @param paramMap
	 * @param cdataFields
	 * @return
	 */
	public static String mapToXml(Map<String, String> paramMap, Collection<String> cdataFields) {
		if (cdataFields == null) {
			cdataFields = Collections.<String> emptySet();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		for (Map.Entry<String, String> entry : paramMap.entrySet()) {
			String k = entry.getKey();
			String v = entry.getValue();
			if (cdataFields.contains(k)) {
				sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
			} else {
				sb.append("<" + k + ">" + v + "</" + k + ">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}

	public static String map2Xml(Map<String, ?> map) {
		String xml;
		try {
			Document d = DocumentHelper.createDocument();
			Element root = d.addElement("xml");
			for (Map.Entry<String, ?> entry : map.entrySet()) {
				root.addElement(entry.getKey()).addText(entry.getValue().toString());

			}
			StringWriter stringWriter = new StringWriter();
			XMLWriter xmlWriter = new XMLWriter(stringWriter);
			xmlWriter.setEscapeText(false);
			xmlWriter.write(d);

			xml = stringWriter.toString();
		} catch (IOException e) {
			e.printStackTrace();
			xml = "";
		}

		return xml;
	}

	public static Map<String, String> xml2Map(String xmlStr) {
		Map<String, String> mapResult = null;
		try {
			SAXReader saxReader = new SAXReader();
			StringReader reader = new StringReader(xmlStr);
			Document doc = saxReader.read(reader);
			Element root = doc.getRootElement();
			@SuppressWarnings("unchecked")
			List<Element> listElement = root.elements();
			mapResult = new HashMap<String, String>();
			for (Element ele : listElement) {
				mapResult.put(ele.getName(), ele.getTextTrim());
			}
		} catch (DocumentException e) {
			throw new RuntimeException(e);
		}
		return mapResult;
	}

	/**
	 * xml转换成Java对象
	 *  
	 * @author maozj  
	 * @param xml
	 * @param clazz
	 * @return
	 */
	public static <T> T xml2Bean(String xml, Class<T> clazz) {

		JSONObject jo = new JSONObject();
		try {
			Document document = DocumentHelper.parseText(xml);
			Element root = document.getRootElement();
			@SuppressWarnings("unchecked")
			List<Element> list = root.elements();
			for (Element e : list) {
				jo.put(e.getName(), e.getTextTrim());
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		return jo.toJavaObject(clazz);
	}

	/**
	 * 把类转为xml
	 * 2017/02/17
	 * @author maozj  
	 * @param object
	 * @return
	 */
	public static String getXMLFromObject(Object object) {
		//解决XStream对出现双下划线的bug
		XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
		//将要提交给API的数据对象转换成XML格式数据Post给API
		String xml = xStreamForRequestPostData.toXML(object);
		return xml;
	}

	/**
	 * 把xml转为指定类
	 *  
	 * @author maozj  
	 * @param xml
	 * @param tClass
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object getObjectFromXML(String xml, Class tClass) {
		//将从API返回的XML数据映射到Java对象
		XStream xStreamForResponseData = new XStream();
		xStreamForResponseData.alias("xml", tClass);
		xStreamForResponseData.ignoreUnknownElements();//暂时忽略掉一些新增的字段
		return xStreamForResponseData.fromXML(xml);
	}

	/**
	 * 设置属性
	 * @param bean
	 * @param ele
	 * @throws Exception
	 */
	public static void setBeanByElement(Object bean, Element ele) {
		PropertyDescriptor[] arrPropDesc = PropertyUtils.getPropertyDescriptors(bean);
		for (PropertyDescriptor propDesc : arrPropDesc) {
			String fileTypeName = propDesc.getPropertyType().toString();
			String propName = propDesc.getName();
			String propValue = ele.attributeValue(propName);
			if (propValue == null)
				continue;
			Object val = null;
			if (fileTypeName.equals("int") || fileTypeName.endsWith("Integer")) {
				val = new Integer(propValue);
			} else if (fileTypeName.equals("long") || fileTypeName.endsWith("Long")) {
				val = new Long(propValue);
			} else if (fileTypeName.endsWith("String")) {
				val = propValue;
			}

			try {
				PropertyUtils.setProperty(bean, propName, val);
			} catch (Exception e) {
				String msg = "setBean[" + bean.getClass().getName() + "] Prop[" + propName + "] failed, ext = " + e.getLocalizedMessage();
				if (log.isDebugEnabled()) {
					log.debug(msg, e);
				} else {
					log.warn(msg);
				}
			}
		}
	}

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
				List<Field> classFields = FieldUtils.getDeclaredFieldsNoDup(clz);
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
				Class<?> itemClass = FieldUtils.getListFieldParameterizedType(field);
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
			List<Field> fields = FieldUtils.getDeclaredFieldsNoDup(clz);
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
