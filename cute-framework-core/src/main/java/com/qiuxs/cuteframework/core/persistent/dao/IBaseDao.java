package com.qiuxs.cuteframework.core.persistent.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.SelectProvider;

import com.qiuxs.cuteframework.core.persistent.dao.page.PageInfo;
import com.qiuxs.cuteframework.core.persistent.entity.IEntity;
import com.qiuxs.cuteframework.core.persistent.mybatis.BaseMyBatisSelectProvider;

public interface IBaseDao<PK extends Serializable, T extends IEntity<PK>> {

	void deleteById(PK id);

	@SelectProvider(type = BaseMyBatisSelectProvider.class, method = "get")
	T get(PK id);

	List<T> getByIds(Collection<PK> ids);

	List<T> list(Map<String, Object> params, PageInfo pageInfo);

	void insert(T bean);

	void update(T bean);
}
