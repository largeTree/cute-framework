package com.qiuxs.cuteframework.web.controller.api;

import java.lang.reflect.Method;

import com.qiuxs.cuteframework.web.action.IAction;

/**
 * Api配置
 * @author qiuxs
 * 
 * 创建时间 ： 2018年8月6日 下午9:31:41
 *
 */
public class ApiConfig {

	/** apiKey */
	private String key;
	/** 描述信息 */
	private String desc;
	/** 是否需要登陆 */
	private boolean loginFlag;
	/** 是否需要授权 */
	private boolean authFlag;
	/** action对象 */
	private String bean;
	/** action持有对象 */
	private IAction action;
	/** 方法名 */
	private String method;
	/** 方法持有对象 */
	private Method methodObj;
	/** 忽略默认响应 */
	private boolean ignoreDefaultResp;
	/** 参数个数 */
	private int paramCount;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public boolean isLoginFlag() {
		return loginFlag;
	}

	public void setLoginFlag(boolean loginFlag) {
		this.loginFlag = loginFlag;
	}

	public boolean isAuthFlag() {
		return authFlag;
	}

	public void setAuthFlag(boolean authFlag) {
		this.authFlag = authFlag;
	}

	public String getBean() {
		return bean;
	}

	public void setBean(String bean) {
		this.bean = bean;
	}

	public IAction getAction() {
		return action;
	}

	public void setAction(IAction action) {
		this.action = action;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Method getMethodObj() {
		return methodObj;
	}

	public void setMethodObj(Method methodObj) {
		this.methodObj = methodObj;
	}

	public boolean isIgnoreDefaultResp() {
		return ignoreDefaultResp;
	}

	public void setIgnoreDefaultResp(boolean ignoreDefaultResp) {
		this.ignoreDefaultResp = ignoreDefaultResp;
	}

	public int getParamCount() {
		return paramCount;
	}

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
