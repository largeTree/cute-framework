package com.qiuxs.cuteframework.core.persistent.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.qiuxs.cuteframework.core.persistent.dao.IBaseDao;
import com.qiuxs.cuteframework.core.persistent.dao.page.PageInfo;
import com.qiuxs.cuteframework.core.persistent.entity.IEntity;
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
	public T getById(PK id) {
		return this.getDao().get(id);
	}

	/**
	 * 根据ID获取多行记录
	 * 
	 * @author qiuxs
	 * @param ids
	 * @return
	 */
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
	}

	protected void initCreate(T bean) {
		if (bean.getCreatedTime() == null) {
			bean.setCreatedTime(new Date());
		}
	}

	/**
	 * 更新对象
	 * 
	 * @see com.qiuxs.frm.persistent.service.IDataService#update(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	public void update(T newBean) {
		// 默认为Null，需要时自行实现
		T oldBean = this.getByIdInner(newBean.getId());
		if (preUpdate(oldBean, newBean)) {
			preSave(oldBean, newBean);
			this.getDao().update(oldBean);
		}
		postUpdate(oldBean, newBean);
		postSave(oldBean, newBean);
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
	}

	@Override
	public void save(T bean) {
		PK id = bean.getId();
		if (id == null) {
			this.create(bean);
		} else {
			this.update(bean);
		}
	}

	/**
	 * 直接掉save时，调用此方法查询旧数据，默认返回Null，需要旧数据时需要自行实现此方法返回对应数据
	 * 
	 * @author qiuxs
	 * @param id
	 * @return
	 */
	protected T getByIdInner(PK id) {
		return null;
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

}