package com.qiuxs.rmq.log.entity;

import java.util.Date;

import com.qiuxs.cuteframework.core.basic.bean.IObject;

/**
 * 事务接收对象
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年4月3日 下午10:37:34 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class TransRevc implements IObject<Long> {

	private static final long serialVersionUID = 2835989604966287037L;

	/** 事务ID */
	private Long id;
	
	/** 单元ID */
	private Long unitId;
	
	/** 创建时间 */
	private Date createdTime;

	public TransRevc(Long id, Long unitId) {
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
