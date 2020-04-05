package com.qiuxs.cuteframework.core.persistent.database.entity.impl;

import com.qiuxs.cuteframework.core.persistent.database.entity.IUnitId;

public abstract class BizEntity<PK> extends AbstractEntity<PK> implements IUnitId {

	private static final long serialVersionUID = 8079241446105140606L;
	
	/** 单元ID  */
	private Long unitId;

	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

}
