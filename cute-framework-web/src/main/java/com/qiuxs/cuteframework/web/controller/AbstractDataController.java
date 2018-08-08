package com.qiuxs.cuteframework.web.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qiuxs.cuteframework.core.basic.ex.ErrorCodes;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.persistent.database.dao.IBaseDao;
import com.qiuxs.cuteframework.core.persistent.database.dao.page.PageInfo;
import com.qiuxs.cuteframework.core.persistent.database.entity.IEntity;
import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDataPropertyService;
import com.qiuxs.cuteframework.web.annotation.Api;
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
	public String create(@Param("jsonParam") String jsonParam) {
		T bean = super.fromJSON(jsonParam);
		this.getService().save(bean);
		return super.responseVal(bean.getId());
	}

	@Api
	public String delete(@Param("id") PK id) {
		this.getService().deleteById(id);
		return super.responseSuccess();
	}

	@Api
	public String update(@Param("jsonParam") String jsonParam) {
		T newBean = super.fromJSON(jsonParam);
		if (newBean.getId() == null) {
			ExceptionUtils.throwLogicalException(ErrorCodes.DataError.UPDATE_NO_ID, "id is required");
		}
		this.getService().update(newBean);
		return super.responseVal(newBean.getId());
	}

	@Api
	public String get(@Param("id") PK id) {
		T bean = this.getService().getById(id);
		return super.responseRes(bean);
	}

	@Api
	public String list(Map<String, String> params) {
		PageInfo pageInfo = super.preparePageInfo(params);
		List<T> list = this.getService().findByMap(new HashMap<>(params), pageInfo);
		return super.responseRes(list);
	}
}
