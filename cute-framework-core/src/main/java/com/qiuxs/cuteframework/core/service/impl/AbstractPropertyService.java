package com.qiuxs.cuteframework.core.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.basic.bean.ViewProperty;
import com.qiuxs.cuteframework.core.basic.utils.JsonUtils;
import com.qiuxs.cuteframework.core.entity.IObject;
import com.qiuxs.cuteframework.core.service.IPropertyService;

public abstract class AbstractPropertyService<PK extends Serializable, T extends IObject<PK>> implements IPropertyService<PK, T> {

	private Map<String, ViewProperty<?>> properties = new HashMap<>();

	/** 表名 */
	private String tableName;
	/** 描述信息 */
	private String description;
	/** 实体类型 */
	private Class<T> entityClass;

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Class<T> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	@Override
	public JSONObject translateBean(T bean) {
		Map<String, ViewProperty<?>> propertis = this.getProperties();
		JSONObject jbean = JsonUtils.bean2JSONObject(bean);
		if (propertis.size() > 0) {
			JSONObject captions = new JSONObject();
			for (Iterator<Map.Entry<String, ViewProperty<?>>> iter = propertis.entrySet().iterator(); iter.hasNext();) {
				Map.Entry<String, ViewProperty<?>> entry = iter.next();
				String key = entry.getKey();
				ViewProperty<?> property = entry.getValue();
				String caption = property.getCaption(jbean.get(key));
				captions.put(key, caption);
			}
			jbean.put("captions", captions);
		}
		return jbean;
	}

	@Override
	public JSONArray translateBeans(Collection<T> beans) {
		int size = beans.size();
		JSONArray retList = new JSONArray(size);
		if (size > 0) {
			for (T bean : beans) {
				JSONObject translatedBean = translateBean(bean);
				retList.add(translatedBean);
			}
		}
		return retList;
	}

	/**
	 * 获取当前属性配置信息
	 * @return
	 */
	private Map<String, ViewProperty<?>> getProperties() {
		if (this.properties.size() == 0) {
			this.initInnerProperty(properties);
		}
		return properties;
	}

	/**
	 * 子类负责向属性集合中填值
	 * @param properties
	 */
	protected abstract void initInnerProperty(Map<String, ViewProperty<?>> properties);

}
