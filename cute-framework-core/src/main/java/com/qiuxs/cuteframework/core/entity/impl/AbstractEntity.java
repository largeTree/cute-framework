package com.qiuxs.cuteframework.core.entity.impl;

import java.util.Date;

import com.qiuxs.cuteframework.core.entity.IEntity;

public abstract class AbstractEntity<PK> implements IEntity<PK> {

	private PK id;
	private Long createdBy;
	private Date createdDate;
	private Long updatedBy;
	private Date updatedDate;
	private Long deletedBy;
	private Date deletedDate;

	@Override
	public PK getId() {
		return this.id;
	}

	@Override
	public void setId(PK id) {
		this.id = id;
	}

	@Override
	public Long getCreatedBy() {
		return createdBy;
	}

	@Override
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
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

	@Override
	public Long getUpdatedBy() {
		return updatedBy;
	}

	@Override
	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Override
	public Long getDeletedBy() {
		return this.deletedBy;
	}

	@Override
	public void setDeletedBy(Long deletedBy) {
		this.deletedBy = deletedBy;
	}

	@Override
	public Date getDeletedDate() {
		return this.deletedDate;
	}

	@Override
	public void setDeletedDate(Date deletedDate) {
		this.deletedDate = deletedDate;
	}

}
