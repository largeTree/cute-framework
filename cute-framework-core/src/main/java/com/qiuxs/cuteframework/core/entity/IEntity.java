package com.qiuxs.cuteframework.core.entity;

import java.util.Date;

public interface IEntity<PK> extends IObject<PK> {

	public Date getCreatedDate();

	public void setCreatedDate(Date createdDate);

	public Date getUpdatedDate();

	public void setUpdatedDate(Date updatedDate);

}
