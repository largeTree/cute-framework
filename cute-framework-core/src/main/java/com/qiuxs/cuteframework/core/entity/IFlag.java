package com.qiuxs.cuteframework.core.entity;

public interface IFlag {

	public static final Integer VALID = 1;
	public static final Integer INVALID = 0;
	public static final Integer DELETED = -1;

	public Integer getFlag();

	public void setFlag();

}
