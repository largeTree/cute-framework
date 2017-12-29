package com.qiuxs.cuteframework.core.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.qiuxs.cuteframework.core.dao.page.PageInfo;
import com.qiuxs.cuteframework.core.entity.IObject;

public interface IBaseQueryDao<PK extends Serializable, T extends IObject<PK>> {

	public T getById(PK id);

	public List<T> getByIds(Collection<PK> ids);

	public List<T> list(Map<String, Object> params);

	public List<T> list(Map<String, Object> params, PageInfo pageInfo);

	public List<T> findAll();

}
