package com.qiuxs.rmq.log.entity;

import java.util.Date;

import com.qiuxs.cuteframework.core.persistent.database.entity.impl.AbstractEntity;

/**
 * MQ消息消费失败记录实体类
 *	for table mq_failed
 * 
 * 创建时间 ：2020-03-30 14:25:45
 * @author qiuxs
 *
 */

public class MqFailed extends AbstractEntity<Long> {

	private static final long serialVersionUID = 1L;
	
	/** 消息ID */
	private String msgId;

	/** 消息主题 */
	private String topic;

	/** 消息标签 */
	private String tags;

	/** 消息业务主键 */
	private String bizKey;

	/** 业务数据 */
	private String bizBody;

	/** 调用栈 */
	private String stacktrace;

	/** 扩展属性 */
	private String extProp;

	/** 重新消费次数 */
	private Integer reconsumeTimes;

	/** 消息生成时间 */
	private Date bornTime;

	/** 服务器 */
	private String serverId;

	/** 用户ID */
	private Long userId;

	/** 单元ID */
	private Long unitId;

	/** GlobalId */
	private Long globalId;

	/** 新增时间 */
	private Date createdDate;

	/** 修改时间 */
	private Date updatedDate;


	/**
	 * get the 消息ID
	 * @return msgId
	 */
	public String getMsgId() {
		return this.msgId;
	}

	/**
	 * set the 消息ID
	 * @param msgId
	 */
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	/**
	 * get the 消息主题
	 * @return topic
	 */
	public String getTopic() {
		return this.topic;
	}

	/**
	 * set the 消息主题
	 * @param topic
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}

	/**
	 * get the 消息标签
	 * @return tags
	 */
	public String getTags() {
		return this.tags;
	}

	/**
	 * set the 消息标签
	 * @param tags
	 */
	public void setTags(String tags) {
		this.tags = tags;
	}

	/**
	 * get the 消息业务主键
	 * @return bizKey
	 */
	public String getBizKey() {
		return this.bizKey;
	}

	/**
	 * set the 消息业务主键
	 * @param bizKey
	 */
	public void setBizKey(String bizKey) {
		this.bizKey = bizKey;
	}

	/**
	 * get the 业务数据
	 * @return bizBody
	 */
	public String getBizBody() {
		return this.bizBody;
	}

	/**
	 * set the 业务数据
	 * @param bizBody
	 */
	public void setBizBody(String bizBody) {
		this.bizBody = bizBody;
	}

	/**
	 * get the 调用栈
	 * @return stacktrace
	 */
	public String getStacktrace() {
		return this.stacktrace;
	}

	/**
	 * set the 调用栈
	 * @param stacktrace
	 */
	public void setStacktrace(String stacktrace) {
		this.stacktrace = stacktrace;
	}

	/**
	 * get the 扩展属性
	 * @return extProp
	 */
	public String getExtProp() {
		return this.extProp;
	}

	/**
	 * set the 扩展属性
	 * @param extProp
	 */
	public void setExtProp(String extProp) {
		this.extProp = extProp;
	}

	/**
	 * get the 重新消费次数
	 * @return reconsumeTimes
	 */
	public Integer getReconsumeTimes() {
		return this.reconsumeTimes;
	}

	/**
	 * set the 重新消费次数
	 * @param reconsumeTimes
	 */
	public void setReconsumeTimes(Integer reconsumeTimes) {
		this.reconsumeTimes = reconsumeTimes;
	}

	/**
	 * get the 消息生成时间
	 * @return bornTime
	 */
	public Date getBornTime() {
		return this.bornTime;
	}

	/**
	 * set the 消息生成时间
	 * @param bornTime
	 */
	public void setBornTime(Date bornTime) {
		this.bornTime = bornTime;
	}

	/**
	 * get the 服务器
	 * @return serverId
	 */
	public String getServerId() {
		return this.serverId;
	}

	/**
	 * set the 服务器
	 * @param serverId
	 */
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	/**
	 * get the 用户ID
	 * @return userId
	 */
	public Long getUserId() {
		return this.userId;
	}

	/**
	 * set the 用户ID
	 * @param userId
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * get the 单元ID
	 * @return unitId
	 */
	public Long getUnitId() {
		return this.unitId;
	}

	/**
	 * set the 单元ID
	 * @param unitId
	 */
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	/**
	 * get the GlobalId
	 * @return globalId
	 */
	public Long getGlobalId() {
		return this.globalId;
	}

	/**
	 * set the GlobalId
	 * @param globalId
	 */
	public void setGlobalId(Long globalId) {
		this.globalId = globalId;
	}

	/**
	 * get the 新增时间
	 * @return createdDate
	 */
	public Date getCreatedDate() {
		return this.createdDate;
	}

	/**
	 * set the 新增时间
	 * @param createdDate
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * get the 修改时间
	 * @return updatedDate
	 */
	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	/**
	 * set the 修改时间
	 * @param updatedDate
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

}