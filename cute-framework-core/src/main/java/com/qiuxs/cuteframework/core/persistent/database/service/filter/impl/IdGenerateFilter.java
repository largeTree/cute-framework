package com.qiuxs.cuteframework.core.persistent.database.service.filter.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.basic.utils.TypeAdapter;
import com.qiuxs.cuteframework.core.persistent.database.entity.IEntity;
import com.qiuxs.cuteframework.core.persistent.database.service.filter.IInsertFilter;
import com.qiuxs.cuteframework.core.persistent.util.IDGenerateUtil;

/**
 * 主键生成过滤器
 * @author qiuxs
 *
 * @param <PK>
 * @param <T>
 */
public class IdGenerateFilter<PK extends Serializable, T extends IEntity<PK>> implements IInsertFilter<PK, T> {

	private static Logger log = LogManager.getLogger(IdGenerateFilter.class);
	
	private String tableName;

	public IdGenerateFilter(String tableName) {
		this.tableName = tableName;
	}

	@Override
	public void preInsert(T bean) {
		if (bean.getId() == null) {
			// 获取主键类型
			Class<?> type = getPKClass(bean.getClass());
			PK pk = getPK(type);
			if (log.isDebugEnabled()) {
				log.debug(StringUtils.append("Table [", tableName, "] auto generate Pk [", String.valueOf(pk), "]"));
			}
			bean.setId(pk);
		}
	}

	private Class<?> getPKClass(Class<?> entityClass) {
		Type type = entityClass.getGenericSuperclass();
		while (!(type instanceof ParameterizedType)) {
			entityClass = entityClass.getSuperclass();
			type = entityClass.getGenericSuperclass();
		}
		return (Class<?>) ((ParameterizedType)type).getActualTypeArguments()[0];
	}
	
	@SuppressWarnings("unchecked")
	private PK getPK(Class<?> type) {
		 Object pk = IDGenerateUtil.getNextId(tableName);
		return (PK) TypeAdapter.adapter(pk, type);
	}

	@Override
	public void postInsert(T bean) {
	}

}