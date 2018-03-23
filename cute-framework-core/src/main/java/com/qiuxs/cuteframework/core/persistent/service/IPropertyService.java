package com.qiuxs.cuteframework.core.persistent.service;

import java.io.Serializable;
import java.util.Collection;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.persistent.entity.IObject;

/**
 * 基础服务类 提供对对象的翻译操作
 * @author qiuxs
 *
 * @param <PK>
 * @param <T>
 */
public interface IPropertyService<PK extends Serializable, T extends IObject<PK>> {

	public static final String VIEW_ID = "viewId";

	public Class<T> getEntityClass();

	public JSONObject translateBean(T bean);

	public JSONArray translateBeans(Collection<T> beans);

}
