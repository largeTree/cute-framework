package com.qiuxs.rmq.conf;

import java.io.Serializable;

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

	public ListenerProp() {}
	
	public ListenerProp(String topic, String tags, String bean, String method) {
		super();
		this.topic = topic;
		this.tags = tags;
		this.bean = bean;
		this.method = method;
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

}
