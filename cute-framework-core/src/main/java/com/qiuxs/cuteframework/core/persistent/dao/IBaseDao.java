package com.qiuxs.cuteframework.core.persistent.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.SelectProvider;

import com.qiuxs.cuteframework.core.persistent.dao.page.PageInfo;
import com.qiuxs.cuteframework.core.persistent.entity.IEntity;
import com.qiuxs.cuteframework.core.persistent.mybatis.dynamicsql.DynamicSqlProvider;

public interface IBaseDao<PK extends Serializable, T extends IEntity<PK>> {

	@SelectProvider(type = DynamicSqlProvider.class, method = DynamicSqlProvider.DYNAMIC_SQL)
	void deleteById(PK id);

	@SelectProvider(type = DynamicSqlProvider.class, method = DynamicSqlProvider.DYNAMIC_SQL)
	T get(PK id);

	@SelectProvider(type = DynamicSqlProvider.class, method = DynamicSqlProvider.DYNAMIC_SQL)
	List<T> getByIds(Collection<PK> ids);

	@SelectProvider(type = DynamicSqlProvider.class, method = DynamicSqlProvider.DYNAMIC_SQL)
	List<T> list(Map<String, Object> params, PageInfo pageInfo);

	@SelectProvider(type = DynamicSqlProvider.class, method = DynamicSqlProvider.DYNAMIC_SQL)
	void insert(T bean);

	@SelectProvider(type = DynamicSqlProvider.class, method = DynamicSqlProvider.DYNAMIC_SQL)
	void update(T bean);
}
