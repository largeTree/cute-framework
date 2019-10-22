package com.qiuxs.cuteframework.view.datamodel;

import java.io.Serializable;

public class DataSourceInfo implements Serializable {

	private static final long serialVersionUID = 8529928896201986979L;

	private String dsId;
	private int maxIdle;
	private int maxTotal;
	private long maxWaitMillis;
	private Boolean defaultAutoCommit;
	private int numActive;
	private int numIdle;
	private String driverClassName;
	private String validationQuery;

	public String getDsId() {
		return dsId;
	}

	public void setDsId(String dsId) {
		this.dsId = dsId;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public long getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(long maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public Boolean getDefaultAutoCommit() {
		return defaultAutoCommit;
	}

	public void setDefaultAutoCommit(Boolean defaultAutoCommit) {
		this.defaultAutoCommit = defaultAutoCommit;
	}

	public int getNumActive() {
		return numActive;
	}

	public void setNumActive(int numActive) {
		this.numActive = numActive;
	}

	public int getNumIdle() {
		return numIdle;
	}

	public void setNumIdle(int numIdle) {
		this.numIdle = numIdle;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getValidationQuery() {
		return validationQuery;
	}

	public void setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
	}

}
