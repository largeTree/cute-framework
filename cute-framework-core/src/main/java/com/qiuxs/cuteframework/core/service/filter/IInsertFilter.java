package com.qiuxs.cuteframework.core.service.filter;

import java.io.Serializable;

import com.qiuxs.cuteframework.core.entity.IObject;

public interface IInsertFilter<PK extends Serializable, T extends IObject<PK>> extends IServiceFilter<PK, T> {

	public void preInsert(T bean);

	public void postInsert(T bean);

}
