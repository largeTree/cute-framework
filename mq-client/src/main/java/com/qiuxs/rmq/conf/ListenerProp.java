package com.qiuxs.rmq.conf;

import java.io.Serializable;

import com.qiuxs.rmq.MqClientContants;

/**
 * The Class ListenerProp.
 */
public class ListenerProp implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3999152391593447417L;

	/**  主题. */
	private String topic;
	
	/**  标签. */
	private String tags;
	
	/**  消费springBeanName. */
	private String bean;
	
	/**  消费方法名. */
	private String method;
	
	/** 是否是广播消息. */
	private boolean broadcast;
	
	/**  是否是顺序消费. */
	private boolean order;
	
	/**  消费者类型. */
	private int listenerType;

	public ListenerProp() {}
	
	public ListenerProp(String topic, String tags, String bean, String method, int listenerType) {
		super();
		this.topic = topic;
		this.tags = tags;
		this.bean = bean;
		this.method = method;
		this.listenerType = listenerType;
		if (listenerType == MqClientContants.LISTENER_TYPE_B) {
			setBroadcast(true);
		}
		if (listenerType == MqClientContants.LISTENER_TYPE_O) {
			setOrder(true);
		}
	}

	/**
	 * Gets the 主题.
	 *
	 * @return the 主题
	 */
	public String getTopic() {
		return topic;
	}

	/**
	 * Sets the 主题.
	 *
	 * @param topic the new 主题
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}

	/**
	 * Gets the 标签.
	 *
	 * @return the 标签
	 */
	public String getTags() {
		return tags;
	}

	/**
	 * Sets the 标签.
	 *
	 * @param tags the new 标签
	 */
	public void setTags(String tags) {
		this.tags = tags;
	}

	/**
	 * Gets the 消费springBeanName.
	 *
	 * @return the 消费springBeanName
	 */
	public String getBean() {
		return bean;
	}

	/**
	 * Sets the 消费springBeanName.
	 *
	 * @param bean the new 消费springBeanName
	 */
	public void setBean(String bean) {
		this.bean = bean;
	}

	/**
	 * Gets the 消费方法名.
	 *
	 * @return the 消费方法名
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * Sets the 消费方法名.
	 *
	 * @param method the new 消费方法名
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * Checks if is 是否是广播消息.
	 *
	 * @return the 是否是广播消息
	 */
	public boolean isBroadcast() {
		return broadcast;
	}

	/**
	 * Sets the 是否是广播消息.
	 *
	 * @param broadcast the new 是否是广播消息
	 */
	public void setBroadcast(boolean broadcast) {
		this.broadcast = broadcast;
	}

	/**
	 * Checks if is 是否是顺序消费.
	 *
	 * @return the 是否是顺序消费
	 */
	public boolean isOrder() {
		return order;
	}

	/**
	 * Sets the 是否是顺序消费.
	 *
	 * @param order the new 是否是顺序消费
	 */
	public void setOrder(boolean order) {
		this.order = order;
	}

	/**
	 * Gets the 消费者类型.
	 *
	 * @return the 消费者类型
	 */
	public int getListenerType() {
		return listenerType;
	}

	/**
	 * Sets the 消费者类型.
	 *
	 * @param listenerType the new 消费者类型
	 */
	public void setListenerType(int listenerType) {
		this.listenerType = listenerType;
	}

}
