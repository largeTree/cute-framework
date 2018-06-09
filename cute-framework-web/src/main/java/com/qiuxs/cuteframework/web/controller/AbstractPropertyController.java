package com.qiuxs.cuteframework.web.controller;

import java.io.Serializable;

import com.qiuxs.cuteframework.core.basic.utils.JsonUtils;
import com.qiuxs.cuteframework.core.persistent.entity.IEntity;
import com.qiuxs.cuteframework.core.persistent.service.ifc.IPropertyService;

public abstract class AbstractPropertyController<PK extends Serializable, T extends IEntity<PK>, S extends IPropertyService<PK, T>> extends BaseController {

	protected abstract S getService();

	protected T fromJSON(String jsonData) {
		return JsonUtils.parseObject(jsonData, this.getService().getPojoClass());
	}
	
}
