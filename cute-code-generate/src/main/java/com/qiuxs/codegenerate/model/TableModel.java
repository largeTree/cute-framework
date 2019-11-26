package com.qiuxs.codegenerate.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.qiuxs.codegenerate.task.TableBuilderService;
import com.qiuxs.codegenerate.utils.ComnUtils;

public class TableModel {

	private boolean buildFlag;
	private String author;
	private String tableName;
	private String packageName;
	private String prefix;
	private String pkClass;
	private String pkField;
	private String superClass = "com.qiuxs.cuteframework.core.persistent.database.entity.impl.AbstractEntity";
	private String simpleSuperClass = "AbstractEntity";
	private String className;
	private String desc;
	private boolean entity = true;
	private boolean dao = true;
	private boolean mapper = true;
	private boolean service = true;
	private boolean action = true;

	private boolean hasError = false;
	private Set<String> unkonwTypes = new HashSet<>();

	private List<FieldModel> fields;
	private List<FieldModel> ukFields = new ArrayList<FieldModel>();

	private Set<String> importClasses = new HashSet<>();
	
	public boolean isBuildFlag() {
		return buildFlag;
	}

	public void setBuildFlag(boolean buildFlag) {
		this.buildFlag = buildFlag;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getPkClass() {
		return pkClass;
	}

	public void setPkClass(String pkClass) {
		this.pkClass = pkClass;
	}

	public String getPkField() {
		return pkField;
	}

	public void setPkField(String pkField) {
		this.pkField = ComnUtils.formatName(pkField, null);
	}

	public String getSuperClass() {
		return superClass;
	}

	public void setSuperClass(String superClass) {
		this.superClass = superClass;
		String[] split = this.superClass.split("\\.");
		this.setSimpleSuperClass(split[split.length - 1]);
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public boolean isEntity() {
		return entity;
	}

	public void setEntity(boolean entity) {
		this.entity = entity;
	}

	public boolean isDao() {
		return dao;
	}

	public void setDao(boolean dao) {
		this.dao = dao;
	}

	public boolean isMapper() {
		return mapper;
	}

	public void setMapper(boolean mapper) {
		this.mapper = mapper;
	}

	public boolean isService() {
		return service;
	}

	public void setService(boolean service) {
		this.service = service;
	}

	public boolean isAction() {
		return action;
	}

	public void setAction(boolean action) {
		this.action = action;
	}

	public List<FieldModel> getFields() {
		return fields;
	}

	public void setFields(List<FieldModel> fields) {
		this.fields = fields;
		this.fields.forEach(field -> {
			if (field.getJavaType() == null) {
				this.unkonwTypes.add(field.getColumnName());
				hasError = true;
				return;
			}
			
			// 不在忽略字段列表的需要导入类
			if (!TableBuilderService.IGNORE_ENTITY_FIELDS.contains(field.getName())) {
				if (Date.class.getSimpleName().equals(field.getJavaType())) {
					// 日期类型
					this.importClasses.add(Date.class.getName() + ";");
				} else if (BigDecimal.class.getSimpleName().equals(field.getJavaType())) {
					// BigDecimal
					this.importClasses.add(BigDecimal.class.getName() + ";");
				} else if ("JSONObject".equals(field.getJavaType())) {
					// JSONObject
					this.importClasses.add("com.alibaba.fastjson.JSONObject;");
				}
			}
		});
	}

	public Set<String> getImportClasses() {
		return importClasses;
	}

	public void setImportClasses(Set<String> importClasses) {
		this.importClasses = importClasses;
	}

	public Set<String> getUnkonwTypes() {
		return unkonwTypes;
	}

	public void setUnkonwTypes(Set<String> unkonwTypes) {
		this.unkonwTypes = unkonwTypes;
	}

	public boolean isHasError() {
		return hasError;
	}

	public void setHasError(boolean hasError) {
		this.hasError = hasError;
	}

	public String getSimpleSuperClass() {
		return simpleSuperClass;
	}

	public void setSimpleSuperClass(String simpleSuperClass) {
		this.simpleSuperClass = simpleSuperClass;
	}

	public List<FieldModel> getUkFields() {
		return ukFields;
	}

	public void setUkFields(List<FieldModel> ukFields) {
		this.ukFields = ukFields;
	}

}
