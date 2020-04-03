package com.qiuxs.rmq;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import com.qiuxs.cuteframework.core.basic.bean.Pair;
import com.qiuxs.cuteframework.core.basic.config.IConfiguration;
import com.qiuxs.cuteframework.core.basic.config.UConfigUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.rmq.conf.ListenerProp;
import com.qiuxs.rmq.conf.MQConfig;
import com.qiuxs.rmq.listener.ConcurrentlyMessageListener;
import com.qiuxs.rmq.listener.OrderlyMessageListener;

/**
 * 消费者工具
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年3月26日 下午9:39:39 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
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
			try {
				String defaultConsumerGroup = config.getString(MqClientContants.CONSUMER_GROUP_NAME);
				if (StringUtils.isNotBlank(defaultConsumerGroup)) {
					// nameserver
					String nameSrv = config.getString(MqClientContants.RMQ_NAME_SRV);
					Map<String, Pair<ListenerProp, Map<String, String>>> listenTopicTagsMap = MQConfig.getListenTopicTagsMap();
					for (Pair<ListenerProp, Map<String, String>> listenTopicTags : listenTopicTagsMap.values()) {
						ListenerProp listenerProp = listenTopicTags.getV1();
						Map<String, String> topicTags = listenTopicTags.getV2();
						addConsumer(defaultConsumerGroup, nameSrv, listenerProp, topicTags, config);
					}
				}
			} catch (Exception e) {
				log.error("InitRocketMqConsumer failed, ext = " + e.getLocalizedMessage(), e);
			}
		}
	}

	/**
	 * 添加一个消费者
	 *  
	 * @author qiuxs  
	 * @param defaultConsumerGroup
	 * @param nameSrv
	 * @param listenerProp
	 * @param topicTags
	 * @param config
	 * @throws MQClientException
	 */
	private static void addConsumer(String defaultConsumerGroup, String nameSrv, ListenerProp listenerProp, Map<String, String> topicTags, IConfiguration config) throws MQClientException {
		String consumerGroupName = defaultConsumerGroup;
		
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();
		MessageModel messageMobdel = MessageModel.CLUSTERING;
		
		if (listenerProp.isBroadcast()) {
			consumerGroupName = consumerGroupName + "B";
			messageMobdel = MessageModel.BROADCASTING;
		}
		// 消费者组名
		consumer.setConsumerGroup(consumerGroupName);
		// 消费模式
		consumer.setMessageModel(messageMobdel);
		// 设置namesrv地址
		consumer.setNamesrvAddr(nameSrv);
		
		// 从第一条开始消费
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
		
		// 最大消费次数
		consumer.setMaxReconsumeTimes(MAX_RECONSUME_TIMES);
		
		// 最大消费线程
		int consumeThreadMax = config.getInt(MqClientContants.RMQ_MAX_CONSUMER_THREAD, DEFAULT_CONSUME_THREAD_MAX);
		consumer.setConsumeThreadMax(consumeThreadMax);
		
		// 最小消费线程
		int consumeThreadMin = config.getInt(MqClientContants.RMQ_MIN_CONSUMER_THREAD, DEFAULT_CONSUME_THREAD_MIN);
		consumer.setConsumeThreadMin(consumeThreadMin);
		
		// 订阅消息
		for (Map.Entry<String, String> entry : topicTags.entrySet()) {
			consumer.subscribe(entry.getKey(), entry.getValue());
		}
		// 设置消费者实例ID
		consumer.setInstanceName(consumerGroupName + consumerInstanceId.getAndIncrement());
		
		// 设置监听器
		if (listenerProp.isOrder()) {
			consumer.registerMessageListener(new OrderlyMessageListener(listenerProp.getListenerType()));
		} else {
			consumer.registerMessageListener(new ConcurrentlyMessageListener(listenerProp.getListenerType()));
		}
		
		// 启动
		consumer.start();
		groupNameConsumerMap.put(consumerGroupName, consumer);
	}

	/**
	 * 销毁mq消费者
	 *  
	 * @author qiuxs
	 */
	public static void destory() {
		for (DefaultMQPushConsumer consumer : groupNameConsumerMap.values()) {
			consumer.shutdown();
		}
	}

}
