package com.qiuxs.cuteframework.core.basic.bean;

import java.io.Serializable;

public interface IObject<PK> extends Serializable {

	/**
	 * 获取ID
	 * @return
	 */
	public PK getId();

	/**
	 * 设置ID
	 * @param id
	 */
	public void setId(PK id);

}
