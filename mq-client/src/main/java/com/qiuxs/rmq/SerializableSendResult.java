package com.qiuxs.rmq;

import java.io.Serializable;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.MessageQueue;

/**
 * 本地的事务消息发送结果
 * @author qiuxs
 *
 */
public class SerializableSendResult implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4232590421307413086L;

	/**  发送结果. */
	private SendStatus sendStatus;
	
	/**  消息ID. */
	private String msgId;
	
	/**  消息队列. */
	private MessageQueue messageQueue;
	
	/**  消息偏移量. */
	private long queueOffset;
	
	/**  事务ID. */
	private String transactionId;
	
	/**  偏移消息ID. */
	private String offsetMsgId;
	
	/**  区域ID. */
	private String regionId;
	
	/**  追踪. */
	private boolean traceOn = true;
	
	/**  生产者组名. */
	private String groupName;
	
	/**  生产者实例名. */
	private String instanceName;
	
	public SerializableSendResult() {
	}
	
	/**
	 * Instantiates a new serializable send result.
	 *
	 * @param sendResult the send result
	 */
	public SerializableSendResult(SendResult sendResult, String groupName, String instanceName) {
		this.sendStatus = sendResult.getSendStatus();
		this.msgId = sendResult.getMsgId();
		this.messageQueue = sendResult.getMessageQueue();
		this.queueOffset = sendResult.getQueueOffset();
		this.transactionId = sendResult.getTransactionId();
		this.offsetMsgId = sendResult.getOffsetMsgId();
		this.regionId = sendResult.getRegionId();
		this.traceOn = sendResult.isTraceOn();
		this.groupName = groupName;
		this.instanceName = instanceName;
	}
	
	/**
	 * 转换成Rocket的sendResult.
	 *
	 * @return the send result
	 */
	public SendResult toRocketSendResult() {
		SendResult sendResult = new SendResult();
		sendResult.setSendStatus(this.getSendStatus());
		sendResult.setMsgId(this.getMsgId());
		sendResult.setMessageQueue(this.getMessageQueue());
		sendResult.setQueueOffset(this.getQueueOffset());
		sendResult.setTransactionId(this.getTransactionId());
		sendResult.setOffsetMsgId(this.getOffsetMsgId());
		sendResult.setRegionId(this.getRegionId());
		sendResult.setTraceOn(this.isTraceOn());
		return sendResult;
	}

	/**
	 * Gets the 发送结果.
	 *
	 * @return the 发送结果
	 */
	public SendStatus getSendStatus() {
		return sendStatus;
	}

	/**
	 * Sets the 发送结果.
	 *
	 * @param sendStatus the new 发送结果
	 */
	public void setSendStatus(SendStatus sendStatus) {
		this.sendStatus = sendStatus;
	}

	/**
	 * Gets the 消息ID.
	 *
	 * @return the 消息ID
	 */
	public String getMsgId() {
		return msgId;
	}

	/**
	 * Sets the 消息ID.
	 *
	 * @param msgId the new 消息ID
	 */
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	/**
	 * Gets the 消息队列.
	 *
	 * @return the 消息队列
	 */
	public MessageQueue getMessageQueue() {
		return messageQueue;
	}

	/**
	 * Sets the 消息队列.
	 *
	 * @param messageQueue the new 消息队列
	 */
	public void setMessageQueue(MessageQueue messageQueue) {
		this.messageQueue = messageQueue;
	}

	/**
	 * Gets the 消息偏移量.
	 *
	 * @return the 消息偏移量
	 */
	public long getQueueOffset() {
		return queueOffset;
	}

	/**
	 * Sets the 消息偏移量.
	 *
	 * @param queueOffset the new 消息偏移量
	 */
	public void setQueueOffset(long queueOffset) {
		this.queueOffset = queueOffset;
	}

	/**
	 * Gets the 事务ID.
	 *
	 * @return the 事务ID
	 */
	public String getTransactionId() {
		return transactionId;
	}

	/**
	 * Sets the 事务ID.
	 *
	 * @param transactionId the new 事务ID
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * Gets the 偏移消息ID.
	 *
	 * @return the 偏移消息ID
	 */
	public String getOffsetMsgId() {
		return offsetMsgId;
	}

	/**
	 * Sets the 偏移消息ID.
	 *
	 * @param offsetMsgId the new 偏移消息ID
	 */
	public void setOffsetMsgId(String offsetMsgId) {
		this.offsetMsgId = offsetMsgId;
	}

	/**
	 * Gets the 区域ID.
	 *
	 * @return the 区域ID
	 */
	public String getRegionId() {
		return regionId;
	}

	/**
	 * Sets the 区域ID.
	 *
	 * @param regionId the new 区域ID
	 */
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	/**
	 * Checks if is 追踪.
	 *
	 * @return the 追踪
	 */
	public boolean isTraceOn() {
		return traceOn;
	}

	/**
	 * Sets the 追踪.
	 *
	 * @param traceOn the new 追踪
	 */
	public void setTraceOn(boolean traceOn) {
		this.traceOn = traceOn;
	}

	/**
	 * Gets the 生产者组名.
	 *
	 * @return the 生产者组名
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * Sets the 生产者组名.
	 *
	 * @param groupName the new 生产者组名
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * Gets the 生产者实例名.
	 *
	 * @return the 生产者实例名
	 */
	public String getInstanceName() {
		return instanceName;
	}

	/**
	 * Sets the 生产者实例名.
	 *
	 * @param instanceName the new 生产者实例名
	 */
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}
}
