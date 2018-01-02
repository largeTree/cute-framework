package com.qiuxs.cuteframework.core.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.qiuxs.cuteframework.core.dao.IBaseDao;
import com.qiuxs.cuteframework.core.dao.page.PageInfo;
import com.qiuxs.cuteframework.core.entity.IObject;

public interface IDataSourceService<PK extends Serializable, T extends IObject<PK>, D extends IBaseDao<PK, T>> {

	public void insert(T bean);

	public void update(T bean);

	public void save(T bean);

	public void delete(T bean);

	public void delete(Map<String, Object> params);

	public List<T> findAll();

	public List<T> list(Map<String, Object> params);

	public List<T> list(Map<String, Object> params, PageInfo pageInfo);

}
