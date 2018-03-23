package com.qiuxs.cuteframework.core.persistent.entity;

/**
 * 带有主键的对象
 * @author qiuxs
 *
 * @param <PK>
 */
public interface IObject<PK> {

	/**
	 * 获取主键
	 * @return
	 */
	public PK getId();

	/**
	 * 设置主键
	 * @param id
	 */
	public void setId(PK id);

}
