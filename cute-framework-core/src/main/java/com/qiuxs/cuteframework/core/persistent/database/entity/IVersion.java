package com.qiuxs.cuteframework.core.persistent.database.entity;

/**
 * 乐观锁
 * @author qiuxs
 * 2019年3月23日 下午11:02:28
 */
public interface IVersion {

	public Integer getVersion();

	public void setVersion();

}
