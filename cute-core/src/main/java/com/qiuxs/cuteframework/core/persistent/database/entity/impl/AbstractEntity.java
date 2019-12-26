package com.qiuxs.cuteframework.core.persistent.database.entity.impl;

import java.util.Date;

import com.qiuxs.cuteframework.core.persistent.database.entity.IEntity;

/**
 * 基础抽象实体类
 * @author qiuxs
 * 2019年3月23日 下午11:00:28
 * @param <PK>
 */
public abstract class AbstractEntity<PK> implements IEntity<PK> {

	private static final long serialVersionUID = -7526268408245513295L;
	/** 主键 */
	private PK id;
	/** 创建人 */
	private Long createdBy;
	/** 创建时间 */
	private Date createdTime;
	/** 更新人 */
	private Long updatedBy;
	/** 更新时间 */
	private Date updatedTime;
	/** 删除人 */
	private Long deletedBy;
	/** 删除时间 */
	private Date deletedTime;

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

	@Override
	public Long getDeletedBy() {
		return deletedBy;
	}

	@Override
	public void setDeletedBy(Long deletedBy) {
		this.deletedBy = deletedBy;
	}

	@Override
	public Date getDeletedTime() {
		return deletedTime;
	}

	@Override
	public void setDeletedTime(Date deletedTime) {
		this.deletedTime = deletedTime;
	}

}
