package com.qiuxs.cuteframework.web.action;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.persistent.database.dao.IBaseDao;
import com.qiuxs.cuteframework.core.persistent.database.dao.page.PageInfo;
import com.qiuxs.cuteframework.core.persistent.database.entity.IEntity;
import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDataPropertyService;

/**
 * 基础数据Action定义
 * @author qiuxs
 * 2019年3月21日 下午10:33:52
 * @param <PK>
 * @param <T>
 * @param <D>
 * @param <S>
 */
public interface IDataPropertyAction<PK extends Serializable, T extends IEntity<PK>, D extends IBaseDao<PK, T>, S extends IDataPropertyService<PK, T, D>> {

	/**
	 * 保存
	 * @param params
	 * @param jsonData
	 * @return
	 * 2019年3月21日 下午10:27:05
	 * @return T
	 */
	public T save(Map<String, String> params, JSONObject jsonData);

	/**
	 * 根据ID获取单个对象
	 * 
	 * @param params
	 * @return
	 * 2019年3月21日 下午10:27:19
	 * @return T
	 */
	public T getById(Map<String, String> params);

	/**
	 * 查询列表
	 * 
	 * @param searchParams
	 * @param pageInfo
	 * @return
	 * 2019年3月21日 下午10:32:31
	 * @auther qiuxs
	 * @return List<T>
	 */
	public List<T> list(JSONObject searchParams, PageInfo pageInfo);

	/**
	 * 根据主键硬删除一行记录
	 * 
	 * @param params
	 * 2019年3月21日 下午10:32:41
	 * @auther qiuxs
	 * @return void
	 */
	public void deleteById(Map<String, String> params);

	/**
	 * 根据主键停用(软删除)一行记录
	 * 
	 * @param params
	 * 2019年3月21日 下午10:33:09
	 * @auther qiuxs
	 * @return void
	 */
	public void disableById(Map<String, String> params);

	/**
	 * 根据主键启用(撤销软删除)一行记录
	 * 
	 * 
	 * @param params
	 * 2019年3月21日 下午10:33:34
	 * @auther qiuxs
	 * @return void
	 */
	public void enableById(Map<String, String> params);

}
