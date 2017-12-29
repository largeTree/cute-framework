package com.qiuxs.cuteframework.core.dao;

import java.io.Serializable;

import com.qiuxs.cuteframework.core.entity.IObject;

public interface IBaseDao<PK extends Serializable, B extends IObject<PK>> extends IBaseEditDao<PK, B>, IBaseQueryDao<PK, B>, IBaseDeleteDao<PK, B> {
	
}
