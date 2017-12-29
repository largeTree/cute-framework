package com.qiuxs.cuteframework.core.dao;

import java.io.Serializable;
import java.util.Map;

import com.qiuxs.cuteframework.core.entity.IObject;

public interface IBaseDaoWithBizKeys<PK extends Serializable, T extends IObject<PK>> extends IBaseDao<PK, T> {

	public T getByBizKeys(Map<String, Object> bizKeyVals);

	public void updateByBizKeys(T bean);

	public void deleteByBizKeys(Map<String, Object> bizKeyVals);

}
