package com.qiuxs.rmq;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;

import com.qiuxs.cuteframework.core.basic.bean.Pair;
import com.qiuxs.cuteframework.core.basic.config.IConfiguration;
import com.qiuxs.cuteframework.core.basic.config.UConfigUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.rmq.conf.ListenerProp;
import com.qiuxs.rmq.conf.MQConfig;

public class ConsumerUtils {
	
	private static Logger log = LogManager.getLogger(ConsumerUtils.class);

	/**  默认消费者线程最大最小数量 */
	private static final int DEFAULT_CONSUME_THREAD_MAX = 5;
	private static final int DEFAULT_CONSUME_THREAD_MIN = 1;

	//默认情况下，消息消费失败时，MQ将自动重试。默认最大重试次数是16次，按1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h的延迟重试。
	//即默认是在4 小时 46 分钟内重试16次，如果16次仍然没有消费成功，消息将丢弃。
	//考虑到我们业务的实际情况，4小时多丢弃消息风险太大，加大消息重试次数到50次。
	//16次重试后，将每2小时重试一次，总共消息可以保留三天左右。
	/** 最大重试次数。 */
	private static final int MAX_RECONSUME_TIMES = 50;
	
	/** 自动生成消费者实例ID */
	private static AtomicInteger consumerInstanceId = new AtomicInteger(1);
	
	/** 组名与消费者Map。key=消费者组名 */
	private static Map<String, DefaultMQPushConsumer> groupNameConsumerMap = new ConcurrentHashMap<>();

	public static void init() {
		log.info("[" + EnvironmentContext.getAppName() + "]Consumer init started... ");
		
		IConfiguration config = UConfigUtils.getDomain(MqClientContants.CONFIG_DOMAIN);
		if (config != null) {
			String defaultConsumerGroup = config.getString(MqClientContants.CONSUMER_GROUP_NAME);
			if (StringUtils.isNotBlank(defaultConsumerGroup)) {
				// nameserver
				String nameSrv = config.getString(MqClientContants.RMQ_NAME_SRV);
				Map<String, Pair<ListenerProp, Map<String, String>>> listenTopicTagsMap = MQConfig.getListenTopicTagsMap();
				for (Pair<ListenerProp, Map<String, String>> listenTopicTags : listenTopicTagsMap.values()) {
					ListenerProp listenerProp = listenTopicTags.getV1();
					Map<String, String> topicTags = listenTopicTags.getV2();
					addConsumer(defaultConsumerGroup, nameSrv, listenerProp, topicTags);
				}
			}
		}
	}

	private static void addConsumer(String defaultConsumerGroup, String nameSrv, ListenerProp listenerProp, Map<String, String> topicTags) {
		
	}

}
