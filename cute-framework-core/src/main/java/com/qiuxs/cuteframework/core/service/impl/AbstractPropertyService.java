package com.qiuxs.cuteframework.core.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.basic.bean.BaseField;
import com.qiuxs.cuteframework.core.basic.bean.BaseProperty;
import com.qiuxs.cuteframework.core.basic.code.provider.ICodeTranslatable;
import com.qiuxs.cuteframework.core.basic.utils.JsonUtils;
import com.qiuxs.cuteframework.core.entity.IObject;
import com.qiuxs.cuteframework.core.service.IPropertyService;

public abstract class AbstractPropertyService<PK extends Serializable, T extends IObject<PK>> implements IPropertyService<PK, T> {

	private Map<String, BaseProperty<? extends BaseField, ? extends Serializable, ? extends ICodeTranslatable<?>>> properties = new HashMap<>();

	@Override
	public JSONObject translateBean(T bean) {
		Map<String, BaseProperty<? extends BaseField, ? extends Serializable, ? extends ICodeTranslatable<?>>> propertis = this.getProperties();
		JSONObject jbean = JsonUtils.bean2JSONObject(bean);
		if (propertis.size() > 0) {
			JSONObject captions = new JSONObject();
			for (Iterator<Map.Entry<String, BaseProperty<? extends BaseField, ? extends Serializable, ? extends ICodeTranslatable<?>>>> iter = propertis.entrySet().iterator(); iter.hasNext();) {
				Map.Entry<String, BaseProperty<? extends BaseField, ? extends Serializable, ? extends ICodeTranslatable<?>>> entry = iter.next();
				String key = entry.getKey();
				BaseProperty<? extends BaseField, ? extends Serializable, ? extends ICodeTranslatable<?>> property = entry.getValue();
				String caption = property.getCaption(jbean.get(key));
				captions.put(key, caption);
			}
			if (captions.size() > 0) {
				jbean.put("captions", captions);
			}
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
	private Map<String, BaseProperty<? extends BaseField, ? extends Serializable, ? extends ICodeTranslatable<?>>> getProperties() {
		if (this.properties.size() == 0) {
			this.initInner(properties);
		}
		return properties;
	}

	/**
	 * 子类负责向属性集合中填值
	 * @param properties
	 */
	protected abstract void initInner(Map<String, BaseProperty<? extends BaseField, ? extends Serializable, ? extends ICodeTranslatable<?>>> properties);

}
