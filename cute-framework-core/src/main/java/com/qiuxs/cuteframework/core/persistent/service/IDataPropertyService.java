package com.qiuxs.cuteframework.core.persistent.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.qiuxs.cuteframework.core.persistent.dao.IBaseDao;
import com.qiuxs.cuteframework.core.persistent.dao.page.PageInfo;
import com.qiuxs.cuteframework.core.persistent.entity.IObject;

/**
 * 提供
 * @author qiuxs
 *
 * @param <PK>
 * @param <T>
 * @param <D>
 */
public interface IDataPropertyService<PK extends Serializable, T extends IObject<PK>, D extends IBaseDao<PK, T>> {

	public void insert(T bean);

	public void insertInBatch(Collection<T> beans);

	public void update(T bean);

	public void updateInBatch(Collection<T> beans);

	public void save(T bean);

	public void delete(T bean);

	public List<T> findAll();

	public List<T> findByWhere(Map<String, Object> params, PageInfo pageInfo);

}
