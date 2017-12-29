package com.qiuxs.cuteframework.core.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.qiuxs.cuteframework.core.entity.IObject;
import com.qiuxs.cuteframework.thirdparty.mybatis.PageInfo;

public interface IBaseQueryDao<PK extends Serializable, B extends IObject<PK>> {

	public B getById(PK id);

	public List<B> getByIds(Collection<PK> ids);

	public List<B> list(Map<String, Object> params);

	public List<B> list(Map<String, Object> params, PageInfo pageInfo);

	public List<B> findAll();

}
