package com.qiuxs.cuteframework.common.mylog.entity;

import java.util.Date;

import com.qiuxs.cuteframework.core.persistent.database.entity.impl.AbstractEntity;

/**
 * 实体类
 *	for table mylog
 * 
 * 创建时间 ：2019-07-25 22:32:08
 * @author qiuxs
 *
 */

public class Mylog extends AbstractEntity<Long> {

	private static final long serialVersionUID = 1L;
	
	/** 客户端IP地址 */
	private String ip;

	/** 服务器ID */
	private String serverId;

	/** 会话中用户ID */
	private Long userId;

	/** 全局日志ID */
	private Long globalId;

	/** 日志级别 */
	private String level;

	/** 类名 */
	private String className;

	/** 方法名 */
	private String method;

	/** 日志消息文本 */
	private String msg;

	/** throwable */
	private String throwable;

	/** 堆栈信息 */
	private String stacktrace;

	/** 线程ID */
	private String threadId;

	/** 错误代码 */
	private String errorCode;

	/** 日志时间 */
	private Date logTime;


	/**
	 * get the 客户端IP地址
	 * @return ip
	 */
	public String getIp() {
		return this.ip;
	}

	/**
	 * set the 客户端IP地址
	 * @param ip
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * get the 服务器ID
	 * @return serverId
	 */
	public String getServerId() {
		return this.serverId;
	}

	/**
	 * set the 服务器ID
	 * @param serverId
	 */
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	/**
	 * get the 会话中用户ID
	 * @return userId
	 */
	public Long getUserId() {
		return this.userId;
	}

	/**
	 * set the 会话中用户ID
	 * @param userId
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * get the 全局日志ID
	 * @return globalId
	 */
	public Long getGlobalId() {
		return this.globalId;
	}

	/**
	 * set the 全局日志ID
	 * @param globalId
	 */
	public void setGlobalId(Long globalId) {
		this.globalId = globalId;
	}

	/**
	 * get the 日志级别
	 * @return level
	 */
	public String getLevel() {
		return this.level;
	}

	/**
	 * set the 日志级别
	 * @param level
	 */
	public void setLevel(String level) {
		this.level = level;
	}

	/**
	 * get the 类名
	 * @return className
	 */
	public String getClassName() {
		return this.className;
	}

	/**
	 * set the 类名
	 * @param className
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * get the 方法名
	 * @return method
	 */
	public String getMethod() {
		return this.method;
	}

	/**
	 * set the 方法名
	 * @param method
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * get the 日志消息文本
	 * @return msg
	 */
	public String getMsg() {
		return this.msg;
	}

	/**
	 * set the 日志消息文本
	 * @param msg
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * get the throwable
	 * @return throwable
	 */
	public String getThrowable() {
		return this.throwable;
	}

	/**
	 * set the throwable
	 * @param throwable
	 */
	public void setThrowable(String throwable) {
		this.throwable = throwable;
	}

	/**
	 * get the 堆栈信息
	 * @return stacktrace
	 */
	public String getStacktrace() {
		return this.stacktrace;
	}

	/**
	 * set the 堆栈信息
	 * @param stacktrace
	 */
	public void setStacktrace(String stacktrace) {
		this.stacktrace = stacktrace;
	}

	/**
	 * get the 线程ID
	 * @return threadId
	 */
	public String getThreadId() {
		return this.threadId;
	}

	/**
	 * set the 线程ID
	 * @param threadId
	 */
	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}

	/**
	 * get the 错误代码
	 * @return errorCode
	 */
	public String getErrorCode() {
		return this.errorCode;
	}

	/**
	 * set the 错误代码
	 * @param errorCode
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * get the 日志时间
	 * @return logTime
	 */
	public Date getLogTime() {
		return this.logTime;
	}

	/**
	 * set the 日志时间
	 * @param logTime
	 */
	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

}