package com.qiuxs.gconfig.entity;


import com.qiuxs.cuteframework.core.persistent.database.entity.impl.AbstractEntity;

/**
 * 全局配置选项实体类
 *	for table sc_gconfig_options
 * 
 * 创建时间 ：2020-03-04 10:23:39
 * @author qiuxs
 *
 */

public class ScGconfigOptions extends AbstractEntity<Long> {

	private static final long serialVersionUID = 1L;
	
	/** 配置代码 */
	private String code;

	/** 配置选项名称 */
	private String name;

	/** 选项值 */
	private String optVal;

	/** 显示顺序 */
	private Integer showOrder;


	/**
	 * get the 配置代码
	 * @return code
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * set the 配置代码
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * get the 配置选项名称
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * set the 配置选项名称
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * get the 选项值
	 * @return optVal
	 */
	public String getOptVal() {
		return this.optVal;
	}

	/**
	 * set the 选项值
	 * @param optVal
	 */
	public void setOptVal(String optVal) {
		this.optVal = optVal;
	}

	/**
	 * get the 显示顺序
	 * @return showOrder
	 */
	public Integer getShowOrder() {
		return this.showOrder;
	}

	/**
	 * set the 显示顺序
	 * @param showOrder
	 */
	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

}