package com.qiuxs.cuteframework.core.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import com.qiuxs.cuteframework.core.entity.IObject;

public interface IBaseDeleteDao<PK extends Serializable, T extends IObject<PK>> {

	public void deleteById(PK id);

	public void deleteByIds(Collection<PK> ids);

	public void delete(T bean);

	public void deleteInBatch(Collection<T> beans);

	public void deleteByWhere(Map<String, Object> params);

}
