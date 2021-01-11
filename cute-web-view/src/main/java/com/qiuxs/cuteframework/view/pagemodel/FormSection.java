package com.qiuxs.cuteframework.view.pagemodel;

import java.io.Serializable;
import java.util.List;

/**
 * 表单部件
 * @author qiuxs
 *
 */
public class FormSection implements Serializable {

	private static final long serialVersionUID = -3819649102340027802L;
	
	/** 标题 */
	private String title;
	/** 是否是明细部件 */
	private boolean detail;
	/** 表单键 */
	private String key;
	/** 包含的字段 */
	private List<Field> fields;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isDetail() {
		return detail;
	}

	public void setDetail(boolean detail) {
		this.detail = detail;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

}
