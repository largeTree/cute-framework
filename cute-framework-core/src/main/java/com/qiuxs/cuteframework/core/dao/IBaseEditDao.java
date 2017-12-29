package com.qiuxs.cuteframework.core.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import com.qiuxs.cuteframework.core.entity.IObject;

public interface IBaseEditDao<PK extends Serializable, B extends IObject<PK>> {

	public void insert(B bean);

	public void insertInBatch(Collection<B> beans);

	public void update(B bean);

	public void updateInBatch(Collection<B> beans);

	public void updateByMap(Map<String, Object> mBean);

}
