package com.qiuxs.cuteframework.core.persistent.service.filter;

import java.io.Serializable;

import com.qiuxs.cuteframework.core.persistent.entity.IObject;

/**
 * 更新行时调用
 * @author qiuxs
 *
 * @param <PK>
 * @param <T>
 */
public interface IUpdateFilter<PK extends Serializable, T extends IObject<PK>> extends IServiceFilter<PK, T> {

	public void preUpdate(T bean);

	public void postUpdate(T bean);

}