package com.qiuxs.gconfig.mq;

import java.io.Serializable;

public class GConfigUpdateDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 作用域 */
	private String domain;
	/** 所有者类型 */
	private Integer ownerType;
	/** 所有者ID */
	private Long ownerId;
	/** 配置编码 */
	private String code;
	
	public GConfigUpdateDTO() {}

	public GConfigUpdateDTO(String domain, Integer ownerType, Long ownerId, String code) {
		super();
		this.domain = domain;
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.code = code;
	}


	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Integer getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(Integer ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
