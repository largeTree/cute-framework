package com.qiuxs.cuteframework.core.persistent.service;

import java.io.Serializable;
import java.util.Collection;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.persistent.entity.IEntity;
import com.qiuxs.cuteframework.core.persistent.service.ifc.IPropertyService;

/**
 * 功能描述: <br/>
 * 新增原因: TODO<br/>
 * 新增日期: 2018年4月18日 下午10:29:22 <br/>
 * 
 * @author qiuxs
 * @version 1.0.0
 */
public abstract class AbstractPropertyService<PK extends Serializable, T extends IEntity<PK>>
		implements IPropertyService<PK, T> {

	private Class<T> pojoClass;

	private Class<PK> pkClass;

	public AbstractPropertyService(Class<PK> pkClass, Class<T> pojoClass) {
		this.pkClass = pkClass;
		this.pojoClass = pojoClass;
	}

	public Class<T> getPojoClass() {
		return pojoClass;
	}

	public Class<PK> getPkClass() {
		return pkClass;
	}

	@Override
	public JSONObject translateBean(T bean) {
		return null;
	}

	@Override
	public JSONArray translateBeans(Collection<T> beans) {
		return null;
	}
}
