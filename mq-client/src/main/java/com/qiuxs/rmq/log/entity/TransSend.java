package com.qiuxs.rmq.log.entity;

import java.util.Date;

import com.qiuxs.cuteframework.core.basic.bean.IObject;

public class TransSend implements IObject<Long> {

	private static final long serialVersionUID = -3100148192767789793L;

	/** 事务ID */
	private Long id;
	/** 单元ID */
	private Long unitId;
	/** 创建时间 */
	private Date createdTime;
	
	public TransSend() {
	}

	public TransSend(Long id, Long unitId) {
		super();
		this.id = id;
		this.unitId = unitId;
		this.createdTime = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

}
