package com.qiuxs.cuteframework.core.persistent.entity.impl;

import java.util.Date;

import com.qiuxs.cuteframework.core.persistent.annotations.Column;
import com.qiuxs.cuteframework.core.persistent.annotations.Id;
import com.qiuxs.cuteframework.core.persistent.entity.IEntity;

public abstract class AbstractEntity<PK> implements IEntity<PK> {

	private static final long serialVersionUID = -7526268408245513295L;
	@Id()
	private PK id;
	@Column("created_by")
	private Long createdBy;
	@Column("created_time")
	private Date createdTime;
	@Column("updated_by")
	private Long updatedBy;
	@Column("updated_time")
	private Date updatedTime;

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
	public Date getCreatedTime() {
		return this.createdTime;
	}

	@Override
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	@Override
	public Date getUpdatedTime() {
		return this.updatedTime;
	}

	@Override
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	@Override
	public Long getUpdatedBy() {
		return updatedBy;
	}

	@Override
	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

}
