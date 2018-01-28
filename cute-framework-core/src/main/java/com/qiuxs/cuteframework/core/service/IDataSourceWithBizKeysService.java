package com.qiuxs.cuteframework.core.service;

import java.io.Serializable;

import com.qiuxs.cuteframework.core.dao.IBaseDaoWithBizKeys;
import com.qiuxs.cuteframework.core.entity.IObject;

public interface IDataSourceWithBizKeysService<PK extends Serializable, T extends IObject<PK>, D extends IBaseDaoWithBizKeys<PK, T>> extends IDataPropertyService<PK, T, D> {

	public T getByBizKeys(Object...bizKeyAndValues);
	
	public void updateByBizKeys(T bean);

}
