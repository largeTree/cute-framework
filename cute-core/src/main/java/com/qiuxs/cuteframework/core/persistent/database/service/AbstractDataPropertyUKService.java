package com.qiuxs.cuteframework.core.persistent.database.service;

import java.io.Serializable;
import java.util.Map;

import com.qiuxs.cuteframework.core.basic.utils.MapUtils;
import com.qiuxs.cuteframework.core.basic.utils.NumberUtils;
import com.qiuxs.cuteframework.core.persistent.database.dao.IBaseUKDao;
import com.qiuxs.cuteframework.core.persistent.database.entity.IEntity;

/**
 * 带唯一约束功能的服务基类
 * 功能描述: <p>  
 * 新增原因: TODO<p>  
 * 新增日期: 2019年11月26日 上午11:26:15 <p>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public abstract class AbstractDataPropertyUKService<PK extends Serializable, T extends IEntity<PK>, D extends IBaseUKDao<PK, T>>
		extends AbstractDataPropertyService<PK, T, D> {
	
	public AbstractDataPropertyUKService(Class<PK> pkClass, Class<T> pojoClass, String tableName, String pkField) {
		super(pkClass, pojoClass, tableName, pkField);
	}

	/**
	 * 根据唯一索引获取一行数据
	 *  
	 * @author qiuxs  
	 * @param keyVals
	 * @return
	 */
	protected T getByUkInner(Object... keyVals) {
		return this.getByUkInner(MapUtils.genMap(keyVals));
	}

	protected T getByUkInner(Map<String, Object> params) {
		log.info(this.getTableName() + ", uk = " + params);
		return this.getDao().getByUk(params);
	}

	/**
	 * 根据唯一索引删除一行数据
	 *  
	 * @author qiuxs  
	 * @param keyVals
	 * @return
	 */
	protected int deleteByUkInner(Object... keyVals) {
		T bean = this.getByUkInner(keyVals);
		if (bean != null) {
			return this.delete(bean);
		}
		return 0;
	}
	
	/**
	 * 唯一约束行是否存在行
	 *  
	 * @author qiuxs  
	 * @param keyVals
	 * @return
	 */
	protected boolean isExistByUkInner(Object... keyVals) {
		Long count = this.getDao().getCountByUk(MapUtils.genMap(keyVals));
		if (NumberUtils.isEmpty(count)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 根据唯一约束判断是否存在其他行
	 *  
	 * @author qiuxs  
	 * @param pk
	 * 		当前行主键
	 * @param keyVals
	 * 		唯一约束的键和值
	 * @return
	 */
	protected boolean isExistOtherByUkInner(PK pk, Object... keyVals) {
		Map<String, Object> params = MapUtils.genMap(keyVals);
		params.put(super.getPkField() + "Ne", pk);
		Long count = this.getDao().getCountByUk(params);
		if (NumberUtils.isEmpty(count)) {
			return false;
		} else {
			return true;
		}
	}
	
}
