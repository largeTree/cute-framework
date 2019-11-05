package com.qiuxs.codegenerate.model;

import java.math.BigDecimal;
import java.util.Date;

import com.qiuxs.codegenerate.utils.ComnUtils;

public class FieldModel {

	private String columnName;
	private String name;
	private String javaType;
	private String comment;
	private boolean ignoreEntity;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
		this.setName(ComnUtils.formatName(columnName, null));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJavaType() {
		return javaType;
	}

	public void setJavaType(String dbType) {
		this.javaType = getJavaType(dbType);
	}

	public String getComment() {
		if (ComnUtils.isBlank(comment)) {
			this.comment = this.getName();
		}
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isIgnoreEntity() {
		return ignoreEntity;
	}

	public void setIgnoreEntity(boolean ignoreEntity) {
		this.ignoreEntity = ignoreEntity;
	}

	private String getJavaType(String dbType) {
		String javaType = null;
		switch (dbType) {
		case "enum":
		case "smallint":
		case "int":
		case "tinyint":
			javaType = Integer.class.getSimpleName();
			break;
		case "bigint":
			javaType = Long.class.getSimpleName();
			break;
		case "varchar":
		case "char":
		case "json":
		case "text":
		case "longtext":
			javaType = String.class.getSimpleName();
			break;
		case "decimal":
			javaType = BigDecimal.class.getSimpleName();
			break;
		case "date":
		case "datetime":
		case "timestamp":
			javaType = Date.class.getSimpleName();
			break;
		}
		return javaType;
	}

}
