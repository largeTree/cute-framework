package com.qiuxs.cuteframework.view.pagemodel;

import java.util.List;

/***
 * 表单模型
 * @author qiuxs
 *
 */
public class FormModel {

	/** 表单ID */
	private String id;
	/** 表单标题 */
	private String title;
	/** 获取数据使用的ApiKey */
	private String getApiKey;
	/** 保存数据使用的apiKey */
	private String saveApiKey;
	/** 字段列表 */
	private List<Field> fields;
	/** 表单部件列表 */
	private List<FormSection> sections;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGetApiKey() {
		return getApiKey;
	}

	public void setGetApiKey(String getApiKey) {
		this.getApiKey = getApiKey;
	}

	public String getSaveApiKey() {
		return saveApiKey;
	}

	public void setSaveApiKey(String saveApiKey) {
		this.saveApiKey = saveApiKey;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public List<FormSection> getSections() {
		return sections;
	}

	public void setSections(List<FormSection> sections) {
		this.sections = sections;
	}

}
