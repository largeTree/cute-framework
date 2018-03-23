package com.qiuxs.cuteframework.core.persistent.service;

import java.io.Serializable;

import com.qiuxs.cuteframework.core.persistent.dao.IBaseDaoWithBizKeys;
import com.qiuxs.cuteframework.core.persistent.entity.IObject;

public interface IDataSourceWithBizKeysService<PK extends Serializable, T extends IObject<PK>, D extends IBaseDaoWithBizKeys<PK, T>> extends IDataPropertyService<PK, T, D> {

	public void updateByBizKeys(T bean);

}
