package com.qiuxs.cuteframework.core.service.impl;

import java.io.Serializable;

import com.qiuxs.cuteframework.core.dao.IBaseDaoWithBizKeys;
import com.qiuxs.cuteframework.core.entity.IObject;
import com.qiuxs.cuteframework.core.service.IDataSourceWithBizKeysService;

public abstract class AbstractDataSourceWithBizKeysService<PK extends Serializable, T extends IObject<PK>, D extends IBaseDaoWithBizKeys<PK, T>> extends AbstractDataSourceService<PK, T, D> implements IDataSourceWithBizKeysService<PK, T, D> {

	@Override
	public T getByBizKeys(Object... bizKeyAndValues) {
	    return null;
	}
	
	@Override
	public void updateByBizKeys(T bean) {

	}

}
