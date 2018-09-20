package com.qiuxs.cuteframework.web.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.qiuxs.cuteframework.core.basic.ex.ErrorCodes;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.JsonUtils;
import com.qiuxs.cuteframework.core.basic.utils.MapUtils;
import com.qiuxs.cuteframework.core.persistent.database.dao.IBaseDao;
import com.qiuxs.cuteframework.core.persistent.database.dao.page.PageInfo;
import com.qiuxs.cuteframework.core.persistent.database.entity.IEntity;
import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDataPropertyService;
import com.qiuxs.cuteframework.web.annotation.Api;
import com.qiuxs.cuteframework.web.bean.ListResult;
import com.qiuxs.cuteframework.web.controller.api.Param;

/**
 * 
 * 功能描述: 抽象数据控制器，提供基础的增删改查方法<br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2018年4月20日 下午10:31:55 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public abstract class AbstractDataController<PK extends Serializable, T extends IEntity<PK>, D extends IBaseDao<PK, T>, S extends IDataPropertyService<PK, T, D>>
		extends AbstractPropertyController<PK, T, S> {

	@Api
	public PK save(@Param("jsonParam") String jsonParam) {
		T bean = super.fromJSON(jsonParam);
		this.getService().save(bean);
		return bean.getId();
	}

	@Api
	public PK create(@Param("jsonParam") String jsonParam) {
		T bean = super.fromJSON(jsonParam);
		return this.createDirect(bean);
	}

	protected PK createDirect(T bean) {
		this.getService().create(bean);
		return bean.getId();
	}

	/**
	 * TODO 此处使用泛型参数有问题 暂时先改成Map参数，后续修正
	 * @author qiuxs
	 *
	 * @param params
	 *
	 * 创建时间：2018年8月29日 下午10:28:27
	 */
	@SuppressWarnings("unchecked")
	@Api
	public void delete(Map<String, String> params) {
		Long id = MapUtils.getLongMust(params, "id");
		this.getService().deleteById((PK) id);
	}

	@Api
	public PK update(@Param("jsonParam") String jsonParam) {
		T newBean = super.fromJSON(jsonParam);
		if (newBean.getId() == null) {
			ExceptionUtils.throwLogicalException(ErrorCodes.DataError.UPDATE_NO_ID, "id is required");
		}
		return this.updateDirect(newBean);
	}

	protected PK updateDirect(T bean) {
		this.getService().update(bean);
		return bean.getId();
	}

	@Api
	public T get(@Param("id") PK id) {
		T bean = this.getService().getById(id);
		return bean;
	}

	@Api
	public ListResult list(Map<String, String> params, PageInfo pageInfo) {
		List<T> list = this.getService().findByMap(new HashMap<>(params), pageInfo);
		JSONArray jlist = null;
		if (MapUtils.getBooleanValue(params, "wrapper", false)) {
			jlist = this.getService().translateBeans(list);
		} else {
			jlist = JsonUtils.toJSONArray(list);
		}
		return new ListResult(jlist, pageInfo.getTotal());
	}
}
