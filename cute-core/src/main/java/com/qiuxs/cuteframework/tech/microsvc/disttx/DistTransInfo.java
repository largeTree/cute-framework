package com.qiuxs.cuteframework.tech.microsvc.disttx;

import java.io.Serializable;

public class DistTransInfo implements Serializable {

	private static final long serialVersionUID = 472211405624884318L;

	/**  单元ID. */
	private Long unitId;
	
	/**  事务ID. */
	private Long txId;
	
	public DistTransInfo() {
	}
	
	public DistTransInfo(Long unitId, Long txId) {
		this();
		this.unitId = unitId;
		this.txId = txId;
	}

	/**
	 * Gets the 单元ID.
	 *
	 * @return the 单元ID
	 */
	public Long getUnitId() {
		return unitId;
	}

	/**
	 * Sets the 单元ID.
	 *
	 * @param unitId the new 单元ID
	 */
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	/**
	 * Gets the 事务ID.
	 *
	 * @return the 事务ID
	 */
	public Long getTxId() {
		return txId;
	}

	/**
	 * Sets the 事务ID.
	 *
	 * @param txId the new 事务ID
	 */
	public void setTxId(Long txId) {
		this.txId = txId;
	}

}
