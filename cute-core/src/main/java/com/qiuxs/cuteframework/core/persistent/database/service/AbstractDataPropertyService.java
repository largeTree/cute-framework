package com.qiuxs.cuteframework.core.persistent.database.service;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.qiuxs.cuteframework.core.basic.bean.UserLite;
import com.qiuxs.cuteframework.core.basic.ex.ErrorCodes;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.ListUtils;
import com.qiuxs.cuteframework.core.basic.utils.reflect.FieldUtils;
import com.qiuxs.cuteframework.core.context.UserContext;
import com.qiuxs.cuteframework.core.persistent.database.dao.IBaseDao;
import com.qiuxs.cuteframework.core.persistent.database.dao.page.PageInfo;
import com.qiuxs.cuteframework.core.persistent.database.entity.IEntity;
import com.qiuxs.cuteframework.core.persistent.database.entity.IFlag;
import com.qiuxs.cuteframework.core.persistent.database.entity.IUnitId;
import com.qiuxs.cuteframework.core.persistent.database.lookup.DsTypeRegister;
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
	protected static final int BATCH_ONCE = 200;

	private final String tableName;
	private final String pkField;
	
	private List<IServiceFilter<PK, T>> serviceFilters;
	private List<IInsertFilter<PK, T>> insertFilters;
	private List<IUpdateFilter<PK, T>> updateFilters;

	public AbstractDataPropertyService(Class<PK> pkClass, Class<T> pojoClass, String tableName, String pkField) {
		super(pkClass, pojoClass);
		this.tableName = tableName;
		this.pkField = pkField;
	}

	@PostConstruct
	protected void registerDsType() {
		DsTypeRegister.register(this.getDao(), IBaseDao.class, this.getDsId(), this.getDsType(), this.getPojoClass());
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
	 * 获取主键字段名
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public String getPkField() {
		return this.pkField;
	}

	/**
	 * 删除一个对象
	 * @see com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDataPropertyService#deleteById(java.io.Serializable)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteById(PK id) {
		T bean = this.getMust(id);
		if (bean != null) {
			return this.delete(bean);
		}
		return 0;
	}
	
	/**
	 * 删除一个对象
	 * @see com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDataPropertyService#delete(com.qiuxs.cuteframework.core.persistent.database.entity.IEntity)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int delete(T bean) {
		// 实现了IFlag接口的默认软删除
		if (bean instanceof IFlag) {
			IFlag flagBean = (IFlag) bean;
			flagBean.setFlag(IFlag.DELETED);
			return this.getDao().update(bean);
		} else {
			return this.getDao().deleteById(bean.getId());
		}
	}
	
	@Override
	public int deleteDirect(PK id) {
		return this.getDao().deleteById(id);
	}

	/**
	 * 2019年3月21日 下午10:44:05
	 * qiuxs
	 * @see com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDataPropertyService#enable(java.io.Serializable)
	 */
	@Override
	public void enable(PK pk) {
		T bean = BeanUtils.instantiateClass(this.getPojoClass());
		if (bean instanceof IFlag) {
			bean.setId(pk);
			this.setUpdate(bean);
			IFlag flagBean = (IFlag) bean;
			flagBean.setFlag(IFlag.VALID);
			this.getDao().update(bean);
			bean = this.get(pk);
			if (bean != null) {
				this.postEnable(bean);
			}
		} else {
			ExceptionUtils.throwLogicalException("启用失败");
		}
	}
	
	protected void postEnable(T bean) {
	}

	/**
	 * 停用记录实现
	 * 2019年3月21日 下午10:38:54
	 * qiuxs
	 * @see com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDataPropertyService#disable(java.io.Serializable)
	 */
	@Override
	public void disable(PK pk) {
		T bean = BeanUtils.instantiateClass(this.getPojoClass());
		if (bean instanceof IFlag) {
			bean.setId(pk);
			this.setUpdate(bean);
			IFlag flagBean = (IFlag) bean;
			flagBean.setFlag(IFlag.INVALID);
			this.getDao().update(bean);
			bean = this.get(pk);
			if (bean != null) {
				this.postDisable(bean);
			}
		} else {
			ExceptionUtils.throwLogicalException("停用失败:001");
		}
	}
	
	protected void postDisable(T bean) {
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

	@Transactional(propagation = Propagation.SUPPORTS)
	public List<T> getAll() {
		return this.findByMap(new HashMap<String, Object>());
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
	 * 使用Map作为参数查询，并仅返回一行记录
	 *  
	 * @author qiuxs  
	 * @param params
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public T findByMapSingle(Map<String, Object> params) {
		List<T> list = this.getDao().list(params, PageInfo.makeSinglePageInfo());
		if (list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}
	
	/**
	 * 按条件获取行数
	 *  
	 * @author qiuxs  
	 * @param params
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public Long getCountByMap(Map<String, Object> params) {
		return this.getDao().getCount(params);
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
		this.createInner(bean);
		postSave(null, bean);
		postCreate(bean);
	}
	
	protected void createInner(T bean) {
		this.getDao().insert(bean);
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
		for (Iterator<T> iter = all.iterator(); iter.hasNext();) {
			T bean = iter.next();
			this.postCreate(bean);
			this.postSave(null, bean);
		}
	}


	/**
	 * 后置创建
	 *  
	 * @author qiuxs  
	 * @param bean
	 */
	protected void postCreate(T bean) {
		List<IInsertFilter<PK, T>> insertFilters = this.getInsertFilters();
		for (IInsertFilter<PK, T> filter : insertFilters) {
			filter.postInsert(bean);
		}
	}

	/**
	 * 初始化创建
	 *  
	 * @author qiuxs  
	 * @param bean
	 */
	protected void initCreate(T bean) {
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
		if (bean.getUpdatedBy() == null && userLite != null) {
			bean.setUpdatedBy(userLite.getUserId());
		}
		if (bean.getUpdatedTime() == null) {
			bean.setUpdatedTime(bean.getCreatedTime());
		}

		// 默认为有效的状态
		if (bean instanceof IFlag) {
			IFlag flagBean = (IFlag) bean;
			flagBean.setFlag(IFlag.VALID);
		}
		
		// 设置单元ID
		if (bean instanceof IUnitId) {
			IUnitId unitBean = (IUnitId) bean;
			if (unitBean.getUnitId() == null) {
				Long unitId = UserContext.getUnitIdOpt();
				unitBean.setUnitId(unitId);
			}
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
				Field accessibleField = FieldUtils.getAccessibleField(bean, fileName);
				if (accessibleField != null) {
					Object value = accessibleField.get(bean);
					if (value == null) {
						FieldUtils.setFieldValue(bean, fileName, field.getDefaultValue());
					}
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
		return new ArrayList<IInsertFilter<PK,T>>(this.insertFilters);
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
				break;
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
		this.initUpdate(oldBean, bean);
		preSave(oldBean, bean);

		this.updateInner(bean);

		postUpdateInner(oldBean, bean);
		postSave(oldBean, bean);
	}
	
	@Override
	public void updateDirect(T newBean) {
		this.updateInner(newBean);
	}
	
	protected void updateInner(T bean) {
		this.getDao().update(bean);
	}
	
	/**
	 * 批量更新
	 *  
	 * @author qiuxs  
	 * @param beans
	 */
	public void updateInBatch(Collection<T> beans) {
		if (ListUtils.isEmpty(beans)) {
			return;
		}
		for (T bean : beans) {
			T oldBean = this.getOld(bean.getId());
			this.initUpdate(oldBean, bean);
			this.preSave(oldBean, bean);
		}
		for (T bean : beans) {
			this.updateInner(bean);
		}
		for (T bean : beans) {
			T oldBean = this.getOld(bean.getId());
			postUpdate(oldBean, bean);
			postSave(oldBean, bean);
		}
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
	 * 初始化更新
	 *  
	 * @author qiuxs  
	 * @param oldBean
	 * @param newBean
	 */
	protected void initUpdate(T oldBean, T newBean) {
		List<IUpdateFilter<PK, T>> updateFilters = this.getUpdateFilters();
		for (IUpdateFilter<PK, T> filter : updateFilters) {
			filter.preUpdate(oldBean, newBean);
		}
		this.setUpdate(newBean);
	}
	
	private void setUpdate(T newBean) {
		UserLite userLite = UserContext.getUserLiteOpt();
		if (userLite != null) {
			newBean.setUpdatedBy(userLite.getUserId());
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
	private void postUpdateInner(T oldBean, T newBean) {
		List<IUpdateFilter<PK, T>> updateFilters = this.getUpdateFilters();
		for (IUpdateFilter<PK, T> filter : updateFilters) {
			filter.postUpdate(oldBean, newBean);
		}
		this.postUpdate(oldBean, newBean);
	}
	
	protected void postUpdate(T oldBean, T newBean) {
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