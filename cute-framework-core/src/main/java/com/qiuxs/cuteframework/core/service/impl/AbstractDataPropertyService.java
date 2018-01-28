package com.qiuxs.cuteframework.core.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DuplicateKeyException;

import com.qiuxs.cuteframework.core.basic.ex.ExceptionCode;
import com.qiuxs.cuteframework.core.basic.ex.LogicalException;
import com.qiuxs.cuteframework.core.dao.IBaseDao;
import com.qiuxs.cuteframework.core.dao.page.PageInfo;
import com.qiuxs.cuteframework.core.entity.IEntity;
import com.qiuxs.cuteframework.core.entity.IObject;
import com.qiuxs.cuteframework.core.service.IDataPropertyService;
import com.qiuxs.cuteframework.core.service.filter.IDeleteFilter;
import com.qiuxs.cuteframework.core.service.filter.IInsertFilter;
import com.qiuxs.cuteframework.core.service.filter.IModifyFilter;
import com.qiuxs.cuteframework.core.service.filter.IServiceFilter;
import com.qiuxs.cuteframework.core.service.filter.IUpdateFilter;

public abstract class AbstractDataPropertyService<PK extends Serializable, T extends IObject<PK>, D extends IBaseDao<PK, T>> extends AbstractPropertyService<PK, T> implements IDataPropertyService<PK, T, D> {

	/** 所有的服务过滤器 */
	private List<IServiceFilter<PK, T>> serviceFilters = new LinkedList<>();
	/** 插入过滤器 */
	private List<IInsertFilter<PK, T>> insertFilters = new LinkedList<>();
	/** 更新过滤器 */
	private List<IUpdateFilter<PK, T>> updateFilters = new LinkedList<>();
	/** 删除过滤器 */
	private List<IDeleteFilter<PK, T>> deleteFilters = new LinkedList<>();
	/** 变更过滤器 */
	private List<IModifyFilter<PK, T>> modifyFilters = new LinkedList<>();

	@Override
	public void insert(T bean) {
		this.preInsertInner(bean);
		this.insertInner(bean);
		this.postInsertInner(bean);
	}

	@Override
	public void insertInBatch(Collection<T> beans) {
		for (T bean : beans) {
			this.preInsertInner(bean);
		}
		this.insertInBatchInner(beans);
		for (T bean : beans) {
			this.postInsertInner(bean);
		}
	}

	protected void insertInBatchInner(Collection<T> beans) {
		this.getDao().insertInBatch(beans);
	}

	protected void insertInner(T bean) {
		this.checkModifyNullBean(bean);
		try {
			this.getDao().insert(bean);
		} catch (DuplicateKeyException duplicateKeyException) {
			throw new LogicalException(ExceptionCode.DuplicateKey, "重复记录已存在==>" + this.getDescription());
		} 
	}

	@Override
	public void update(T bean) {
		this.preUpdateInner(bean);
		this.updateInner(bean);
		this.postUpdateInner(bean);
	}

	protected void updateInner(T bean) {
		this.checkModifyNullBean(bean);
		this.getDao().update(bean);
	}

	@Override
	public void save(T bean) {
		this.checkModifyNullBean(bean);
		if (bean.getId() == null) {
			this.insert(bean);
		} else {
			this.update(bean);
		}
	}

	@Override
	public void delete(T bean) {
		this.checkModifyNullBean(bean);
		this.preDeleteInner(bean);
		this.deleteInner(bean);
		this.postDeleteInner(bean);
	}

	protected void deleteInner(T bean) {
		this.getDao().delete(bean);
	}

	@Override
	public void delete(Map<String, Object> params) {
		this.getDao().deleteByWhere(params);
		this.invokePostModifyFilter(null);
	}

	@Override
	public List<T> findAll() {
		return this.getDao().findAll();
	}

	@Override
	public List<T> list(Map<String, Object> params, PageInfo pageInfo) {
		return this.getDao().list(params, pageInfo);
	}

	/**
	 * 检查是否在更改一个空的Bean
	 * @param bean
	 */
	protected void checkModifyNullBean(T bean) {
		if (bean == null) {
			throw new NullPointerException("can't modify with null bean");
		}
	}

	/**
	 * 执行初始化
	 * @param bean
	 */
	private void preInsertInner(T bean) {
		// 1.外置通用  变更过滤器
		this.invokePreModifyFilter(bean);
		// 2.服务内设置默认值
		this.initDefault(bean);
		// 3.服务内通用 保存前置
		this.preSave(bean);
		// 4.外置 调用插入过滤器
		this.invokePreInsertFilter(bean);
		// 5.服务内前置插入
		this.preInsert(bean);

		if (bean instanceof IEntity) {
			IEntity<PK> iEntity = ((IEntity<PK>) bean);
			if (iEntity.getCreatedDate() == null) {
				iEntity.setCreatedDate(new Date());
			}
		}
	}

	/**
	 * 执行更新初始化
	 * @param bean
	 */
	private void preUpdateInner(T bean) {
		// 变更过滤器
		this.invokePreModifyFilter(bean);
		// 前置保存
		this.preSave(bean);
		// 更新过滤器
		this.invokePreUpdateFilter(bean);
		// 内置前置更新
		this.preUpdate(bean);

		if (bean instanceof IEntity) {
			IEntity<PK> iEntity = (IEntity<PK>) bean;
			if (iEntity.getUpdatedDate() == null) {
				iEntity.setUpdatedDate(new Date());
			}
		}
	}

	/**
	 * 前置删除处理器
	 * @param bean
	 */
	private void preDeleteInner(T bean) {
		// 变更过滤器
		this.invokePreModifyFilter(bean);
		// 服务内前置删除
		this.preDelete(bean);
		// 删除过滤器
		this.invokePreDeleteFilter(bean);
	}

	/**
	 * 执行插入前置过滤器
	 * @param bean
	 */
	protected void invokePreInsertFilter(T bean) {
		List<IInsertFilter<PK, T>> insertFilters = this.getInsertFilters();
		if (!insertFilters.isEmpty()) {
			insertFilters.forEach((filter) -> {
				filter.preInsert(bean);
			});
		}
	}

	/**
	 * 调用前置更新过滤器
	 * @param bean
	 */
	protected void invokePreUpdateFilter(T bean) {
		List<IUpdateFilter<PK, T>> filters = this.getUpdateFilters();
		if (!filters.isEmpty()) {
			filters.forEach((filter) -> {
				filter.preUpdate(bean);
			});
		}
	}

	/**
	 * 执行前置删除过滤器
	 * @param bean
	 */
	protected void invokePreDeleteFilter(T bean) {
		List<IDeleteFilter<PK, T>> delelteFilters = this.getDeleteFilters();
		if (!delelteFilters.isEmpty()) {
			delelteFilters.forEach((filter) -> {
				filter.preDelete(bean);
			});
		}
	}

	/**
	 * 调用前置变更过滤器
	 * @param bean
	 */
	protected void invokePreModifyFilter(T bean) {
		List<IModifyFilter<PK, T>> modifyFilters = this.getModifyFilters();
		if (!modifyFilters.isEmpty()) {
			modifyFilters.forEach((filter) -> {
				filter.preModify(bean);
			});
		}
	}

	/**
	 * 执行插入后置处理器
	 * @param bean
	 */
	private void postInsertInner(T bean) {
		// 变更过滤器
		this.invokePostModifyFilter(bean);
		// 保存后置
		this.postSave(bean);
		// 调用插入过滤器
		this.invokePostInsertFilter(bean);
		// 服务内后置插入
		this.postInsert(bean);
	}

	/**
	 * 修改后置操作
	 * @param bean
	 */
	private void postUpdateInner(T bean) {
		// 外置通用
		this.invokePostModifyFilter(bean);
		// 服务内置通用
		this.postSave(bean);
		// 变更过滤器
		this.invokePostUpdateFilter(bean);
		// 服务内置
		this.postUpdate(bean);
	}

	/**
	 * 后置删除处理
	 * @param bean
	 */
	private void postDeleteInner(T bean) {
		// 外置通用
		this.invokePostModifyFilter(bean);
		// 服务内置后置删除
		this.postDelete(bean);
		// 后置删除过滤器
		this.invokePostDeleteFilter(bean);
	}

	/**
	 * 执行插入后置过滤器
	 * @param bean
	 */
	protected void invokePostInsertFilter(T bean) {
		List<IInsertFilter<PK, T>> insertFilters = this.getInsertFilters();
		if (!insertFilters.isEmpty()) {
			insertFilters.forEach((filter) -> {
				filter.postInsert(bean);
			});
		}
	}

	/**
	 * 调用后置更新过滤器
	 * @param bean
	 */
	protected void invokePostUpdateFilter(T bean) {
		List<IUpdateFilter<PK, T>> filters = this.getUpdateFilters();
		if (!filters.isEmpty()) {
			filters.forEach((filter) -> {
				filter.postUpdate(bean);
			});
		}
	}

	/**
	 * 调用后置删除过滤器
	 * @param bean
	 */
	protected void invokePostDeleteFilter(T bean) {
		List<IDeleteFilter<PK, T>> deleteFilters = this.getDeleteFilters();
		if (!deleteFilters.isEmpty()) {
			deleteFilters.forEach((filter) -> {
				filter.postDelete(bean);
			});
		}
	}

	/**
	 * 调用后置变更过滤器
	 * @param bean
	 */
	protected void invokePostModifyFilter(T bean) {
		List<IModifyFilter<PK, T>> modifyFilters = this.getModifyFilters();
		if (!modifyFilters.isEmpty()) {
			modifyFilters.forEach((filter) -> {
				filter.postModify(bean);
			});
		}
	}

	/**
	 * 获取插入过滤器 过滤器数量为0时 执行初始化
	 * @return
	 */
	private List<IInsertFilter<PK, T>> getInsertFilters() {
		if (this.insertFilters.isEmpty()) {
			this.initInsertFilters(this.insertFilters);
		}
		return this.insertFilters;
	}

	/**
	 * 初始化插入过滤器
	 * @param filters
	 */
	protected void initInsertFilters(List<IInsertFilter<PK, T>> filters) {
		List<IServiceFilter<PK, T>> serviceFilters = this.getServiceFilters();
		if (!serviceFilters.isEmpty()) {
			serviceFilters.forEach((serviceFilter) -> {
				if (serviceFilter instanceof IInsertFilter<?, ?>) {
					filters.add((IInsertFilter<PK, T>) serviceFilter);
				}
			});
		}
	}

	/**
	 * 获取更新过滤器 过滤器数量为0时 执行初始化
	 * @return
	 */
	private List<IUpdateFilter<PK, T>> getUpdateFilters() {
		if (this.updateFilters.isEmpty()) {
			this.initUpdateFilters(this.updateFilters);
		}
		return this.updateFilters;
	}

	/**
	 * 初始化更新过滤器
	 * @param filters
	 */
	protected void initUpdateFilters(List<IUpdateFilter<PK, T>> filters) {
		List<IServiceFilter<PK, T>> serviceFilters = this.getServiceFilters();
		if (!serviceFilters.isEmpty()) {
			serviceFilters.forEach((serviceFilter) -> {
				if (serviceFilter instanceof IUpdateFilter<?, ?>) {
					filters.add((IUpdateFilter<PK, T>) serviceFilter);
				}
			});
		}
	}

	private List<IDeleteFilter<PK, T>> getDeleteFilters() {
		if (this.deleteFilters.isEmpty()) {
			this.initDeleteFilters(this.deleteFilters);
		}
		return this.deleteFilters;
	}

	/**
	 * 初始化删除过滤器
	 * @param filters
	 */
	protected void initDeleteFilters(List<IDeleteFilter<PK, T>> filters) {
		List<IServiceFilter<PK, T>> serviceFilters = this.getServiceFilters();
		if (!serviceFilters.isEmpty()) {
			serviceFilters.forEach((serviceFilter) -> {
				if (serviceFilter instanceof IDeleteFilter<?, ?>) {
					filters.add((IDeleteFilter<PK, T>) serviceFilter);
				}
			});
		}

	}

	/**
	 * 获取变更过滤器
	 * @return
	 */
	private List<IModifyFilter<PK, T>> getModifyFilters() {
		if (this.modifyFilters.isEmpty()) {
			this.initModifyFilters(this.modifyFilters);
		}
		return this.modifyFilters;
	}

	/**
	 * 初始化变更过滤器
	 * @param filters
	 */
	private void initModifyFilters(List<IModifyFilter<PK, T>> filters) {
		List<IServiceFilter<PK, T>> serviceFilters = this.getServiceFilters();
		if (!serviceFilters.isEmpty()) {
			serviceFilters.forEach((serviceFilter) -> {
				if (serviceFilter instanceof IModifyFilter<?, ?>) {
					filters.add((IModifyFilter<PK, T>) serviceFilter);
				}
			});
		}
	}

	/**
	 * 获取所有过滤器
	 * @return
	 */
	private List<IServiceFilter<PK, T>> getServiceFilters() {
		if (this.serviceFilters.isEmpty()) {
			this.initServiceFilters(this.serviceFilters);
		}
		return this.serviceFilters;
	}

	/**
	 * 初始化所有过滤器
	 * @param filters
	 */
	protected void initServiceFilters(List<IServiceFilter<PK, T>> filters) {
	}

	/**
	 * 给子类机会 对bean设置默认值
	 * @param bean
	 */
	protected void initDefault(T bean) {
	}

	/**
	 * 服务内置前置插入操作
	 * @param bean
	 */
	protected void preInsert(T bean) {

	}

	/**
	 * 服务内置更新前处理操作
	 * @param bean
	 */
	protected void preUpdate(T bean) {
	}

	/**
	 * 服务内删除前置
	 * @param bean
	 */
	protected void preDelete(T bean) {
	}

	/**
	 * 服务内置后置插入操作
	 * @param bean
	 */
	protected void postInsert(T bean) {
	}

	/***
	 * 服务内置后置更新操作
	 * @param bean
	 */
	protected void postUpdate(T bean) {
	}

	/**
	 * 服务内删除后置
	 * @param bean
	 */
	protected void postDelete(T bean) {
	}

	/**
	 * 保存前调用  新增修改都会调用
	 * @param bean
	 */
	protected void preSave(T bean) {
	}

	/**
	 * 保存后调用 新增修改都会调用
	 * @param bean
	 */
	private void postSave(T bean) {
	}

	/**
	 * 子类提供获取Dao的能力
	 * @return
	 */
	protected abstract D getDao();

}
