package com.qiuxs.gconfig.client.dto;

import java.io.Serializable;

/**
 * 参数选项
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年3月4日 上午10:33:24 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class GConfigOptions implements Serializable {

	private static final long serialVersionUID = -3077057160638242912L;
	
	private String code;
	private String name;
	private String val;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

}
