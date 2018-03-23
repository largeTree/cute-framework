package com.qiuxs.cuteframework.core.persistent.dao;

import java.io.Serializable;

import com.qiuxs.cuteframework.core.persistent.entity.IObject;

public interface IBaseDao<PK extends Serializable, T extends IObject<PK>> extends IBaseEditDao<PK, T>, IBaseQueryDao<PK, T> {
	
}
