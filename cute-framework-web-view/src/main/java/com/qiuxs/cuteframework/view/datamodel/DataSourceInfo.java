/*
 * 
 */
package com.qiuxs.cuteframework.view.datamodel;

import java.io.Serializable;

/**
 * 数据源状态信息.
 *
 * @author qiuxs
 */
public class DataSourceInfo implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8529928896201986979L;

	/**  数据源ID. */
	private String dsId;
	
	/**  最大空闲数. */
	private int maxIdle;
	
	/**  最大链接数. */
	private int maxTotal;
	
	/**  最大等待时间. */
	private long maxWaitMillis;
	
	/**  自动提交. */
	private Boolean defaultAutoCommit;
	
	/**  当前活动链接. */
	private int numActive;
	
	/**  当前空闲链接. */
	private int numIdle;
	
	/**  驱动类名. */
	private String driverClassName;
	
	/**  验证语句. */
	private String validationQuery;
	
	/**  是否已关闭. */
	private Boolean closed;

	/**
	 * Gets the 数据源ID.
	 *
	 * @return the 数据源ID
	 */
	public String getDsId() {
		return dsId;
	}

	/**
	 * Sets the 数据源ID.
	 *
	 * @param dsId the new 数据源ID
	 */
	public void setDsId(String dsId) {
		this.dsId = dsId;
	}

	/**
	 * Gets the 最大空闲数.
	 *
	 * @return the 最大空闲数
	 */
	public int getMaxIdle() {
		return maxIdle;
	}

	/**
	 * Sets the 最大空闲数.
	 *
	 * @param maxIdle the new 最大空闲数
	 */
	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	/**
	 * Gets the 最大链接数.
	 *
	 * @return the 最大链接数
	 */
	public int getMaxTotal() {
		return maxTotal;
	}

	/**
	 * Sets the 最大链接数.
	 *
	 * @param maxTotal the new 最大链接数
	 */
	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	/**
	 * Gets the 最大等待时间.
	 *
	 * @return the 最大等待时间
	 */
	public long getMaxWaitMillis() {
		return maxWaitMillis;
	}

	/**
	 * Sets the 最大等待时间.
	 *
	 * @param maxWaitMillis the new 最大等待时间
	 */
	public void setMaxWaitMillis(long maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	/**
	 * Gets the 自动提交.
	 *
	 * @return the 自动提交
	 */
	public Boolean getDefaultAutoCommit() {
		return defaultAutoCommit;
	}

	/**
	 * Sets the 自动提交.
	 *
	 * @param defaultAutoCommit the new 自动提交
	 */
	public void setDefaultAutoCommit(Boolean defaultAutoCommit) {
		this.defaultAutoCommit = defaultAutoCommit;
	}

	/**
	 * Gets the 当前活动链接.
	 *
	 * @return the 当前活动链接
	 */
	public int getNumActive() {
		return numActive;
	}

	/**
	 * Sets the 当前活动链接.
	 *
	 * @param numActive the new 当前活动链接
	 */
	public void setNumActive(int numActive) {
		this.numActive = numActive;
	}

	/**
	 * Gets the 当前空闲链接.
	 *
	 * @return the 当前空闲链接
	 */
	public int getNumIdle() {
		return numIdle;
	}

	/**
	 * Sets the 当前空闲链接.
	 *
	 * @param numIdle the new 当前空闲链接
	 */
	public void setNumIdle(int numIdle) {
		this.numIdle = numIdle;
	}

	/**
	 * Gets the 驱动类名.
	 *
	 * @return the 驱动类名
	 */
	public String getDriverClassName() {
		return driverClassName;
	}

	/**
	 * Sets the 驱动类名.
	 *
	 * @param driverClassName the new 驱动类名
	 */
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	/**
	 * Gets the 验证语句.
	 *
	 * @return the 验证语句
	 */
	public String getValidationQuery() {
		return validationQuery;
	}

	/**
	 * Sets the 验证语句.
	 *
	 * @param validationQuery the new 验证语句
	 */
	public void setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
	}

	/**
	 * Gets the 是否已关闭.
	 *
	 * @return the 是否已关闭
	 */
	public Boolean getClosed() {
		return closed;
	}

	/**
	 * Sets the 是否已关闭.
	 *
	 * @param closed the new 是否已关闭
	 */
	public void setClosed(Boolean closed) {
		this.closed = closed;
	}

}
