package com.qiuxs.rmq.listener;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.rocketmq.common.message.MessageExt;

import com.qiuxs.cuteframework.core.basic.utils.JsonUtils;
import com.qiuxs.cuteframework.core.basic.utils.MapUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.basic.utils.io.SerializeUtil;
import com.qiuxs.cuteframework.core.basic.utils.reflect.MethodUtils;
import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;
import com.qiuxs.cuteframework.core.context.TLVariableHolder;
import com.qiuxs.cuteframework.tech.microsvc.MicroSvcContext;
import com.qiuxs.cuteframework.tech.microsvc.log.ApiLogConstants;
import com.qiuxs.cuteframework.tech.microsvc.log.ApiLogProp;
import com.qiuxs.cuteframework.tech.microsvc.log.ApiLogUtils;
import com.qiuxs.rmq.MqClientContants;
import com.qiuxs.rmq.conf.ListenerProp;
import com.qiuxs.rmq.conf.MQConfig;
import com.qiuxs.rmq.log.utils.MqLogUtils;
import com.qiuxs.rmq.microsvc.MqMicroSvcContext;

public abstract class MessageListener {

	private static final Logger log = LogManager.getLogger(MessageListener.class);
	
	private static final int MAX_FAILED_COUNT = 3;
	
	private int listenerType;

	public MessageListener(int listenerType) {
		this.listenerType = listenerType;
	}

	protected boolean invokeListeners(List<MessageExt> msgs, boolean isOrder) {
		boolean res = false;
		for (MessageExt msg : msgs) {
			ApiLogProp logProp = null;
			Object body = null;
			String answerId = ApiLogUtils.genRandomAnswerId();
			try {
				String topic = msg.getTopic();
				String tags = msg.getTags();

				// 缓存上下文
				MicroSvcContext microSvcContext = new MqMicroSvcContext(msg);
				microSvcContext.cacheContextFromSource();

				Map<String, String> extProp = new HashMap<>(msg.getProperties());

				// 填充扩展数据
				putExtProps(extProp, msg);

				byte[] bodyByte = msg.getBody();

				// 消息反序列化
				body = SerializeUtil.unserial(bodyByte);

				// 写入调用日志
				logProp = writeReqApiLog(msg, body, extProp);

				// 执行监听器
				invokeListener(topic, tags, msg.getKeys(), body, extProp, logProp);

				// 写入响应日志
				ApiLogUtils.writeResLog(logProp, answerId, null, null, ApiLogConstants.TYPE_ANSWER_MQ);

				// 删除调用失败的日志
				deleteMqFailed(msg);
				res = true;
			} catch (Throwable e) {
				log.error("消费失败：" + msg.getMsgId() + ", topic = " + msg.getTopic() + ", tags = " + msg.getTags() + ", ext = " + e.getLocalizedMessage(), e);
				// 记录失败日志
				ApiLogUtils.writeResLog(logProp, answerId, null, e, ApiLogConstants.TYPE_ANSWER_MQ);
				// 允许失败后重试几次后再记录消费失败
				if (msg.getReconsumeTimes() >= MAX_FAILED_COUNT) {
					// 记录mq消费失败详情
					writeMqFailed(msg, body, e);
				}
			} finally {
				TLVariableHolder.clear();
			}
		}
		return res;
	}

	/**
	 * 记录mq消费失败详细信息
	 *  
	 * @author qiuxs  
	 * @param msg
	 * @param body
	 * @param e
	 */
	private void writeMqFailed(MessageExt msg, Object body, Throwable e) {
		MqLogUtils.saveMqFailed(msg, body, e);
	}
	
	/**
	 * 删除mq消费失败日志
	 *  
	 * @author qiuxs  
	 * @param msg
	 */
	private void deleteMqFailed(MessageExt msg) {
		MqLogUtils.deleteMqFailed(msg);
	}

	private void invokeListener(String topic, String tags, String bizKey, Object body, Map<String, String> extProp, ApiLogProp logProp) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Map<String, ListenerProp> listeners = MQConfig.getListener(listenerType);
		ListenerProp listenerProp = listeners.get(topic + "." + tags);
		Object listener = ApplicationContextHolder.getBean(listenerProp.getBean());
		MethodUtils.invokeMethod(listener, listenerProp.getMethod(), new Class[] { String.class, String.class, String.class, Object.class, Map.class }, new Object[] { topic, tags, bizKey, body, extProp });
	}

	/**
	 * 写入调用日志
	 *  
	 * @author qiuxs  
	 * @param msg
	 * @param body
	 * @param extProp
	 * @return 
	 */
	private ApiLogProp writeReqApiLog(MessageExt msg, Object body, Map<String, String> extProp) {
		log.info("消费消息：" + msg.getMsgId() + ", topic = " + msg.getTopic() + ", tags = " + msg.getTags());
		String strApiLogProp = MapUtils.getString(extProp, ApiLogConstants.ATTACH_KEY_REQ_PROP);
		if (StringUtils.isNotBlank(strApiLogProp)) {
			ApiLogProp apiLogProp = JsonUtils.parseObject(strApiLogProp, ApiLogProp.class);
			ApiLogUtils.initApiLog(apiLogProp);
			return apiLogProp;
		}
		return null;
	}

	/**
	 * 填充扩展数据
	 *  
	 * @author qiuxs  
	 * @param extProp
	 * @param msg
	 */
	private void putExtProps(Map<String, String> extProp, MessageExt msg) {
		extProp.put(MqClientContants.MSG_PROP_RECONSUME_TIMES, String.valueOf(msg.getReconsumeTimes()));
		extProp.put(MqClientContants.MSG_PROP_BORN_TIMEstamp, String.valueOf(msg.getBornTimestamp()));
	}

}
