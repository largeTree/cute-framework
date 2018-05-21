package com.qiuxs.cuteframework.core.persistent.service.filter;

import java.io.Serializable;

import com.qiuxs.cuteframework.core.persistent.entity.IEntity;

/**
 * 删除过滤器
 * @author qiuxs
 *
 * @param <PK>
 * @param <T>
 */
public interface IDeleteFilter<PK extends Serializable, T extends IEntity<PK>> extends IServiceFilter<PK, T> {

	public void preDelete(T bean);

	public void postDelete(T bean);

}