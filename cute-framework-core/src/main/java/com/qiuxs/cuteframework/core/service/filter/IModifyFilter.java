package com.qiuxs.cuteframework.core.service.filter;

import java.io.Serializable;

import com.qiuxs.cuteframework.core.entity.IObject;

/**
 * 表记录变更的时候调用(包含新增，修改，删除)
 * @author qiuxs
 *
 * @param <PK>
 * @param <T>
 */
public interface IModifyFilter<PK extends Serializable, T extends IObject<PK>> extends IServiceFilter<PK, T> {

	public void preModify();

	public void preModify(T bean);

	public void postModify();

	public void postModify(T bean);

}