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
