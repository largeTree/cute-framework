package com.qiuxs.cuteframework.core.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.qiuxs.cuteframework.core.dao.IBaseDao;
import com.qiuxs.cuteframework.core.dao.page.PageInfo;
import com.qiuxs.cuteframework.core.entity.IObject;
import com.qiuxs.cuteframework.core.service.IDataSourceService;

public abstract class AbstractDataSourceService<PK extends Serializable, T extends IObject<PK>, D extends IBaseDao<PK, T>> extends AbstractPropertyService<PK, T> implements IDataSourceService<PK, T, D> {

	@Override
	public void insert(T bean) {
		this.checkModifyNullBean(bean, "insert");
		this.initInsert(bean);
		this.getDao().insert(bean);
		this.postInsert(bean);
	}

	@Override
	public void update(T bean) {
		this.checkModifyNullBean(bean, "update");
		this.getDao().update(bean);
	}

	@Override
	public void save(T bean) {
		this.checkModifyNullBean(bean, "save");
		if (bean.getId() == null) {
			this.insert(bean);
		} else {
			this.update(bean);
		}
	}

	@Override
	public void delete(T bean) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<T> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> list(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> list(Map<String, Object> params, boolean wrapper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> list(Map<String, Object> params, PageInfo pageInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> list(Map<String, Object> params, boolean wrapper, PageInfo pageInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	private void checkModifyNullBean(T bean, String opt) {
		if (bean == null) {
			StringBuilder msg = new StringBuilder();
			msg.append("can't modify with null bean opt = ").append(opt);
			throw new NullPointerException(msg.toString());
		}
	}

	private void initInsert(T bean) {
		this.initDefault(bean);
	}

	private void postInsert(T bean) {
	}

	/**
	 * 获取主键冲突时的异常提示
	 * @return
	 */
	protected String getDuplicatedMsgInner() {
		return "相同记录已存在";
	}

	protected abstract void initDefault(T bean);

	protected abstract D getDao();

}
