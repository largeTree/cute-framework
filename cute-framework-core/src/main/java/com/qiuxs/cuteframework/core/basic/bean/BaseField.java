package com.qiuxs.cuteframework.core.basic.bean;

/**
 * 基础字段配置
 * @author qiuxs
 *
 */
public class BaseField {

	/** 字段名 */
	private String name;

	/** 字段标签 */
	private String label;
	/** 字段类型 */
	private String type;
	/** 字段类型Class */
	private Class<?> typeClass;

	public BaseField(String name, String label, String type) {
		this.name = name;
		this.label = label;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Class<?> getTypeClass() {
		return typeClass;
	}

	public void setTypeClass(Class<?> typeClass) {
		this.typeClass = typeClass;
	}

}
