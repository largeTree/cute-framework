package com.qiuxs.cuteframework.core.persistent.unit.entity;


import com.qiuxs.cuteframework.core.persistent.database.entity.impl.AbstractEntity;

/**
 * 单元对应数据库关系实体类
 *	for table ds_unit
 * 
 * 创建时间 ：2020-03-10 09:48:14
 * @author qiuxs
 *
 */

public class DsUnit extends AbstractEntity<Long> {

	private static final long serialVersionUID = 1L;
	
	/** 单元ID */
	private Long unitId;

	/** 数据ID */
	private String dsId;


	/**
	 * get the 单元ID
	 * @return unitId
	 */
	public Long getUnitId() {
		return this.unitId;
	}

	/**
	 * set the 单元ID
	 * @param unitId
	 */
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	/**
	 * get the 数据ID
	 * @return dsId
	 */
	public String getDsId() {
		return this.dsId;
	}

	/**
	 * set the 数据ID
	 * @param dsId
	 */
	public void setDsId(String dsId) {
		this.dsId = dsId;
	}

}