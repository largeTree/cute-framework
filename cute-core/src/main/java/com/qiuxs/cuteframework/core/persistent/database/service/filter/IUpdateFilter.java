package com.qiuxs.cuteframework.core.persistent.database.service.filter;

import java.io.Serializable;

import com.qiuxs.cuteframework.core.persistent.database.entity.IEntity;

/**
 * 更新行时调用
 * 
 * @author qiuxs
 *
 * @param <PK>
 * @param <T>
 */
public interface IUpdateFilter<PK extends Serializable, T extends IEntity<PK>> extends IServiceFilter<PK, T> {

	public void preUpdate(T oldBean, T newBean);

	public void postUpdate(T oldBean, T newBean);

}