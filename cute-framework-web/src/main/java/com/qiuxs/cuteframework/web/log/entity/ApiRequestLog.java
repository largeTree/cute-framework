package com.qiuxs.cuteframework.web.log.entity;

import java.util.Date;

import com.qiuxs.cuteframework.core.persistent.database.entity.impl.AbstractEntity;

/**
 * 请求日志记录实体类
 *	for table api_request_log
 * 
 * 创建时间 ：2019-05-30 22:26:46
 * @author qiuxs
 *
 */

public class ApiRequestLog extends AbstractEntity<Long> {

	private static final long serialVersionUID = 1L;
	
	/** 接口号 */
	private String apiKey;

	/** 服务器id */
	private String serverId;

	/** 请求ip */
	private String reqIp;

	/** requestUrl */
	private String reqUrl;

	/** 请求开始时间 */
	private Date reqStartTime;

	/** 请求结束时间 */
	private Date reqEndTime;


	/**
	 * get the 接口号
	 * @return apiKey
	 */
	public String getApiKey() {
		return this.apiKey;
	}

	/**
	 * set the 接口号
	 * @param apiKey
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * get the 服务器id
	 * @return serverId
	 */
	public String getServerId() {
		return this.serverId;
	}

	/**
	 * set the 服务器id
	 * @param serverId
	 */
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	/**
	 * get the 请求ip
	 * @return reqIp
	 */
	public String getReqIp() {
		return this.reqIp;
	}

	/**
	 * set the 请求ip
	 * @param reqIp
	 */
	public void setReqIp(String reqIp) {
		this.reqIp = reqIp;
	}

	/**
	 * get the requestUrl
	 * @return reqUrl
	 */
	public String getReqUrl() {
		return this.reqUrl;
	}

	/**
	 * set the requestUrl
	 * @param reqUrl
	 */
	public void setReqUrl(String reqUrl) {
		this.reqUrl = reqUrl;
	}

	/**
	 * get the 请求开始时间
	 * @return reqStartTime
	 */
	public Date getReqStartTime() {
		return this.reqStartTime;
	}

	/**
	 * set the 请求开始时间
	 * @param reqStartTime
	 */
	public void setReqStartTime(Date reqStartTime) {
		this.reqStartTime = reqStartTime;
	}

	/**
	 * get the 请求结束时间
	 * @return reqEndTime
	 */
	public Date getReqEndTime() {
		return this.reqEndTime;
	}

	/**
	 * set the 请求结束时间
	 * @param reqEndTime
	 */
	public void setReqEndTime(Date reqEndTime) {
		this.reqEndTime = reqEndTime;
	}

}