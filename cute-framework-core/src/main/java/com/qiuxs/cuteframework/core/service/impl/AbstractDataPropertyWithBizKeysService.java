package com.qiuxs.cuteframework.core.service.impl;

import java.io.Serializable;

import com.qiuxs.cuteframework.core.basic.utils.MapUtils;
import com.qiuxs.cuteframework.core.dao.IBaseDaoWithBizKeys;
import com.qiuxs.cuteframework.core.entity.IObject;
import com.qiuxs.cuteframework.core.service.IDataSourceWithBizKeysService;

public abstract class AbstractDataPropertyWithBizKeysService<PK extends Serializable, T extends IObject<PK>, D extends IBaseDaoWithBizKeys<PK, T>> extends AbstractDataPropertyService<PK, T, D> implements IDataSourceWithBizKeysService<PK, T, D> {
	
	protected T getByBizKeysInner(Object keyValues) {
		return this.getDao().getByBizKeys(MapUtils.genMap(keyValues));
	}
	
	@Override
	public void updateByBizKeys(T bean) {
		this.getDao().updateByBizKeys(bean);
	}

}
