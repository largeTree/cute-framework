/*
 * 
 */
package com.qiuxs.cuteframework.web.controller.api;

import java.lang.reflect.Method;

import com.qiuxs.cuteframework.web.action.IAction;

// TODO: Auto-generated Javadoc
/**
 * Api配置.
 *
 * @author qiuxs
 * 
 * 创建时间 ： 2018年8月6日 下午9:31:41
 */
public class ApiConfig {

	/**  接口类型：后台接口. */
	public static final long API_TYPE_ADMIN = 1 << 0;
	
	/**  接口类型：前端接口. */
	public static final long API_TYPE_USER = 1 << 1;

	/**  apiKey. */
	private String key;
	
	/**  描述信息. */
	private String desc;
	
	/**  是否需要登陆. */
	private boolean loginFlag;
	
	/**  是否需要授权. */
	private boolean authFlag;
	
	/**  接口类型. */
	private long type;
	
	/**  action对象. */
	private String bean;
	
	/**  action持有对象. */
	private IAction action;
	
	/**  方法名. */
	private String method;
	
	/**  方法持有对象. */
	private Method methodObj;
	
	/**  忽略默认响应. */
	private boolean ignoreDefaultResp;
	
	/**  参数个数. */
	private int paramCount;

	/**
	 * Gets the apiKey.
	 *
	 * @return the apiKey
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Sets the apiKey.
	 *
	 * @param key the new apiKey
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Gets the 描述信息.
	 *
	 * @return the 描述信息
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * Sets the 描述信息.
	 *
	 * @param desc the new 描述信息
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * Checks if is 是否需要登陆.
	 *
	 * @return the 是否需要登陆
	 */
	public boolean isLoginFlag() {
		return loginFlag;
	}

	/**
	 * Sets the 是否需要登陆.
	 *
	 * @param loginFlag the new 是否需要登陆
	 */
	public void setLoginFlag(boolean loginFlag) {
		this.loginFlag = loginFlag;
	}

	/**
	 * Checks if is 是否需要授权.
	 *
	 * @return the 是否需要授权
	 */
	public boolean isAuthFlag() {
		return authFlag;
	}

	/**
	 * Sets the 是否需要授权.
	 *
	 * @param authFlag the new 是否需要授权
	 */
	public void setAuthFlag(boolean authFlag) {
		this.authFlag = authFlag;
	}

	/**
	 * Gets the 接口类型.
	 *
	 * @return the 接口类型
	 */
	public long getType() {
		return type;
	}

	/**
	 * Sets the 接口类型.
	 *
	 * @param type the new 接口类型
	 */
	public void setType(long type) {
		this.type = type;
	}

	/**
	 * Gets the action对象.
	 *
	 * @return the action对象
	 */
	public String getBean() {
		return bean;
	}

	/**
	 * Sets the action对象.
	 *
	 * @param bean the new action对象
	 */
	public void setBean(String bean) {
		this.bean = bean;
	}

	/**
	 * Gets the action持有对象.
	 *
	 * @return the action持有对象
	 */
	public IAction getAction() {
		return action;
	}

	/**
	 * Sets the action持有对象.
	 *
	 * @param action the new action持有对象
	 */
	public void setAction(IAction action) {
		this.action = action;
	}

	/**
	 * Gets the 方法名.
	 *
	 * @return the 方法名
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * Sets the 方法名.
	 *
	 * @param method the new 方法名
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * Gets the 方法持有对象.
	 *
	 * @return the 方法持有对象
	 */
	public Method getMethodObj() {
		return methodObj;
	}

	/**
	 * Sets the 方法持有对象.
	 *
	 * @param methodObj the new 方法持有对象
	 */
	public void setMethodObj(Method methodObj) {
		this.methodObj = methodObj;
	}

	/**
	 * Checks if is 忽略默认响应.
	 *
	 * @return the 忽略默认响应
	 */
	public boolean isIgnoreDefaultResp() {
		return ignoreDefaultResp;
	}

	/**
	 * Sets the 忽略默认响应.
	 *
	 * @param ignoreDefaultResp the new 忽略默认响应
	 */
	public void setIgnoreDefaultResp(boolean ignoreDefaultResp) {
		this.ignoreDefaultResp = ignoreDefaultResp;
	}

	/**
	 * Gets the 参数个数.
	 *
	 * @return the 参数个数
	 */
	public int getParamCount() {
		return paramCount;
	}

	/**
	 * Sets the 参数个数.
	 *
	 * @param paramCount the new 参数个数
	 */
	public void setParamCount(int paramCount) {
		this.paramCount = paramCount;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\"{[").append(this.getKey()).append("]").append("desc=").append("[").append(this.getDesc()).append("]}\"");
		return sb.toString();
	}

}
