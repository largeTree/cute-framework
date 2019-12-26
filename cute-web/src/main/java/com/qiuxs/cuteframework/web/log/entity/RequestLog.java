package com.qiuxs.cuteframework.web.log.entity;

import java.util.Date;

import com.qiuxs.cuteframework.core.basic.code.annotation.Code;
import com.qiuxs.cuteframework.core.basic.code.annotation.CodeDomain;
import com.qiuxs.cuteframework.core.persistent.database.entity.impl.AbstractEntity;

/**
 * 请求日志记录实体类
 *	for table api_request_log
 * 
 * 创建时间 ：2019-05-30 22:26:46
 * @author qiuxs
 *
 */

public class RequestLog extends AbstractEntity<Long> {

	private static final long serialVersionUID = 1L;
	
	@CodeDomain
	public static final String DOMAIN_STATUS = "domain.ApiStatus";
	@Code(domain = DOMAIN_STATUS, caption = "成功")
	public static final int SUCCESS = 1;
	@Code(domain = DOMAIN_STATUS, caption = "失败")
	public static final int FAILED = 0;
	
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
	
	/** 请求状态 */
	private Integer status;
	
	/** 全局流水号 */
	private Long globalId;

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

	/**
	 * get the 请求状态
	 * 
	 * 2019年6月11日 下午9:14:41
	 * @auther qiuxs
	 * @return
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * set the 请求状态
	 * 
	 * 2019年6月11日 下午9:14:58
	 * @auther qiuxs
	 * @param status
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * get the 全局流水号
	 * 
	 * 2019年8月2日 下午10:39:41
	 * @auther qiuxs
	 * @return
	 */
	public Long getGlobalId() {
		return globalId;
	}

	/**
	 * set the 全局流水号
	 * 
	 * 2019年8月2日 下午10:39:41
	 * @auther qiuxs
	 * @param status
	 */
	public void setGlobalId(Long globalId) {
		this.globalId = globalId;
	}
	
}