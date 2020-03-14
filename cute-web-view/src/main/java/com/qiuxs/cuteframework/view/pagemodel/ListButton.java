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

	/**  按钮上的文字. */
	private String text;
	
	/**  图标，easyui支持的图标类名. */
	private String icon;
	
	/**  跳转连接. */
	private String href;
	
	/**  直接调用某个接口. */
	private String apiKey;
	
	/**  指定的js方法. */
	private String js;

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

}
