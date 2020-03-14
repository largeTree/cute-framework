package com.qiuxs.cuteframework.view.pagemodel;

import java.io.Serializable;

/**
 * 列表行按钮
 * @author qiuxs
 *
 */
public class TdBtn implements Serializable {
	
	private static final long serialVersionUID = -1875555474981592215L;
	
	/** 按钮名 */
	private String name;
	/** 主键 */
	private String pk;
	/** js方法 */
	private String js;
	/** 链接 */
	private String href;
	/** 接口号 */
	private String apiKey;
	/** 参数定义 */
	private String params;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getJs() {
		return js;
	}

	public void setJs(String js) {
		this.js = js;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

}
