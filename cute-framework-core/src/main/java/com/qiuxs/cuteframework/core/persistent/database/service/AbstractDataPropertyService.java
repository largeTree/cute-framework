package com.qiuxs.cuteframework.core.persistent.database.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.qiuxs.cuteframework.core.basic.bean.UserLite;
import com.qiuxs.cuteframework.core.basic.ex.ErrorCodes;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.ListUtils;
import com.qiuxs.cuteframework.core.basic.utils.ReflectUtils;
import com.qiuxs.cuteframework.core.context.UserContext;
import com.qiuxs.cuteframework.core.persistent.database.dao.IBaseDao;
import com.qiuxs.cuteframework.core.persistent.database.dao.page.PageInfo;
import com.qiuxs.cuteframework.core.persistent.database.entity.IEntity;
import com.qiuxs.cuteframework.core.persistent.database.entity.IFlag;
import com.qiuxs.cuteframework.core.persistent.database.modal.BaseField;
import com.qiuxs.cuteframework.core.persistent.database.modal.PropertyWrapper;
import com.qiuxs.cuteframework.core.persistent.database.service.filter.IInsertFilter;
import com.qiuxs.cuteframework.core.persistent.database.service.filter.IServiceFilter;
import com.qiuxs.cuteframework.core.persistent.database.service.filter.IUpdateFilter;
import com.qiuxs.cuteframework.core.persistent.database.service.filter.impl.IdGenerateFilter;
import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDataPropertyService;

/**
 * 
 * 功能描述: <br/>
 * 新增原因: TODO<br/>
 * 新增日期: 2018年4月18日 下午9:16:44 <br/>
 * 
 * @author qiuxs
 * @version 1.0.0
 */
public abstract class AbstractDataPropertyService<PK extends Serializable, T extends IEntity<PK>, D extends IBaseDao<PK, T>>
		extends AbstractPropertyService<PK, T> implements IDataPropertyService<PK, T, D> {
	
	/** 批量操作一次多少条 */
	private static final int BATCH_ONCE = 200;

	private String tableName;

	private List<IServiceFilter<PK, T>> serviceFilters;
	private List<IInsertFilter<PK, T>> insertFilters;
	private List<IUpdateFilter<PK, T>> updateFilters;

	public AbstractDataPropertyService(Class<PK> pkClass, Class<T> pojoClass, String tableName) {
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
		T bean = this.getMust(id);
		// 实现了IFlag接口的默认软删除
		if (bean instanceof IFlag) {
			IFlag flagBean = (IFlag) bean;
			flagBean.setFlag(IFlag.DELETED);
			this.getDao().update(bean);
		} else {
			this.getDao().deleteById(id);
		}
	}
	
	/**
	 * 2019年3月21日 下午10:44:05
	 * qiuxs
	 * @see com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDataPropertyService#enable(java.io.Serializable)
	 */
	@Override
	public void enable(PK pk) {
		T bean = this.getMust(pk);
		if (bean instanceof IFlag) {
			IFlag flagBean = (IFlag) bean;
			flagBean.setFlag(IFlag.VALID);
			this.getDao().update(bean);
		} else {
			
		}
	}
	
	/**
	 * 停用记录实现
	 * 2019年3月21日 下午10:38:54
	 * qiuxs
	 * @see com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDataPropertyService#disable(java.io.Serializable)
	 */
	@Override
	public void disable(PK pk) {
		T bean = this.getMust(pk);
		if (bean instanceof IFlag) {
			IFlag flagBean = (IFlag) bean;
			flagBean.setFlag(IFlag.INVALID);
			this.getDao().update(bean);
		} else {
			
		}
	}
	
	/**
	 * 根据ID获取一行记录
	 * 
	 * @author qiuxs
	 * @param id
	 * @return
	 */
	@Override
	public T get(PK id) {
		T bean = this.getDao().get(id);
		return bean;
	}

	/**
	 * 
	 * 2019年3月21日 下午10:40:58
	 * qiuxs
	 * @see com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDataPropertyService#getMust(java.io.Serializable)
	 */
	@Override
	public T getMust(PK id) {
		T bean = this.get(id);
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
	 * 		参数列表
	 * @param pageInfo
	 * 		分页信息
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<T> findByMap(Map<String, Object> params, PageInfo pageInfo) {
		return this.getDao().list(params, pageInfo);
	}
	
	/**
	 * 使用Map作为参数查询
	 * 
	 * @author qiuxs
	 * @param params
	 * 		参数列表
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<T> findByMap(Map<String, Object> params) {
		return this.getDao().list(params);
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
		this.initCreate(bean);
		this.preSave(null, bean);
		this.getDao().insert(bean);
		postSave(null, bean);
		postCreate(bean);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void createInBatch(List<T> beans) {
		if (ListUtils.isNullOrEmpty(beans)) {
			return;
		}
		// 本次被批量插入的所有记录
		List<T> all = new ArrayList<>(beans.size());
		// 单次插入的记录
		List<T> once = new ArrayList<>(beans.size() > BATCH_ONCE ? BATCH_ONCE : beans.size());
		for (Iterator<T> iter = beans.iterator(); iter.hasNext();) {
			T bean = iter.next();
			// 执行初始化并加入待插入列表
			this.initCreate(bean);
			this.preSave(null, bean);
			once.add(bean);
			all.add(bean);
			// 两百条 插入一次
			if (once.size() == BATCH_ONCE) {
				this.getDao().insertInBatch(once);
				once.clear();
			}
		}
		// 不足两百条多出来的部分
		if (once.size() > 0) {
			this.getDao().insertInBatch(once);
		}
		// 执行后置操作
		for (Iterator<T> iter = all.iterator();iter.hasNext();) {
			T bean = iter.next();
			this.postCreate(bean);
			this.postSave(null, bean);
		}
	}

	protected void preCreate(T bean) {
	}

	protected void postCreate(T bean) {
		List<IInsertFilter<PK, T>> insertFilters = this.getInsertFilters();
		for (IInsertFilter<PK, T> filter : insertFilters) {
			filter.postInsert(bean);
		}
	}

	private void initCreate(T bean) {
		this.preCreate(bean);
		List<IInsertFilter<PK, T>> insertFilters = this.getInsertFilters();
		for (IInsertFilter<PK, T> filter : insertFilters) {
			filter.preInsert(bean);
		}
		UserLite userLite = UserContext.getUserLiteOpt();
		if (bean.getCreatedBy() == null && userLite != null) {
			bean.setCreatedBy(userLite.getUserId());
		}
		if (bean.getCreatedTime() == null) {
			bean.setCreatedTime(new Date());
		}
		
		// 默认为有效的状态
		if (bean instanceof IFlag) {
			IFlag flagBean = (IFlag) bean;
			flagBean.setFlag(IFlag.VALID);
		}
		
		this.initDefault(bean);
	}

	/**
	 * 根据props配置设置默认值
	 * 
	 * @param bean
	 */
	protected void initDefault(T bean) {
		List<PropertyWrapper<?>> props = this.getProperties();
		for (PropertyWrapper<?> prop : props) {
			BaseField field = prop.getField();
			String fileName = field.getName();
			try {
				Object value = ReflectUtils.getFieldValue(bean, fileName);
				if (value == null) {
					ReflectUtils.setFieldValue(bean, fileName, field.getDefaultValue());
				}
			} catch (ReflectiveOperationException e) {
				log.warn(this.getPojoClass().getName() + " has no Field [name=" + fileName + "]");
			}
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

	/**
	 * 设置ID
	 * 
	 * @param bean
	 */
	public void setId(T bean) {
		List<IInsertFilter<PK, T>> filters = this.getInsertFilters();
		for (IInsertFilter<PK, T> filter : filters) {
			if (filter instanceof IdGenerateFilter) {
				filter.preInsert(bean);
			}
		}
	}

	/**
	 * 更新对象
	 * 
	 * @see com.qiuxs.frm.persistent.service.IDataService#update(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void update(T bean) {
		// 默认为Null，需要时自行实现
		T oldBean = this.getOld(bean.getId());
		if (this.initUpdate(oldBean, bean)) {
			preSave(oldBean, bean);
			this.getDao().update(bean);
		}
		postUpdate(oldBean, bean);
		postSave(oldBean, bean);
	}

	/**
	 * 获取旧记录
	 * 默认返回空，需要时自行实现
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
		return true;
	}

	private boolean initUpdate(T oldBean, T newBean) {
		List<IUpdateFilter<PK, T>> updateFilters = this.getUpdateFilters();
		for (IUpdateFilter<PK, T> filter : updateFilters) {
			filter.preUpdate(oldBean, newBean);
		}
		UserLite userLite = UserContext.getUserLiteOpt();
		if (userLite != null) {
			newBean.setUpdatedBy(userLite.getUserId());
		}
		newBean.setUpdatedTime(new Date());
		return this.preUpdate(oldBean, newBean);
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