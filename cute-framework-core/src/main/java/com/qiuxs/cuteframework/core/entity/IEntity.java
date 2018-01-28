package com.qiuxs.cuteframework.core.entity;

import java.util.Date;

public interface IEntity<PK> extends IObject<PK> {

	public Long getCreatedBy();

	public void setCreatedBy(Long createdBy);

	public Date getCreatedDate();

	public void setCreatedDate(Date createdDate);

	public Long getUpdatedBy();

	public void setUpdatedBy(Long updatedBy);

	public Date getUpdatedDate();

	public void setUpdatedDate(Date updatedDate);

	public Long getDeletedBy();

	public void setDeletedBy(Long deletedBy);
	
	public Date getDeletedDate();
	
	public void setDeletedDate(Date deletedDate);

}
