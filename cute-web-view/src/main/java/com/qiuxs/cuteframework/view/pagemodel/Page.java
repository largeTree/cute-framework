package com.qiuxs.cuteframework.view.pagemodel;

import java.io.Serializable;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * 页面配置.
 *
 * @author qiuxs
 */
public class Page implements Serializable {
	
	private static final long serialVersionUID = -885650549863375664L;

	/**  页面ID. */
	private String id;
	
	/**  页面名称. */
	private String name;
	
	/**  数据列表. */
	private DataList dataList;

	/** The map forms. */
	private Map<String, FormModel> mapForms;

	/**
	 * Gets the 页面ID.
	 *
	 * @return the 页面ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the 页面ID.
	 *
	 * @param id the new 页面ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the 页面名称.
	 *
	 * @return the 页面名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the 页面名称.
	 *
	 * @param name the new 页面名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the 数据列表.
	 *
	 * @return the 数据列表
	 */
	public DataList getDataList() {
		return dataList;
	}

	/**
	 * Sets the 数据列表.
	 *
	 * @param dataList the new 数据列表
	 */
	public void setDataList(DataList dataList) {
		this.dataList = dataList;
	}

	/**
	 * Gets the map forms.
	 *
	 * @return the map forms
	 */
	public Map<String, FormModel> getMapForms() {
		return mapForms;
	}

	/**
	 * Sets the map forms.
	 *
	 * @param mapForms the map forms
	 */
	public void setMapForms(Map<String, FormModel> mapForms) {
		this.mapForms = mapForms;
	}
	
	/**
	 * Gets the form.
	 *
	 * @param formId the form id
	 * @return the form
	 */
	public FormModel getForm(String formId) {
		if (this.mapForms == null) {
			throw new RuntimeException("formId[ " + formId + " ]未定义");
		}
		return this.mapForms.get(formId);
	}

}
