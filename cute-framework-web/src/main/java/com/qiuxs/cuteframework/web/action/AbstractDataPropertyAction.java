package com.qiuxs.cuteframework.web.action;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.basic.utils.JsonUtils;
import com.qiuxs.cuteframework.core.basic.utils.MapUtils;
import com.qiuxs.cuteframework.core.basic.utils.TypeAdapter;
import com.qiuxs.cuteframework.core.persistent.database.dao.IBaseDao;
import com.qiuxs.cuteframework.core.persistent.database.dao.page.PageInfo;
import com.qiuxs.cuteframework.core.persistent.database.entity.IEntity;
import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDataPropertyService;

/**
 * 基础数据操作Action实现
 * 1.保存
 * 2.根据主键获取单个对象
 * 3.查询列表
 * 4.根据主键硬删除一行记录
 * 5.根据主键停用(软删除)一行记录
 * 6.根据主键启用(撤销软删除)一行记录
 * @author qiuxs
 *
 * @param <PK>
 * @param <T>
 * @param <D>
 * @param <S>
 */
public abstract class AbstractDataPropertyAction<PK extends Serializable, T extends IEntity<PK>, D extends IBaseDao<PK, T>, S extends IDataPropertyService<PK, T, D>> implements IDataPropertyAction<PK, T, D, S> {

	private static final String PK_KEY = "id";

	/**
	 * 
	 * 2019年3月21日 下午10:34:20
	 * qiuxs
	 * @see com.qiuxs.cuteframework.web.action.IDataPropertyAction#save(java.util.Map, com.alibaba.fastjson.JSONObject)
	 */
	@Override
	public T save(Map<String, String> params, JSONObject jsonData) {
		T bean = this.fromJSON(jsonData);
		this.getService().save(bean);
		return bean;
	}

	/**
	 * 转换为实体类对象
	 * 
	 * @param jsonData
	 * @return
	 * 2019年3月21日 下午10:34:35
	 * @auther qiuxs
	 * @return T
	 */
	protected T fromJSON(JSONObject jsonData) {
		T bean = JsonUtils.parseObject(jsonData, this.getService().getPojoClass());
		return bean;
	}

	/**
	 * 
	 * 2019年3月21日 下午10:34:40
	 * qiuxs
	 * @see com.qiuxs.cuteframework.web.action.IDataPropertyAction#get(java.util.Map)
	 */
	@Override
	public T getById(Map<String, String> params) {
		PK pk = this.getPkDirect(params);
		return this.getService().get(pk);
	}

	/**
	 * 2019年3月21日 下午10:34:49
	 * qiuxs
	 * @see com.qiuxs.cuteframework.web.action.IDataPropertyAction#list(com.alibaba.fastjson.JSONObject, com.qiuxs.cuteframework.core.persistent.database.dao.page.PageInfo)
	 */
	@Override
	public List<T> list(JSONObject searchParams, PageInfo pageInfo) {
		return this.getService().findByMap(searchParams, pageInfo);
	}

	/**
	 * 2019年3月21日 下午10:34:56
	 * qiuxs
	 * @see com.qiuxs.cuteframework.web.action.IDataPropertyAction#deleteById(java.util.Map)
	 */
	@Override
	public void deleteById(Map<String, String> params) {
		PK pk = this.getPkDirect(params);
		this.getService().deleteById(pk);
	}

	/**
	 * 2019年3月21日 下午10:26:43
	 * qiuxs
	 * @see com.qiuxs.cuteframework.web.action.IDataPropertyAction#disableById(java.util.Map)
	 */
	@Override
	public void disableById(Map<String, String> params) {
		PK pk = this.getPkDirect(params);
		this.getService().disable(pk);
	}

	/**
	 * 2019年3月21日 下午10:35:03
	 * qiuxs
	 * @see com.qiuxs.cuteframework.web.action.IDataPropertyAction#enableById(java.util.Map)
	 */
	@Override
	public void enableById(Map<String, String> params) {
		PK pk = this.getPkDirect(params);
		this.getService().enable(pk);
	}

	/**
	 * @param params
	 * 2019年3月21日 下午10:35:09
	 * @auther qiuxs
	 * @return PK
	 */
	@SuppressWarnings("unchecked")
	protected PK getPkDirect(Map<String, String> params) {
		String pk = MapUtils.getStringMust(params, PK_KEY);
		return (PK) TypeAdapter.adapter(pk, this.getService().getPkClass());
	}

	/**
	 * 获取服务对象
	 * @return
	 * 2019年3月21日 下午10:35:22
	 * @auther qiuxs
	 * @return S
	 */
	protected abstract S getService();

}
