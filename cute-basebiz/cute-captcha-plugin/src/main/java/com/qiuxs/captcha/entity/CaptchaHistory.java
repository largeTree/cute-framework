package com.qiuxs.captcha.entity;

import java.util.Date;

import com.qiuxs.cuteframework.core.persistent.database.entity.impl.AbstractEntity;

/**
 * 验证码历史记录实体类
 *	for table cute_captcha_history
 * 
 * 创建时间 ：2019-03-28 22:55:09
 * @author qiuxs
 *
 */

public class CaptchaHistory extends AbstractEntity<Long> {

	private static final long serialVersionUID = 1L;
	
	/** 会话Key */
	private String sessionKey;

	/** ip */
	private String ip;

	/** 验证码 */
	private String captcha;

	/** 时限 */
	private Long timeLimit;

	/** 验证码创建时间 */
	private Date capCreatedTime;


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
	 * get the ip
	 * @return ip
	 */
	public String getIp() {
		return this.ip;
	}

	/**
	 * set the ip
	 * @param ip
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * get the 验证码
	 * @return captcha
	 */
	public String getCaptcha() {
		return this.captcha;
	}

	/**
	 * set the 验证码
	 * @param captcha
	 */
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	/**
	 * get the 时限
	 * @return timeLimit
	 */
	public Long getTimeLimit() {
		return this.timeLimit;
	}

	/**
	 * set the 时限
	 * @param timeLimit
	 */
	public void setTimeLimit(Long timeLimit) {
		this.timeLimit = timeLimit;
	}

	/**
	 * get the 验证码创建时间
	 * @return capCreatedTime
	 */
	public Date getCapCreatedTime() {
		return this.capCreatedTime;
	}

	/**
	 * set the 验证码创建时间
	 * @param capCreatedTime
	 */
	public void setCapCreatedTime(Date capCreatedTime) {
		this.capCreatedTime = capCreatedTime;
	}

}