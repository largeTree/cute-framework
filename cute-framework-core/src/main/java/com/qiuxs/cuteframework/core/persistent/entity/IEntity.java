package com.qiuxs.cuteframework.core.persistent.entity;

import java.util.Date;

/**
 * 创建修改信息
 * @author qiuxs
 *
 * @param <PK>
 */
public interface IEntity<PK> extends IObject<PK> {

	/**
	 * 获取创建人
	 * @return
	 */
	public Long getCreatedBy();

	/**
	 * 设置创建人
	 * @param createdBy
	 */
	public void setCreatedBy(Long createdBy);

	/**
	 * 获取创建时间
	 * @return
	 */
	public Date getCreatedDate();

	/**
	 * 设置创建时间
	 * @param createdDate
	 */
	public void setCreatedDate(Date createdDate);

	/**
	 * 获取更新人
	 * @return
	 */
	public Long getUpdatedBy();

	/**
	 * 设置更新人
	 * @param updatedBy
	 */
	public void setUpdatedBy(Long updatedBy);

	/**
	 * 获取更新时间
	 * @return
	 */
	public Date getUpdatedDate();

	/**
	 * 设置更新时间
	 * @param updatedDate
	 */
	public void setUpdatedDate(Date updatedDate);

	/**
	 * 获取删除人
	 * @return
	 */
	public Long getDeletedBy();

	/**
	 * 设置删除人
	 * @param deletedBy
	 */
	public void setDeletedBy(Long deletedBy);
	
	/**
	 * 获取删除日期
	 * @return
	 */
	public Date getDeletedDate();
	
	/**
	 * 设置删除日期
	 * @param deletedDate
	 */
	public void setDeletedDate(Date deletedDate);

}
