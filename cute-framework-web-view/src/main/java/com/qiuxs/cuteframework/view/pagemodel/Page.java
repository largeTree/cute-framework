package com.qiuxs.cuteframework.view.pagemodel;

import java.util.Map;

/**
 * 页面配置
 * @author qiuxs
 *
 */
public class Page {
	/** 页面ID */
	private String id;
	/** 数据列表 */
	private DataList dataList;

	private Map<String, FormModel> mapForms;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DataList getDataList() {
		return dataList;
	}

	public void setDataList(DataList dataList) {
		this.dataList = dataList;
	}

	public Map<String, FormModel> getMapForms() {
		return mapForms;
	}

	public void setMapForms(Map<String, FormModel> mapForms) {
		this.mapForms = mapForms;
	}
	
	public FormModel getForm(String formId) {
		if (this.mapForms == null) {
			throw new RuntimeException("formId[ " + formId + " ]未定义");
		}
		return this.mapForms.get(formId);
	}

}
