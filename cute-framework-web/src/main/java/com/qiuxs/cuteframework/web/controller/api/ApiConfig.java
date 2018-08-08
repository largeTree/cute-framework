package com.qiuxs.cuteframework.web.controller.api;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

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
	/** 方法参数类型列表 */
	private Parameter[] parameters;
	/** 描述信息 */
	private String desc;
	/** 是否需要登陆 */
	private boolean loginFlag;
	/** 是否需要授权 */
	private boolean authFlag;
	/** 持有方法对象 */
	private Method method;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Parameter[] getParameters() {
		return parameters;
	}

	public void setParameters(Parameter[] parameters) {
		this.parameters = parameters;
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

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\"{[").append(this.getKey()).append("]")
				.append("desc=").append("[").append(this.getDesc()).append("]}\"");
		return sb.toString();
	}

}
