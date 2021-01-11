package com.qiuxs.cuteframework.view.pagemodel;

import java.io.Serializable;
import java.util.List;

/**
 * 搜索表单
 * @author qiuxs
 *
 */
public class Search implements Serializable {
	
	private static final long serialVersionUID = 709404894756292323L;
	
	/** 字段列表 */
	private List<Field> fields;

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

}
