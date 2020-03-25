package com.qiuxs.rmq;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.basic.config.IConfiguration;
import com.qiuxs.cuteframework.core.basic.config.UConfigUtils;
import com.qiuxs.cuteframework.core.basic.constants.SymbolConstants;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.basic.utils.io.SerializeUtil;
import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.cuteframework.tech.microsvc.MicroSvcContext;
import com.qiuxs.cuteframework.tech.microsvc.log.ApiLogConstants;
import com.qiuxs.cuteframework.tech.microsvc.log.ApiLogProp;
import com.qiuxs.cuteframework.tech.microsvc.log.ApiLogUtils;
import com.qiuxs.rmq.microsvc.MqMicroSvcContext;

/**
 * 
 * 功能描述: mq生产者工具 <p>  
 * 新增原因: TODO<p>  
 * 新增日期: 2020年3月24日 下午6:24:45 <p>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class ProducerUtils {

	private static Logger log = LogManager.getLogger(ProducerUtils.class);

	private static AtomicInteger producerId = new AtomicInteger(1);
	/** 默认生产者组名 */
	public static String defaultProducerGroupName;
	/** 默认生产者对象 */
	private static DefaultMQProducer defaultProducer;
	/** 组名与生产者Map。key=生产者组名 */
	private static Map<String, DefaultMQProducer> groupNameProducerMap = new ConcurrentHashMap<>();
	/** 默认发送超时时间 */
	private static long sendMsgTimeout = 3000;
	/** 默认消息队列选择器。用于发送顺序消息 */
	private static MessageQueueSelector defaultQueueSelector;
	
	/**
	 * 
	 * 使用默认生产者发送简单消息。
	 * 发送成功时返回消息ID。发送失败时将抛出异常。注意发送消息成功只是指发送消息到消息服务器的Master成功，就算没有成功刷盘、复制到从服务器也算成功。
	 * @param topic	主题
	 * @param tags 消息tag。用于标识一种消息。
	 * @param bizKeys 业务主键。根据tags和keys必须能唯一确定一条消息。
	 * @param body 需要发送的数据对象。注意该对象类型在消费端必须存在。
	 * @return 消息服务器生成的消息ID
	 */
	public static String send(String topic, String tags, String bizKeys, Object body) {
		return send(null, topic, tags, bizKeys, body, null, sendMsgTimeout, null).getMsgId();
	}

	/**
	 * 使用默认生产者发送消息。
	 * 发送成功时返回消息ID。发送失败时将抛出异常。注意发送消息成功只是指发送消息到消息服务器的Master成功，就算没有成功刷盘、复制到从服务器也算成功。
	 * @param topic	主题
	 * @param tags 消息tag。用于标识一种消息。
	 * @param bizKeys 业务主键。根据tags和keys必须能唯一确定一条消息。
	 * @param body 需要发送的数据对象。注意该对象类型在消费端必须存在。
	 * @param extProp 需要发送的附加属性，为null时不添加附加属性
	 * @return 消息服务器生成的消息ID
	 */
	public static String send(String topic, String tags, String bizKeys, Object body, Map<String, String> extProp) {
		return send(null, topic, tags, bizKeys, body, null, sendMsgTimeout, extProp).getMsgId();
	}

	/**
	 * 发送顺序消息。顺序消息是指消费时必须按发送时的顺序消费的消息。
	 * @param topic	主题
	 * @param tags 消息tag。用于标识一种消息。
	 * @param bizKeys 业务主键。根据tags和keys必须能唯一确定一条消息。
	 * @param body 需要发送的数据对象。注意该对象类型在消费端必须存在。
	 * @param orderId 消息顺序号。相同顺序ID的消息将是有序的。
	 * @return 消息服务器生成的消息ID
	 */
	public static String send(String topic, String tags, String bizKeys, Object body, Integer orderId) {
		return send(null, topic, tags, bizKeys, body, orderId, sendMsgTimeout, null).getMsgId();
	}

	/**
	 * 使用默认生产者发送延迟消息。
	 * 发送成功时返回消息ID。发送失败时将抛出异常。注意发送消息成功只是指发送消息到消息服务器的Master成功，就算没有成功刷盘、复制到从服务器也算成功。
	 * @param topic	主题
	 * @param tags 消息tag。用于标识一种消息。
	 * @param bizKeys 业务主键。根据tags和keys必须能唯一确定一条消息。
	 * @param body 需要发送的数据对象。注意该对象类型在消费端必须存在。
	 * @param extProp 需要发送的附加属性，为null时不添加附加属性
	 * @param delayLevel 延迟发送级别。从0级开始，对应的延迟时间=0s 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
	 * @return 消息服务器生成的消息ID
	 */
	public static String send(String topic, String tags, String bizKeys, Object body, Map<String, String> extProp, int delayLevel) {
		return send(null, topic, tags, bizKeys, body, true, null, sendMsgTimeout, extProp, true, delayLevel).getMsgId();
	}

	/**
	 *	发送字节数组消息体。发送时，将不序列化消息体。
	 *	可以通过参数serialBody指定消费者收到消息时，是否需要序列化消息体。
	 * @param topic	主题
	 * @param tags 消息tag。用于标识一种消息。
	 * @param bizKeys 业务主键。根据tags和keys必须能唯一确定一条消息。
	 * @param body 需要发送的数据。
	 * @param orderId 消息顺序号。为null时发送无序消息。不为null时，发送有序消息，相同顺序ID的消息将是有序的。
	 * @param bodyIsSerialed 消息体是否为序列化对象。true 消息体为序列化对象。消费者收到消息后，将执行反序列化操作。
	 * 			false 消息体不是序列化后的字节数组。消费者收到消息后，不执行反序列化操作。
	 * @param extProp 需要发送的附加属性，为null时不添加附加属性
	 * @return 消息服务器生成的消息ID
	 */
	public static String send(String topic, String tags, String bizKeys, byte[] body, Integer orderId,
			boolean bodyIsSerialed, Map<String, String> extProp) {
		return send(null, topic, tags, bizKeys, body, false, orderId, sendMsgTimeout, extProp, bodyIsSerialed, 0).getMsgId();
	}

	/**
	 * 相对完整的发送同步消息方法。
	 * @param groupName 生产者组名
	 * @param topic	主题
	 * @param tags 消息tag。用于标识一种消息。
	 * @param bizKeys 业务主键。根据tags和keys必须能唯一确定一条消息。
	 * @param body 需要发送的数据对象。注意该对象类型在消费端必须存在。
	 * @param orderId 消息顺序号。为null时发送无序消息。不为null时，发送有序消息，相同顺序ID的消息将是有序的。
	 * @param timeout 超时时间。为-1时，使用默认值
	 * @param extProp 需要发送的附加属性，为null时不添加附加属性
	 * @return RocketMQ消息发送结果对象
	 * @throws InterruptedException 
	 * @throws MQBrokerException 
	 * @throws RemotingException 
	 * @throws MQClientException 
	 */
	public static SendResult send(String groupName, String topic, String tags, String bizKeys, Object body, 
			Integer orderId, long timeout, Map<String, String> extProp) {
		return send(groupName, topic, tags, bizKeys, body, true, orderId, timeout, extProp, true, 0);
	}
	
	/**
	 * 发送同步消息方法。
	 * @param groupName 生产者组名。为null时，使用默认生产者
	 * @param topic	主题
	 * @param tags 消息tag。用于标识一种消息。
	 * @param bizKeys 业务主键。根据tags和keys必须能唯一确定一条消息。
	 * @param body 需要发送的数据对象。注意该对象类型在消费端必须存在。
	 * @param serialBody 是否需要序列化body。不序列化时，消息体body必须是字节数组。
	 * @param orderId 消息顺序号。为null时发送无序消息。不为null时，发送有序消息，相同顺序ID的消息将是有序的。
	 * @param timeout 超时时间。为-1时，使用默认值
	 * @param extProp 需要发送的附加属性，为null时不添加附加属性
	 * @param bodyIsSerialed 消息体是否为序列化对象，用于通知消费者是否需要执行反序列化操作。true 消息体为序列化对象，消费者收到消息后，将执行反序列化操作。
	 * 			false 消息体不是序列化后的字节数组，消费者收到消息后，不执行反序列化操作。
	 * @param delayLevel 延迟发送级别。从0级开始，对应的延迟时间=0s 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
	 * @return RocketMQ消息发送结果对象
	 * @throws InterruptedException 
	 * @throws MQBrokerException 
	 * @throws RemotingException 
	 * @throws MQClientException 
	 */
	public static SendResult send(String groupName, String topic, String tags, String bizKeys, Object body,
			boolean serialBody, Integer orderId, long timeout, Map<String, String> extProp,
			boolean bodyIsSerialed, int delayLevel) {
		try {
			return sendThrowException(groupName, topic, tags, bizKeys, body, serialBody, orderId, timeout, extProp, bodyIsSerialed, delayLevel);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static SendResult sendThrowException(String groupName, String topic, String tags, String bizKeys, Object body,
			boolean serialBody, Integer orderId, long timeout, Map<String, String> extProp,
			boolean bodyIsSerialed, int delayLevel) throws MQClientException, RemotingException, MQBrokerException  {
		//生成Rocketmq Message对象发送
		//根据serialBody参数，确定是否要序列化body。不序列化时，需保证body对象为已序列化的byte[]类型。
		byte[] msgData = serialBody ? SerializeUtil.serial(body) : (byte[])body;
        Message msg = new Message(topic, tags, bizKeys, msgData);
        msg.setDelayTimeLevel(delayLevel);
        
        //如果指定了附加属性，将附加属性添加到消息中
        putExtProps(msg, extProp);
        
        //增加用户数据到附加信息中，以传送当前用户信息给消费者
        putSendToInvokedProp(msg);
        
        //增加传送到被调用者端的日志附加数据。
        putSendToInvokedLogProp(msg, body);
        
        //没有设置有效超时时间时，使用默认设置
        if(timeout < 0) {
        	timeout = sendMsgTimeout;
        }
        
        //使用组名对应的生产者发送消息
		DefaultMQProducer producer = getProducer(groupName);
		if(producer == null) {
			ExceptionUtils.throwLogicalException("mqClient_config_producer_groupName_notexists", groupName);
		}
		
		try {
			if(orderId == null) {
		        return producer.send(msg, timeout);
			}
			else {
		        return producer.send(msg, defaultQueueSelector, orderId, timeout);
			}
		} catch(InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 消息属性中附加需要传送到被调用者的的日志属性数据
	 * @author qiuxs  
	 * @param msg 消息对象
	 */
	private static void putSendToInvokedLogProp(Message msg, Object body) {
        //生成传送到被调用者端的日志附加数据。
        ApiLogProp logProp = ApiLogUtils.genSendToInvokedLogProp();
        logProp.setToApp("_mq_namesrv"); //发到mq服务器
        ApiLogUtils.writeReqLog(logProp, msg.getTopic(),
        		StringUtils.substringBefore(msg.getTags(), SymbolConstants.SEPARATOR_DOT), body, ApiLogConstants.TYPE_REQUEST_MQ);
       	msg.putUserProperty(ApiLogConstants.ATTACH_KEY_REQ_PROP, JSONObject.toJSONString(logProp));
	}
	
	/**
	 * 消息属性中附加需要传送到被调用者的数据
	 * 1. 传送用户上下文属性
	 * 2. 传送transId
	 * @author qiuxs  
	 * @param msg 消息对象
	 */
	private static void putSendToInvokedProp(Message msg) {
    	//添加上下文环境到目标端
    	MicroSvcContext ctx = new MqMicroSvcContext(msg);
    	ctx.putMicroContext();
	}
	
	/**
	 * 根据组名称取生产者。
	 * @param groupName 生产者组。为null时，返回默认生产者
	 * @return MQ生产者。生产者不存在时，将返回null。
	 */
	public static DefaultMQProducer getProducer(String groupName){
		DefaultMQProducer producer = groupName == null ? defaultProducer : groupNameProducerMap.get(groupName);
		return producer;
	}
	
	/**
	 * 把附加属性添加到消息中，以发送给消费者。
	 * 为防止覆盖MQ预定义属性，排除掉附加属性中的MQ预定义属性
	 * @author lsh  
	 * @param msg 消息
	 * @param extProp 附加属性
	 */
	private static void putExtProps(Message msg, Map<String, String> extProp) {
        if(extProp != null) {
        	Map<String, String> props = msg.getProperties();
        	for(Map.Entry<String, String> e : extProp.entrySet()) {
                if (!MessageConst.STRING_HASH_SET.contains(e.getKey())) { //仅添加不在预定义属性中的属性
                	//为提升效率，直接调用属性Map原生方法，不调用msg.putUserProperty
                	props.put(e.getKey(), e.getValue());
                }
        	}
        }
	}
	
	/**
	 * 初始化生产者
	 *  
	 * @author qiuxs
	 */
	public static void init() {
		String appName = EnvironmentContext.getAppName();
		log.info("[" + appName + "]ProducerUtils init started ...");

		IConfiguration config = UConfigUtils.getDomain(MqClientContants.CONFIG_DOMAIN);
		if (config != null) {
			// 配置了生产者组名的自动初始化生产者
			String producerGroupName = config.getString(MqClientContants.PRODUCER_GROUP_NAME);
			if (StringUtils.isNotBlank(producerGroupName)) {
				// 超时时间
				sendMsgTimeout = config.getLongValue(MqClientContants.RMQ_SEND_TIMEOUT, sendMsgTimeout);
				// nameserver
				String nameSrv = config.getString(MqClientContants.RMQ_NAME_SRV);
				try {
					defaultProducer = addProducer(producerGroupName, nameSrv);
					defaultProducerGroupName = producerGroupName;
				} catch (Throwable e) {
					log.error("Start Rmq Producer failed, group = " + producerGroupName + ", ext = " + e.getLocalizedMessage(), e);
				}
			}
		}
		
		//生成默认消息队列选择器。根据传入的顺序号与消息队列数求模选择队列。用于发送顺序消息。
		defaultQueueSelector = new MessageQueueSelector() {
            @Override
            public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                Integer orderIdArg = (Integer) arg;
                int index = orderIdArg % mqs.size();
                return mqs.get(index);
            }
		};
	}

	private static DefaultMQProducer addProducer(String producerGroupName, String nameSrv) throws MQClientException {
		DefaultMQProducer producer = new DefaultMQProducer(producerGroupName);
		producer.setNamesrvAddr(nameSrv);
		producer.setInstanceName(producerGroupName + "-" + producerId.getAndIncrement());
		producer.start();
		groupNameProducerMap.put(producerGroupName, producer);
		return producer;
	}

}
