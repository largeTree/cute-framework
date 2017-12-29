package com.qiuxs.cuteframework.core.entity.impl;

import java.util.Date;

import com.qiuxs.cuteframework.core.entity.IEntity;

public abstract class AbstractEntity<PK> implements IEntity<PK> {

	private PK id;
	private Date createdDate;
	private Date updatedDate;

	@Override
	public PK getId() {
		return this.id;
	}

	@Override
	public void setId(PK id) {
		this.id = id;
	}

	@Override
	public Date getCreatedDate() {
		return this.createdDate;
	}

	@Override
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	@Override
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

}
