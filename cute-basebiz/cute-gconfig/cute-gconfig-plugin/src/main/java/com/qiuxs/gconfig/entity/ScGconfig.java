package com.qiuxs.gconfig.entity;

import com.qiuxs.cuteframework.core.basic.code.annotation.Code;
import com.qiuxs.cuteframework.core.basic.code.annotation.CodeDomain;
import com.qiuxs.cuteframework.core.persistent.database.entity.impl.AbstractEntity;

/**
 * 全局配置实体类
 * 	for table sc_gconfig
 * 
 * 创建时间 ：2020-03-04 10:37:13.
 *
 * @author qiuxs
 */

public class ScGconfig extends AbstractEntity<Long> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**  配置域：系统. */
	public static final String DOMAIN_SYSTEM = "system";
	
	/**  配置域：业务. */
	public static final String DOMAIN_BUSINESS = "business";
	
	/**  配置域：用户. */
	public static final String DOMAIN_USER = "user";

	/** The Constant DOMAIN_INPUT_TYPE. */
	@CodeDomain
	public static final String DOMAIN_INPUT_TYPE = "domain.gConfigInputType";
	
	/** The Constant INPUT_TYPE_SELGIN_SELECT. */
	@Code(caption = "单选", domain = DOMAIN_INPUT_TYPE)
	public static final int INPUT_TYPE_SELGIN_SELECT = 0;
	
	/** The Constant INPUT_TYPE_MUL_SELECT. */
	@Code(caption = "多选", domain = DOMAIN_INPUT_TYPE)
	public static final int INPUT_TYPE_MUL_SELECT = 1;
	
	/** The Constant INPUT_TYPE_TEXT. */
	@Code(caption = "文本", domain = DOMAIN_INPUT_TYPE)
	public static final int INPUT_TYPE_TEXT = 2;

	/**  配置代码. */
	private String code;

	/**  配置名称. */
	private String name;

	/**  配置域. */
	private String domain;

	/**  默认值. */
	private String val;
	
	/**  值翻译. */
	private String valCaption;

	/**  输入类型. */
	private Integer inputType;

	/**  配置类别. */
	private Integer catId;

	/**  显示顺序. */
	private Integer showOrder;

	/**
	 * get the 配置代码.
	 *
	 * @return code
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * set the 配置代码.
	 *
	 * @param code the new 配置代码
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * get the 配置名称.
	 *
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * set the 配置名称.
	 *
	 * @param name the new 配置名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * get the 配置域.
	 *
	 * @return domain
	 */
	public String getDomain() {
		return this.domain;
	}

	/**
	 * set the 配置域.
	 *
	 * @param domain the new 配置域
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * get the 默认值.
	 *
	 * @return val
	 */
	public String getVal() {
		return this.val;
	}

	/**
	 * set the 默认值.
	 *
	 * @param val the new 默认值
	 */
	public void setVal(String val) {
		this.val = val;
	}

	/**
	 * Gets the 值翻译.
	 *
	 * @return the 值翻译
	 */
	public String getValCaption() {
		return valCaption;
	}

	/**
	 * Sets the 值翻译.
	 *
	 * @param valCaption the new 值翻译
	 */
	public void setValCaption(String valCaption) {
		this.valCaption = valCaption;
	}

	/**
	 * get the 输入类型.
	 *
	 * @return inputType
	 */
	public Integer getInputType() {
		return this.inputType;
	}

	/**
	 * set the 输入类型.
	 *
	 * @param inputType the new 输入类型
	 */
	public void setInputType(Integer inputType) {
		this.inputType = inputType;
	}

	/**
	 * get the 配置类别.
	 *
	 * @return catId
	 */
	public Integer getCatId() {
		return this.catId;
	}

	/**
	 * set the 配置类别.
	 *
	 * @param catId the new 配置类别
	 */
	public void setCatId(Integer catId) {
		this.catId = catId;
	}

	/**
	 * get the 显示顺序.
	 *
	 * @return showOrder
	 */
	public Integer getShowOrder() {
		return this.showOrder;
	}

	/**
	 * set the 显示顺序.
	 *
	 * @param showOrder the new 显示顺序
	 */
	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

}