package com.qiuxs.cuteframework.core.service.filter;

import java.io.Serializable;

import com.qiuxs.cuteframework.core.entity.IObject;

/**
 * 删除过滤器
 * @author qiuxs
 *
 * @param <PK>
 * @param <T>
 */
public interface IDeleteFilter<PK extends Serializable, T extends IObject<PK>> extends IServiceFilter<PK, T> {

	public void preDelete(T bean);

	public void postDelete(T bean);

}