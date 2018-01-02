package com.qiuxs.cuteframework.core.service.filter;

import java.io.Serializable;

import com.qiuxs.cuteframework.core.entity.IObject;

/**
 * 新增记录时调用
 * @author qiuxs
 *
 * @param <PK>
 * @param <T>
 */
public interface IInsertFilter<PK extends Serializable, T extends IObject<PK>> extends IServiceFilter<PK, T> {

	public void preInsert();

	public void preInsert(T bean);

	public void postInsert();

	public void postInsert(T bean);

}
