package com.qiuxs.cuteframework.core.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import com.qiuxs.cuteframework.core.entity.IObject;

public interface IBaseEditDao<PK extends Serializable, T extends IObject<PK>> {

	public void insert(T bean);

	public void insertInBatch(Collection<T> beans);

	public void update(T bean);

	public void updateInBatch(Collection<T> beans);

	public void updateByMap(Map<String, Object> mBean);

}
