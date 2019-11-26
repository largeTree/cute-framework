/*
 * 
 */
package com.qiuxs.cuteframework.core.basic.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * 功能描述: 简单会话持有对象<br/>
 * 新增原因: TODO<br/>
 * 新增日期: 2018年4月23日 下午10:28:06 <br/>.
 *
 * @author qiuxs
 * @version 1.0.0
 */
public class UserLite implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3008176462742444808L;

	/**  当前用户ID. */
	private Long userId;

	/**  单元ID. */
	private Long unitId;

	/**  登陆名，账号. */
	private String loginId;

	/**  用户名. */
	private String name;

	/**  sessionId. */
	private String sessionId;

	/**  登陆时间. */
	private Date loginTime;

	/**  请求地址. */
	private String requestUrl;

	/**  客户端IP地址. */
	private String clientIp;

	/**  扩展字段. */
	private Map<String, Object> extProps;

	/**  用户类型. */
	private int userType;

	/**
	 * Gets the 当前用户ID.
	 *
	 * @return the 当前用户ID
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * Sets the 当前用户ID.
	 *
	 * @param userId the new 当前用户ID
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * Gets the 单元ID.
	 *
	 * @return the 单元ID
	 */
	public Long getUnitId() {
		return unitId;
	}

	/**
	 * Sets the 单元ID.
	 *
	 * @param unitId the new 单元ID
	 */
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	/**
	 * Gets the 登陆名，账号.
	 *
	 * @return the 登陆名，账号
	 */
	public String getLoginId() {
		return loginId;
	}

	/**
	 * Sets the 登陆名，账号.
	 *
	 * @param loginId the new 登陆名，账号
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	/**
	 * Gets the 用户名.
	 *
	 * @return the 用户名
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the 用户名.
	 *
	 * @param name the new 用户名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the sessionId.
	 *
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * Sets the sessionId.
	 *
	 * @param sessionId the new sessionId
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * Gets the 登陆时间.
	 *
	 * @return the 登陆时间
	 */
	public Date getLoginTime() {
		return loginTime;
	}

	/**
	 * Sets the 登陆时间.
	 *
	 * @param loginTime the new 登陆时间
	 */
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	/**
	 * Gets the 请求地址.
	 *
	 * @return the 请求地址
	 */
	public String getRequestUrl() {
		return requestUrl;
	}

	/**
	 * Sets the 请求地址.
	 *
	 * @param requestUrl the new 请求地址
	 */
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	/**
	 * Gets the 客户端IP地址.
	 *
	 * @return the 客户端IP地址
	 */
	public String getClientIp() {
		return clientIp;
	}

	/**
	 * Sets the 客户端IP地址.
	 *
	 * @param clientIp the new 客户端IP地址
	 */
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	/**
	 * Gets the 扩展字段.
	 *
	 * @return the 扩展字段
	 */
	public Map<String, Object> getExtProps() {
		return extProps;
	}

	/**
	 * Sets the 扩展字段.
	 *
	 * @param extProps the new 扩展字段
	 */
	public void setExtProps(Map<String, Object> extProps) {
		this.extProps = extProps;
	}

	/**
	 * Gets the 用户类型.
	 *
	 * @return the 用户类型
	 */
	public int getUserType() {
		return userType;
	}

	/**
	 * Sets the 用户类型.
	 *
	 * @param userType the new 用户类型
	 */
	public void setUserType(int userType) {
		this.userType = userType;
	}

}
