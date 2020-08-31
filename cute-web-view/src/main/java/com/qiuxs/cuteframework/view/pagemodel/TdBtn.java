package com.qiuxs.cuteframework.view.pagemodel;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * 列表行按钮.
 *
 * @author qiuxs
 */
public class TdBtn implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1875555474981592215L;
	
	/**  按钮ID,后续用作权限控制. */
	private String id;
	
	/**  按钮名. */
	private String name;
	
	/**  主键. */
	private String pk;
	
	/**  js方法. */
	private String js;
	
	/**  链接. */
	private String href;
	
	/**  接口号. */
	private String apiKey;
	
	/**  参数定义. */
	private String params;
	
	/**  表单ID. */
	private String formid;
	
	/**  操作类型. */
	private String optype;
	
	/**
	 * Gets the 按钮名.
	 *
	 * @return the 按钮名
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the 按钮名.
	 *
	 * @param name the new 按钮名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the 主键.
	 *
	 * @return the 主键
	 */
	public String getPk() {
		return pk;
	}

	/**
	 * Sets the 主键.
	 *
	 * @param pk the new 主键
	 */
	public void setPk(String pk) {
		this.pk = pk;
	}

	/**
	 * Gets the js方法.
	 *
	 * @return the js方法
	 */
	public String getJs() {
		return js;
	}

	/**
	 * Sets the js方法.
	 *
	 * @param js the new js方法
	 */
	public void setJs(String js) {
		this.js = js;
	}

	/**
	 * Gets the 链接.
	 *
	 * @return the 链接
	 */
	public String getHref() {
		return href;
	}

	/**
	 * Sets the 链接.
	 *
	 * @param href the new 链接
	 */
	public void setHref(String href) {
		this.href = href;
	}

	/**
	 * Gets the 接口号.
	 *
	 * @return the 接口号
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * Sets the 接口号.
	 *
	 * @param apiKey the new 接口号
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * Gets the 参数定义.
	 *
	 * @return the 参数定义
	 */
	public String getParams() {
		return params;
	}

	/**
	 * Sets the 参数定义.
	 *
	 * @param params the new 参数定义
	 */
	public void setParams(String params) {
		this.params = params;
	}

	/**
	 * Gets the 按钮ID,后续用作权限控制.
	 *
	 * @return the 按钮ID,后续用作权限控制
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the 按钮ID,后续用作权限控制.
	 *
	 * @param id the new 按钮ID,后续用作权限控制
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the 表单ID.
	 *
	 * @return the 表单ID
	 */
	public String getFormid() {
		return formid;
	}

	/**
	 * Sets the 表单ID.
	 *
	 * @param formid the new 表单ID
	 */
	public void setFormid(String formid) {
		this.formid = formid;
	}

	/**
	 * Gets the 操作类型.
	 *
	 * @return the 操作类型
	 */
	public String getOptype() {
		return optype;
	}

	/**
	 * Sets the 操作类型.
	 *
	 * @param optype the new 操作类型
	 */
	public void setOptype(String optype) {
		this.optype = optype;
	}

}
