package com.qiuxs.cuteframework.core.persistent.service.filter;

import java.io.Serializable;

import com.qiuxs.cuteframework.core.persistent.entity.IObject;

/**
 * 新增记录时调用
 * @author qiuxs
 *
 * @param <PK>
 * @param <T>
 */
public interface IInsertFilter<PK extends Serializable, T extends IObject<PK>> extends IServiceFilter<PK, T> {

	public void preInsert(T bean);

	public void postInsert(T bean);

}