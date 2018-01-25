package com.qiuxs.cuteframework.core.service.filter.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import com.qiuxs.cuteframework.core.basic.utils.TypeAdapter;
import com.qiuxs.cuteframework.core.entity.IObject;
import com.qiuxs.cuteframework.core.service.filter.IInsertFilter;
import com.qiuxs.cuteframework.core.service.util.IDGenerateUtil;

/**
 * 主键生成过滤器
 * @author qiuxs
 *
 * @param <PK>
 * @param <T>
 */
public class IdGenerateFilter<PK extends Serializable, T extends IObject<PK>> implements IInsertFilter<PK, T> {

	private String tableName;

	public IdGenerateFilter(String tableName) {
		this.tableName = tableName;
	}

	@Override
	public void preInsert(T bean) {
		if (bean.getId() == null) {
			// 获取主键类型
			Class<?> type = (Class<?>) ((ParameterizedType) bean.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			bean.setId(getPK(type));
		}
	}

	@SuppressWarnings("unchecked")
	private PK getPK(Class<?> type) {
		Object pk = IDGenerateUtil.getNextId(this.tableName);
		return (PK) TypeAdapter.adapter(pk, type);
	}

	@Override
	public void postInsert(T bean) {
	}

}