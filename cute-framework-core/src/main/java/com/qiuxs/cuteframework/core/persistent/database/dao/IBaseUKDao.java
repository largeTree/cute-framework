package com.qiuxs.cuteframework.core.persistent.database.dao;

import java.io.Serializable;
import java.util.Map;

import com.qiuxs.cuteframework.core.persistent.database.entity.IEntity;

/**
 * 带唯一约束的dao
 * 功能描述: <p>  
 * 新增原因: TODO<p>  
 * 新增日期: 2019年11月26日 上午11:34:16 <p>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public interface IBaseUKDao<PK extends Serializable, T extends IEntity<PK>> extends IBaseDao<PK, T> {
	
	/**
	 * 根据唯一约束获取一行数据
	 *  
	 * @author qiuxs  
	 * @param params
	 * @return
	 */
	public T getByUk(Map<String, Object> params);

	/**
	 * 根据唯一约束获取行数
	 *  
	 * @author qiuxs  
	 * @param params
	 * @return
	 */
	public Long getCountByUk(Map<String, Object> params);
	
}
