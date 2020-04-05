package com.qiuxs.rmq;

public class MqClientContants {
	
	/** rmq配置域 */
	public static final String CONFIG_DOMAIN = "rmq";
	
	/** 生产者组名配置 */
	public static final String PRODUCER_GROUP_NAME = "mq_producer_group";
	/** 消费者组名 */
	public static final String CONSUMER_GROUP_NAME = "mq_consumer_group";
	/** rocketmq nameserver地址 */
	public static final String RMQ_NAME_SRV = "rmq_name_srv";
	/** 发送超时时间 */
	public static final String RMQ_SEND_TIMEOUT = "rmq_send_time";
	/** 最大消费线程 */
	public static final String RMQ_MAX_CONSUMER_THREAD = "rmq_max_consumer_thread";
	/** 最小消费线程 */
	public static final String RMQ_MIN_CONSUMER_THREAD = "rmq_min_consumer_thread";
	
	/** 普通消费者 */
	public static final int LISTENER_TYPE_NORMAL = 1;
	/** 广播消费者 */
	public static final int LISTENER_TYPE_B = 2;
	/** 顺序消息 */
	public static final int LISTENER_TYPE_O = 3;
	
	/** 消息体json类型 */
	public static final String MSG_PROP_BODY_JSON_TO_CLASS = "mqBodyJsonToClassProp";
	/** 消息消费重试次数 */
	public static final String MSG_PROP_RECONSUME_TIMES = "mqReconsumeTimes";
	/** 消息产生时间 */
	public static final String MSG_PROP_BORN_TIMEstamp = "mqMessageBornTimestamp";
	/** mq子事务ID */
	public static final String MSG_PROP_SUB_TX_ID = "mqSubTxId";
	
}
