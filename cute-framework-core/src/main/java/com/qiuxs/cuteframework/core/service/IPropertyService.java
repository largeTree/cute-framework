package com.qiuxs.cuteframework.core.service;

import java.io.Serializable;
import java.util.Collection;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.entity.IObject;

public interface IPropertyService<PK extends Serializable, T extends IObject<PK>> {

	public static final String VIEW_ID = "viewId";
	
	public Class<T> getEntityClass();

	public JSONObject translateBean(T bean);

	public JSONArray translateBeans(Collection<T> beans);

}
