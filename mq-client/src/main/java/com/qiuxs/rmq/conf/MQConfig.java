package com.qiuxs.rmq.conf;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.core.io.Resource;

import com.qiuxs.cuteframework.core.basic.utils.ClassPathResourceUtil;
import com.qiuxs.cuteframework.core.basic.utils.CollectionUtils;
import com.qiuxs.cuteframework.core.basic.utils.converter.XmlUtil;
import com.qiuxs.cuteframework.core.basic.utils.io.IOUtils;

public class MQConfig {

	private static Logger log = LogManager.getLogger(MQConfig.class);

	/** 默认的配置文件 */
	private static final String CONF_FILE = "classpath*:config/mq.xml";

	/** 普通消费者 */
	private static Map<String, ListenerProp> consumerMap;
	/** 广播消费者 */
	private static Map<String, ListenerProp> consumerBMap;

	static {
		init();
	}

	private static void init() {
		try {
			List<Resource> resList = ClassPathResourceUtil.getResourceList(CONF_FILE);
			if (CollectionUtils.isEmpty(resList)) {
				log.info("没有配置classpath*:/config/mq.xml 不启用RocketMq...");
				return;
			}
			
			// 第一个是普通消费者，第二个是广播消费者
			@SuppressWarnings("unchecked")
			Map<String, ListenerProp>[] tempConsumerMap = new HashMap[] {new HashMap<>(), new HashMap<>()};
			
			for (Resource res : resList) {
				InputStream is = res.getInputStream();
				if (is != null) {
					try{
						initOne(is, res.getURL(), tempConsumerMap);
					} finally {
						IOUtils.closeQuietly(is);
					}
				}
			}
			
			consumerMap = tempConsumerMap[0];
			consumerBMap = tempConsumerMap[1];
		} catch (Throwable e) {
			log.error("初始化Mq失败... ext = " + e.getLocalizedMessage(), e);
		}
	}

	private static void initOne(InputStream inputStream, URL resUrl, Map<String, ListenerProp>[] tempConsumerMap) {
		Document doc = XmlUtil.readAsDocument(inputStream);
		Element rooElement = doc.getRootElement();
		
		// 普通消费者
		initConsumers(tempConsumerMap[0], rooElement.element("consumer"));
		// 广播消费者
		initConsumers(tempConsumerMap[1], rooElement.element("consumerB"));
	}

	@SuppressWarnings("unchecked")
	private static void initConsumers(Map<String, ListenerProp> listenerMap, Element consumerE) {
		Iterator<Element> topicIter = consumerE.elementIterator("topic");
		while (topicIter.hasNext()) {
			Element topicElement = topicIter.next();
			// 主题
			String topic = topicElement.attributeValue("id");

			Iterator<Element> listenerEles = topicElement.elementIterator("listener");
			while (listenerEles.hasNext()) {
				Element listenerE = listenerEles.next();
				String tags = listenerE.attributeValue("tags");
				String bean = listenerE.attributeValue("bean");
				String method = listenerE.attributeValue("method");
				String[] tagList = tags.split("\\|\\|");
				for (String tag : tagList) {
					ListenerProp listenerProp = new ListenerProp(topic, tag, bean, method);
					listenerMap.put(topic + "." + tag, listenerProp);
				}
			}
		}
	}

	public static Map<String, ListenerProp> getConsumerMap() {
		return consumerMap;
	}

	public static Map<String, ListenerProp> getConsumerBMap() {
		return consumerBMap;
	}

}
