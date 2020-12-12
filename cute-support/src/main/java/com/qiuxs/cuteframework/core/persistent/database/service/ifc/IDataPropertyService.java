package com.qiuxs.cuteframework.core.persistent.database.service.ifc;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.qiuxs.cuteframework.core.persistent.database.dao.page.PageInfo;
import com.qiuxs.cuteframework.core.persistent.database.entity.IEntity;

/**
 * 提供基础的增上改查相关定义
 * 
 * @author qiuxs
 *
 * @param <PK>
 * @param <T>
 */
public interface IDataPropertyService<PK extends Serializable, T extends IEntity<PK>> extends IPropertyService<PK, T>, IDataSourceService {

	public void setId(T bean);

	public void create(T bean);

	public void createInBatch(List<T> beans);

	public void save(T bean);

	public int update(T newBean);
	
	public int updateDirect(T newBean);

	public T get(PK pk);
	
	public List<T> getAll();
	
	public T getMust(PK pk);

	public List<T> getByIds(Collection<PK> ids);

	public List<T> findByMap(Map<String, Object> params, PageInfo pageInfo);
	
	public List<T> findByMap(Map<String, Object> params);
	
	public T findByMapSingle(Map<String, Object> params);

	public int delete(T bean);
	
	public int deleteById(PK pk);

	public void disable(PK pk);

	public void enable(PK pk);

	public int deleteDirect(PK id);

	public Long getCountByMap(Map<String, Object> params);


}
