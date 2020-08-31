package com.qiuxs.cuteframework.view.pagemodel;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * 列表上方按钮
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年1月31日 下午2:46:24 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class ListButton implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -472771926097353199L;

	/**  按钮ID，用作权限控制. */
	private String id;
	
	/**  按钮上的文字. */
	private String text;
	
	/**  图标，element-ui支持的图标类名. */
	private String icon;
	
	/**  跳转连接. */
	private String href;
	
	/**  直接调用某个接口. */
	private String apiKey;
	
	/**  指定的js方法. */
	private String js;
	
	/**  对应前端的颜色类型. */
	private String type;
	
	/**  是否批量操作按鈕. */
	private String batch;
	
	/**  操作类型. */
	private String optype;
	
	/**  表单ID. */
	private String formid;

	/**
	 * Gets the 按钮ID，用作权限控制.
	 *
	 * @return the 按钮ID，用作权限控制
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the 按钮ID，用作权限控制.
	 *
	 * @param id the new 按钮ID，用作权限控制
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the 按钮上的文字.
	 *
	 * @return the 按钮上的文字
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the 按钮上的文字.
	 *
	 * @param text the new 按钮上的文字
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Gets the 图标，easyui支持的图标类名.
	 *
	 * @return the 图标，easyui支持的图标类名
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * Sets the 图标，easyui支持的图标类名.
	 *
	 * @param icon the new 图标，easyui支持的图标类名
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * Gets the 跳转连接.
	 *
	 * @return the 跳转连接
	 */
	public String getHref() {
		return href;
	}

	/**
	 * Sets the 跳转连接.
	 *
	 * @param href the new 跳转连接
	 */
	public void setHref(String href) {
		this.href = href;
	}

	/**
	 * Gets the 直接调用某个接口.
	 *
	 * @return the 直接调用某个接口
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * Sets the 直接调用某个接口.
	 *
	 * @param apiKey the new 直接调用某个接口
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * Gets the 指定的js方法.
	 *
	 * @return the 指定的js方法
	 */
	public String getJs() {
		return js;
	}

	/**
	 * Sets the 指定的js方法.
	 *
	 * @param js the new 指定的js方法
	 */
	public void setJs(String js) {
		this.js = js;
	}

	/**
	 * Gets the 对应前端的颜色类型.
	 *
	 * @return the 对应前端的颜色类型
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the 对应前端的颜色类型.
	 *
	 * @param type the new 对应前端的颜色类型
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the 是否批量操作按鈕.
	 *
	 * @return the 是否批量操作按鈕
	 */
	public String getBatch() {
		return batch;
	}

	/**
	 * Sets the 是否批量操作按鈕.
	 *
	 * @param batch the new 是否批量操作按鈕
	 */
	public void setBatch(String batch) {
		this.batch = batch;
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

}
