package com.qiuxs.cuteframework.core.persistent.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.qiuxs.cuteframework.core.basic.ex.ErrorCodes;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.persistent.dao.IBaseDao;
import com.qiuxs.cuteframework.core.persistent.dao.page.PageInfo;
import com.qiuxs.cuteframework.core.persistent.entity.IEntity;
import com.qiuxs.cuteframework.core.persistent.service.filter.IInsertFilter;
import com.qiuxs.cuteframework.core.persistent.service.filter.IServiceFilter;
import com.qiuxs.cuteframework.core.persistent.service.filter.IUpdateFilter;
import com.qiuxs.cuteframework.core.persistent.service.ifc.IDataPropertyService;

/**
 * 
 * 功能描述: <br/>
 * 新增原因: TODO<br/>
 * 新增日期: 2018年4月18日 下午9:16:44 <br/>
 * 
 * @author qiuxs
 * @version 1.0.0
 */
public abstract class AbstractDataService<PK extends Serializable, T extends IEntity<PK>, D extends IBaseDao<PK, T>>
		extends AbstractPropertyService<PK, T> implements IDataPropertyService<PK, T, D> {

	private String tableName;

	private List<IServiceFilter<PK, T>> serviceFilters;
	private List<IInsertFilter<PK, T>> insertFilters;
	private List<IUpdateFilter<PK, T>> updateFilters;

	public AbstractDataService(Class<PK> pkClass, Class<T> pojoClass, String tableName) {
		super(pkClass, pojoClass);
		this.tableName = tableName;
	}

	/**
	 * 获取Dao对象
	 * 
	 * @author qiuxs
	 * @return
	 */
	protected abstract D getDao();

	/**
	 * 获取表名
	 * 
	 * @return
	 */
	public String getTableName() {
		return this.tableName;
	}

	/**
	 * 删除一个对象
	 * 
	 * @see com.qiuxs.frm.persistent.service.IDataService#deleteById(java.lang.Object)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteById(PK id) {
		this.getDao().deleteById(id);
	}

	/**
	 * 根据ID获取一行记录
	 * 
	 * @author qiuxs
	 * @param id
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public T getById(PK id) {
		T bean = this.getDao().get(id);
		if (bean == null) {
			ExceptionUtils.throwLogicalException(ErrorCodes.DataError.INVALID_PRIMARY_KEY, "records_do_not_exists");
		}
		return bean;
	}

	/**
	 * 根据ID获取多行记录
	 * 
	 * @author qiuxs
	 * @param ids
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<T> getByIds(Collection<PK> ids) {
		return this.getDao().getByIds(ids);
	}

	/**
	 * 使用Map作为参数查询
	 * 
	 * @author qiuxs
	 * @param params
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<T> findByMap(final Map<String, Object> params, PageInfo pageInfo) {
		return this.getDao().list(params, pageInfo);
	}

	/**
	 * 新增对象
	 * 
	 * @author qiuxs
	 * @param bean
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void create(T bean) {
		if (preCreate(bean)) {
			this.preSave(null, bean);
			this.getDao().insert(bean);
		}
		postCreate(bean);
		postSave(null, bean);
	}

	protected boolean preCreate(T bean) {
		initCreate(bean);
		return true;
	}

	protected void postCreate(T bean) {
		List<IInsertFilter<PK, T>> insertFilters = this.getInsertFilters();
		for (IInsertFilter<PK, T> filter : insertFilters) {
			filter.postInsert(bean);
		}
	}

	protected void initCreate(T bean) {
		List<IInsertFilter<PK, T>> insertFilters = getInsertFilters();
		for (IInsertFilter<PK, T> filter : insertFilters) {
			filter.preInsert(bean);
		}
		if (bean.getCreatedTime() == null) {
			bean.setCreatedTime(new Date());
		}
	}

	private List<IInsertFilter<PK, T>> getInsertFilters() {
		if (this.insertFilters == null) {
			this.insertFilters = new ArrayList<>();
			List<IServiceFilter<PK, T>> serviceFilters = getServiceFilters();
			for (IServiceFilter<PK, T> filter : serviceFilters) {
				if (filter instanceof IInsertFilter) {
					this.insertFilters.add((IInsertFilter<PK, T>) filter);
				}
			}
		}
		return this.insertFilters;
	}

	public void setId(T bean) {

	}

	/**
	 * 更新对象
	 * 
	 * @see com.qiuxs.frm.persistent.service.IDataService#update(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void update(T newBean) {
		// 默认为Null，需要时自行实现
		T oldBean = this.getOld(newBean.getId());
		if (preUpdate(oldBean, newBean)) {
			preSave(oldBean, newBean);
			this.getDao().update(oldBean);
		}
		postUpdate(oldBean, newBean);
		postSave(oldBean, newBean);
	}

	/**
	 * 获取就记录
	 * 
	 * @param pk
	 * @return
	 */
	protected T getOld(PK pk) {
		return null;
	}

	/**
	 * 更新前操作
	 * 
	 * @author qiuxs
	 * @param oldBean
	 * @param newBean
	 * @return
	 */
	protected boolean preUpdate(T oldBean, T newBean) {
		initUpdate(oldBean, newBean);
		return true;
	}

	private void initUpdate(T oldBean, T newBean) {
		List<IUpdateFilter<PK, T>> updateFilters = this.getUpdateFilters();
		for (IUpdateFilter<PK, T> filter : updateFilters) {
			filter.preUpdate(oldBean, newBean);
		}
		newBean.setUpdatedTime(new Date());
	}

	/**
	 * 更新后操作
	 * 
	 * @author qiuxs
	 * @param oldBean
	 * @param newBean
	 */
	protected void postUpdate(T oldBean, T newBean) {
		List<IUpdateFilter<PK, T>> updateFilters = this.getUpdateFilters();
		for (IUpdateFilter<PK, T> filter : updateFilters) {
			filter.postUpdate(oldBean, newBean);
		}
	}

	private List<IUpdateFilter<PK, T>> getUpdateFilters() {
		if (this.updateFilters == null) {
			this.updateFilters = new ArrayList<>();
			List<IServiceFilter<PK, T>> serviceFilters = getServiceFilters();
			for (IServiceFilter<PK, T> filter : serviceFilters) {
				if (filter instanceof IUpdateFilter) {
					this.updateFilters.add((IUpdateFilter<PK, T>) filter);
				}
			}
		}
		return this.updateFilters;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(T bean) {
		PK id = bean.getId();
		if (id == null) {
			this.create(bean);
		} else {
			this.update(bean);
		}
	}

	/**
	 * 保存前操作
	 * 
	 * @author qiuxs
	 * @param bean
	 * @return
	 */
	protected void preSave(T oldBean, T newBean) {
	}

	/**
	 * 保存后操作
	 * 
	 * @author qiuxs
	 * @param bean
	 */
	protected void postSave(T oldBean, T newBean) {
	}

	protected List<IServiceFilter<PK, T>> getServiceFilters() {
		if (this.serviceFilters == null) {
			this.serviceFilters = new ArrayList<>();
			this.initServiceFilters(serviceFilters);
		}
		return this.serviceFilters;
	}

	protected abstract void initServiceFilters(List<IServiceFilter<PK, T>> serviceFilters);
}