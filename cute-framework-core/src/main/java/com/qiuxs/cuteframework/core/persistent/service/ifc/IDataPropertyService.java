package com.qiuxs.cuteframework.core.persistent.service.ifc;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.qiuxs.cuteframework.core.persistent.dao.IBaseDao;
import com.qiuxs.cuteframework.core.persistent.dao.page.PageInfo;
import com.qiuxs.cuteframework.core.persistent.entity.IEntity;

/**
 * 提供
 * 
 * @author qiuxs
 *
 * @param <PK>
 * @param <T>
 * @param <D>
 */
public interface IDataPropertyService<PK extends Serializable, T extends IEntity<PK>, D extends IBaseDao<PK, T>> {

	public void create(T bean);

	public void save(T bean);

	public void update(T newBean);

	public T getById(PK id);

	public List<T> getByIds(Collection<PK> ids);

	public List<T> findByMap(final Map<String, Object> params, PageInfo pageInfo);

	public void deleteById(PK id);
}
