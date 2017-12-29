package com.qiuxs.cuteframework.core.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import com.qiuxs.cuteframework.core.entity.IObject;

public interface IBaseEditDao<PK extends Serializable, T extends IObject<PK>> {

	public int insert(T bean);

	public int insertInBatch(Collection<T> beans);

	public int update(T bean);

	public int updateInBatch(Collection<T> beans);

	public int updateByMap(Map<String, Object> mBean);

}
