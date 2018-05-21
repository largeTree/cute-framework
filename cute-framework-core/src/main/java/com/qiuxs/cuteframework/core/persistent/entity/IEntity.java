package com.qiuxs.cuteframework.core.persistent.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 创建修改信息
 * 
 * @author qiuxs
 *
 * @param <PK>
 */
public interface IEntity<PK> extends Serializable {

	/**
	 * 获取ID
	 * @return
	 */
	public PK getId();

	/**
	 * 设置ID
	 * @param id
	 */
	public void setId(PK id);

	/**
	 * 获取创建人
	 * 
	 * @return
	 */
	public Long getCreatedBy();

	/**
	 * 设置创建人
	 * 
	 * @param createdBy
	 */
	public void setCreatedBy(Long createdBy);

	/**
	 * 获取创建时间
	 * 
	 * @return
	 */
	public Date getCreatedTime();

	/**
	 * 设置创建时间
	 * 
	 * @param createdDate
	 */
	public void setCreatedTime(Date createdDate);

	/**
	 * 获取更新人
	 * 
	 * @return
	 */
	public Long getUpdatedBy();

	/**
	 * 设置更新人
	 * 
	 * @param updatedBy
	 */
	public void setUpdatedBy(Long updatedBy);

	/**
	 * 获取更新时间
	 * 
	 * @return
	 */
	public Date getUpdatedTime();

	/**
	 * 设置更新时间
	 * 
	 * @param updatedDate
	 */
	public void setUpdatedTime(Date updatedDate);

}
