package com.qiuxs.gconfig.entity;


import com.qiuxs.cuteframework.core.basic.code.annotation.Code;
import com.qiuxs.cuteframework.core.basic.code.annotation.CodeDomain;
import com.qiuxs.cuteframework.core.persistent.database.entity.impl.AbstractEntity;

/**
 * 全局配置所有者的值实体类
 *	for table sc_gconfig_owner_val
 * 
 * 创建时间 ：2020-03-04 10:23:40
 * @author qiuxs
 *
 */

public class ScGconfigOwnerVal extends AbstractEntity<Long> {

	private static final long serialVersionUID = 1L;
	
	@CodeDomain
	public static final String DOMAIN_OWNER_TYPE = "domain.gconfigOwnerType";
	@Code(caption = "系统", domain = DOMAIN_OWNER_TYPE)
	public static final int OWNER_TYPE_SYSTEM = 1 << 0;
	@Code(caption = "用户", domain = DOMAIN_OWNER_TYPE)
	public static final int OWNER_TYPE_USER = 1 << 1;
	
	/** 配置代码 */
	private String code;

	/** 默认值 */
	private String val;

	/** 所有者类型 */
	private Integer ownerType;

	/** 所有者id */
	private Long ownerId;


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
	 * get the 默认值
	 * @return val
	 */
	public String getVal() {
		return this.val;
	}

	/**
	 * set the 默认值
	 * @param val
	 */
	public void setVal(String val) {
		this.val = val;
	}

	/**
	 * get the 所有者类型
	 * @return ownerType
	 */
	public Integer getOwnerType() {
		return this.ownerType;
	}

	/**
	 * set the 所有者类型
	 * @param ownerType
	 */
	public void setOwnerType(Integer ownerType) {
		this.ownerType = ownerType;
	}

	/**
	 * get the 所有者id
	 * @return ownerId
	 */
	public Long getOwnerId() {
		return this.ownerId;
	}

	/**
	 * set the 所有者id
	 * @param ownerId
	 */
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

}