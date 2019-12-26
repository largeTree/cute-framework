package com.qiuxs.cuteframework.web.common.biz.func.entity;


import com.qiuxs.cuteframework.core.persistent.database.entity.impl.AbstractEntity;

/**
 * 功能菜单表实体类
 *	for table ct_func
 * 
 * 创建时间 ：2019-07-11 22:36:22
 * @author qiuxs
 *
 */

public class Func extends AbstractEntity<String> {

	private static final long serialVersionUID = 1L;
	
	/** 上级菜单ID */
	private String parentId;

	/** 菜单深度 */
	private Integer level;

	/** 菜单类型 */
	private Integer funcType;

	/** 菜单名 */
	private String name;

	/** 终端类型 */
	private Long termCap;

	/** 显示顺序 */
	private Integer showOrder;

	/** 扩展属性 */
	private String extra;

	/** 备注 */
	private String rem;


	/**
	 * get the 上级菜单ID
	 * @return parentId
	 */
	public String getParentId() {
		return this.parentId;
	}

	/**
	 * set the 上级菜单ID
	 * @param parentId
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * get the 菜单深度
	 * @return level
	 */
	public Integer getLevel() {
		return this.level;
	}

	/**
	 * set the 菜单深度
	 * @param level
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}

	/**
	 * get the 菜单类型
	 * @return funcType
	 */
	public Integer getFuncType() {
		return this.funcType;
	}

	/**
	 * set the 菜单类型
	 * @param funcType
	 */
	public void setFuncType(Integer funcType) {
		this.funcType = funcType;
	}

	/**
	 * get the 菜单名
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * set the 菜单名
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * get the 终端类型
	 * @return termCap
	 */
	public Long getTermCap() {
		return this.termCap;
	}

	/**
	 * set the 终端类型
	 * @param termCap
	 */
	public void setTermCap(Long termCap) {
		this.termCap = termCap;
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

	/**
	 * get the 扩展属性
	 * @return extra
	 */
	public String getExtra() {
		return this.extra;
	}

	/**
	 * set the 扩展属性
	 * @param extra
	 */
	public void setExtra(String extra) {
		this.extra = extra;
	}

	/**
	 * get the 备注
	 * @return rem
	 */
	public String getRem() {
		return this.rem;
	}

	/**
	 * set the 备注
	 * @param rem
	 */
	public void setRem(String rem) {
		this.rem = rem;
	}

}