package com.qiuxs.captcha.entity;

import com.qiuxs.cuteframework.core.persistent.database.entity.impl.AbstractEntity;

/**
 * 实体类
 *	for table cute_captcha_blacklist
 * 
 * 创建时间 ：2019-03-28 22:55:09
 * @author qiuxs
 *
 */

public class CaptchaBlacklist extends AbstractEntity<Long> {

	private static final long serialVersionUID = 1L;

	/** 会话Key */
	private String sessionKey;

	/** 时限,秒,-1代表永久 */
	private Long timeLimit;

	/** 拉黑原因 */
	private String reason;

	/**
	 * get the 会话Key
	 * @return sessionKey
	 */
	public String getSessionKey() {
		return this.sessionKey;
	}

	/**
	 * set the 会话Key
	 * @param sessionKey
	 */
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	/**
	 * get the 时限,秒,-1代表永久
	 * @return timeLimit
	 */
	public Long getTimeLimit() {
		return this.timeLimit;
	}

	/**
	 * set the 时限,秒,-1代表永久
	 * @param timeLimit
	 */
	public void setTimeLimit(Long timeLimit) {
		this.timeLimit = timeLimit;
	}

	/**
	 * get the 拉黑原因
	 * @return reason
	 */
	public String getReason() {
		return this.reason;
	}

	/**
	 * set the 拉黑原因
	 * @param reason
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

}