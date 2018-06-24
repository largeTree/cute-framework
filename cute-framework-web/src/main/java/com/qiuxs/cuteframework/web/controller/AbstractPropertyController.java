package com.qiuxs.cuteframework.web.controller;

import java.io.Serializable;

import com.qiuxs.cuteframework.core.basic.utils.JsonUtils;
import com.qiuxs.cuteframework.core.persistent.database.entity.IEntity;
import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IPropertyService;

/**
 * 抽象属性对象控制器
 * @author qiuxs
 *
 * @param <PK>
 * @param <T>
 * @param <S>
 */
public abstract class AbstractPropertyController<PK extends Serializable, T extends IEntity<PK>, S extends IPropertyService<PK, T>> extends BaseController {

	protected abstract S getService();

	protected T fromJSON(String jsonData) {
		return JsonUtils.parseObject(jsonData, this.getService().getPojoClass());
	}
	
}
