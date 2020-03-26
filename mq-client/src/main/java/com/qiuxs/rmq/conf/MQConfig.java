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

import com.qiuxs.cuteframework.core.basic.bean.Pair;
import com.qiuxs.cuteframework.core.basic.config.IConfiguration;
import com.qiuxs.cuteframework.core.basic.config.UConfigUtils;
import com.qiuxs.cuteframework.core.basic.utils.ClassPathResourceUtil;
import com.qiuxs.cuteframework.core.basic.utils.CollectionUtils;
import com.qiuxs.cuteframework.core.basic.utils.converter.XmlUtil;
import com.qiuxs.cuteframework.core.basic.utils.io.IOUtils;
import com.qiuxs.rmq.MqClientContants;

public class MQConfig {

	private static Logger log = LogManager.getLogger(MQConfig.class);

	/** 默认的配置文件 */
	private static final String CONF_FILE = "classpath*:config/mq.xml";

	/** 普通消费者 */
	private static Map<String, ListenerProp> consumerMap;

	/** 广播消费者 */
	private static Map<String, ListenerProp> consumerBMap;

	/** 顺序消费者 */
	private static Map<String, ListenerProp> consumerOMap;

	/** Map<String=消费组最终名称, Pair<消费组属性, Map<String=主题, String=同一主题下，合并后的以||分隔的标签列表>> */
	private static Map<String, Pair<ListenerProp, Map<String, String>>> listenTopicTagsMap = new HashMap<>();

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

			// 第一个是普通消费者，第二个是广播消费者，第三个是顺序消费者
			@SuppressWarnings("unchecked")
			Map<String, ListenerProp>[] tempConsumerMap = new HashMap[] { new HashMap<>(), new HashMap<>(), new HashMap<>() };

			for (Resource res : resList) {
				InputStream is = res.getInputStream();
				if (is != null) {
					try {
						initOne(is, res.getURL(), tempConsumerMap);
					} finally {
						IOUtils.closeQuietly(is);
					}
				}
			}

			consumerMap = tempConsumerMap[0];
			consumerBMap = tempConsumerMap[1];
			consumerOMap = tempConsumerMap[2];
		} catch (Throwable e) {
			log.error("初始化Mq失败... ext = " + e.getLocalizedMessage(), e);
		}
	}

	private static void initOne(InputStream inputStream, URL resUrl, Map<String, ListenerProp>[] tempConsumerMap) {
		Document doc = XmlUtil.readAsDocument(inputStream);
		Element rooElement = doc.getRootElement();

		// 普通消费者
		initConsumers(tempConsumerMap[0], rooElement.element("consumer"), MqClientContants.LISTENER_TYPE_NORMAL);
		// 广播消费者
		initConsumers(tempConsumerMap[1], rooElement.element("consumerB"), MqClientContants.LISTENER_TYPE_B);
		// 广播消费者
		initConsumers(tempConsumerMap[2], rooElement.element("consumerO"), MqClientContants.LISTENER_TYPE_O);
	}

	@SuppressWarnings("unchecked")
	private static void initConsumers(Map<String, ListenerProp> listenerMap, Element consumerE, int listenerType) {
		if (consumerE == null) {
			return;
		}
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
					ListenerProp listenerProp = new ListenerProp(topic, tag, bean, method, listenerType);
					listenerMap.put(topic + "." + tag, listenerProp);
					addTopicTagsToMap(topic, tag, listenerProp);
				}
			}
		}
	}

	private static void addTopicTagsToMap(String topic, String tag, ListenerProp listenerProp) {
		IConfiguration config = UConfigUtils.getDomain(MqClientContants.CONFIG_DOMAIN);
		String consumerGroup = null;
		if (config != null) {
			consumerGroup = config.getString(MqClientContants.CONSUMER_GROUP_NAME);
		}
		if (listenerProp.isBroadcast()) {
			consumerGroup = consumerGroup + "B";
		}
		Pair<ListenerProp, Map<String, String>> groupTopicTags = listenTopicTagsMap.get(consumerGroup);
		if (groupTopicTags == null) {
			groupTopicTags = new Pair<ListenerProp, Map<String, String>>(listenerProp, new HashMap<>());
			listenTopicTagsMap.put(consumerGroup, groupTopicTags);
		}
		Map<String, String> topicTagMap = groupTopicTags.getV2();
		String tags = topicTagMap.get(topic);
		if (tags == null) {
			tags = tag;
		} else if (!"*".equals(tags)) {
			if ("*".equals(tag)) {
				tags = "*";
			} else {
				tags = tags + "||" + tag;
			}
		}
		topicTagMap.put(topic, tags);
	}

	public static Map<String, ListenerProp> getListener(int listenerType) {
		Map<String, ListenerProp> listenerMap;
		switch (listenerType) {
		case MqClientContants.LISTENER_TYPE_NORMAL:
			listenerMap = consumerMap;
			break;
		case MqClientContants.LISTENER_TYPE_B:
			listenerMap = consumerBMap;
			break;
		case MqClientContants.LISTENER_TYPE_O:
			listenerMap = consumerOMap;
			break;
		default:
			listenerMap = null;
			break;
		}
		return listenerMap;
	}

	public static Map<String, Pair<ListenerProp, Map<String, String>>> getListenTopicTagsMap() {
		return listenTopicTagsMap;
	}

}
