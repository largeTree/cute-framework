package com.qiuxs.cuteframework.core.basic.bean;

import java.util.Date;

/**
 * 
 * 功能描述: 简单会话持有对象<br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2018年4月23日 下午10:28:06 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class UserLite {

	/** 当前用户ID */
	private Long userId;
	/** 用户名 */
	private String name;
	/** sessionId */
	private String sessionId;
	/** 登陆时间 */
	private Date loginTime;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

}
