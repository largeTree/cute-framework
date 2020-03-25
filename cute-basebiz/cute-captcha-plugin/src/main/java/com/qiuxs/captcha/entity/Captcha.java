package com.qiuxs.captcha.entity;

import com.qiuxs.cuteframework.core.persistent.database.entity.impl.AbstractEntity;

/**
 * 验证码实体类
 *	for table cute_captcha
 * 
 * 创建时间 ：2019-03-28 22:55:09
 * @author qiuxs
 *
 */

public class Captcha extends AbstractEntity<Long> {

	private static final long serialVersionUID = 1L;

	/** 会话Key */
	private String sessionKey;

	/** ip */
	private String ip;

	/** 验证码 */
	private String captcha;

	/** 时限,秒 */
	private Long timeLimit;

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
	 * get the 时限,秒
	 * @return timeLimit
	 */
	public Long getTimeLimit() {
		return this.timeLimit;
	}

	/**
	 * set the 时限,秒
	 * @param timeLimit
	 */
	public void setTimeLimit(Long timeLimit) {
		this.timeLimit = timeLimit;
	}

	/**
	 * 检查是否过期
	 * 当前时间 - 创建时间 > 有效期 则认为过期
	 * 2019年4月9日 下午10:11:02
	 * @auther qiuxs
	 * @return
	 */
	public boolean checkExpire() {
		return System.currentTimeMillis() / 1000 - this.getCreatedTime().getTime() / 1000 > this.timeLimit;
	}

}