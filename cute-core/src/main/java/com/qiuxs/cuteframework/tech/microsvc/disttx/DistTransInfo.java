package com.qiuxs.cuteframework.tech.microsvc.disttx;

import java.io.Serializable;

import com.qiuxs.cuteframework.core.basic.constants.SymbolConstants;

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

	/**
	 * 获取事务Key
	 * @return
	 */
	public String getTxKey() {
		return txKey(this.txId, this.unitId);
	}
	
	/**
	 * 构造事务消息Key
	 * @param txId
	 * @param unitId
	 * @return
	 */
	public static String txKey(Long txId, Long unitId) {
		return txId + SymbolConstants.SEPARATOR_HYPHEN + unitId;
	}
	
	/**
	 * 解析事务消息key
	 * @param txKey
	 * @return
	 */
	public static DistTransInfo parseTxKey(String txKey) {
		String[] split = txKey.split(SymbolConstants.SEPARATOR_HYPHEN);
		return new DistTransInfo(Long.parseLong(split[0]), Long.parseLong(split[0]));
	}

}
