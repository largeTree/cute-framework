package com.hzecool.fdn.utils.converter;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
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
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.alibaba.fastjson.JSONObject;
import com.hzecool.fdn.exception.utils.Exceptions;
import com.hzecool.fdn.utils.reflect.Reflections;
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

	private static ConcurrentMap<Class<?>, JAXBContext> jaxbContexts = new ConcurrentHashMap<Class<?>, JAXBContext>();

	/**
	 * Java Object->Xml without encoding.
	 */
	public static String toXml(Object root) {
		Class<?> clazz = Reflections.getUserClass(root);
		return toXml(root, clazz, null);
	}

	/**
	 * Java Object->Xml with encoding.
	 */
	public static String toXml(Object root, String encoding) {
		Class<?> clazz = Reflections.getUserClass(root);
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
			throw Exceptions.unchecked(e);
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

			JAXBElement<CollectionWrapper> wrapperElement = new JAXBElement<CollectionWrapper>(new QName(rootName),
					CollectionWrapper.class, wrapper);

			StringWriter writer = new StringWriter();
			createMarshaller(clazz, encoding).marshal(wrapperElement, writer);

			return writer.toString();
		} catch (JAXBException e) {
			throw Exceptions.unchecked(e);
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
			throw Exceptions.unchecked(e);
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
			throw Exceptions.unchecked(e);
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
			throw Exceptions.unchecked(e);
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
				throw new RuntimeException("Could not instantiate JAXBContext for class [" + clazz + "]: "
						+ ex.getMessage(), ex);
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
	
	public static String mapToCdataXml(Map<String,String> paramMap){
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
	public static String mapToXml(Map<String,String> paramMap, Collection<String> cdataFields){
		if (cdataFields == null) {
			cdataFields = Collections.<String>emptySet();
        }
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		for (Map.Entry<String, String>	entry : paramMap.entrySet()) {
	        String k = entry.getKey();
	        String v = entry.getValue();
	        if (cdataFields.contains(k)) {
	        	sb.append("<"+k+">"+"<![CDATA["+v+"]]></"+k+">");
            } else {
            	sb.append("<"+k+">"+v+"</"+k+">");
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
	public static void setBeanByElement(Object bean, Element ele) throws Exception {
		PropertyDescriptor[] arrPropDesc =  PropertyUtils.getPropertyDescriptors(bean);
		for(PropertyDescriptor propDesc : arrPropDesc){
			String fileTypeName = propDesc.getPropertyType().toString();
			String propName = propDesc.getName();
			String propValue = ele.attributeValue(propName);
			if(propValue == null)continue;
			Object val = null;
			if(fileTypeName.equals("int") || fileTypeName.endsWith("Integer")){
				val = new Integer(propValue);
			}else if(fileTypeName.equals("long") || fileTypeName.endsWith("Long")){
				val = new Long(propValue);
			}else if(fileTypeName.endsWith("String")){
				val = propValue;
			}

			PropertyUtils.setProperty(bean, propName, val);
		}
	}


}
