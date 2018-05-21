package com.qiuxs.cuteframework.core.persistent.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.qiuxs.cuteframework.core.persistent.dao.page.PageInfo;
import com.qiuxs.cuteframework.core.persistent.entity.IEntity;

public interface IBaseDao<PK extends Serializable, T extends IEntity<PK>> {

	void deleteById(PK id);

	T get(PK id);

	List<T> getByIds(Collection<PK> ids);

	List<T> list(Map<String, Object> params, PageInfo pageInfo);

	void insert(T bean);

	void update(T bean);
}
