package com.qiuxs.cuteframework.core.persistent.database.entity;

import java.util.Date;

import com.qiuxs.cuteframework.core.basic.bean.IObject;

/**
 * 创建修改信息
 * 
 * @author qiuxs
 *
 * @param <PK>
 */
public interface IEntity<PK> extends IObject<PK> {

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
	
	/***
	 * 获取删除时间
	 * 
	 * 2019年3月23日 下午10:58:00
	 * @auther qiuxs
	 * @return
	 */
	public Date getDeletedTime();
	
	/**
	 * 设置删除时间
	 * 
	 * 2019年3月23日 下午10:58:07
	 * @auther qiuxs
	 * @param deletedTime
	 */
	public void setDeletedTime(Date deletedTime);

	/**
	 * 获取删除人ID
	 * 
	 * 2019年3月23日 下午10:58:43
	 * @auther qiuxs
	 * @return
	 */
	public Long getDeletedBy();

	/**
	 * 设置删除人ID
	 * 
	 * 2019年3月23日 下午10:59:21
	 * @auther qiuxs
	 * @param deletedBy
	 */
	public void setDeletedBy(Long deletedBy);

}
