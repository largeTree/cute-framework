package com.qiuxs.cuteframework.web.action;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.basic.utils.JsonUtils;
import com.qiuxs.cuteframework.core.basic.utils.MapUtils;
import com.qiuxs.cuteframework.core.persistent.database.dao.IBaseDao;
import com.qiuxs.cuteframework.core.persistent.database.dao.page.PageInfo;
import com.qiuxs.cuteframework.core.persistent.database.dao.page.PageSettings;
import com.qiuxs.cuteframework.core.persistent.database.entity.IEntity;
import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDataPropertyService;
import com.qiuxs.cuteframework.web.bean.ActionResult;

/**
 * 基础Action提供基础Api
 * @author qiuxs
 * 2019年3月22日 下午8:06:00
 * @param <PK>
 * @param <T>
 * @param <D>
 * @param <S>
 */
public abstract class BaseAction<PK extends Serializable, T extends IEntity<PK>, D extends IBaseDao<PK, T>, S extends IDataPropertyService<PK, T, D>>
        extends AbstractDataPropertyAction<PK, T, D, S> implements IBaseAction {

	/**
	 * 2019年3月22日 下午9:28:46
	 * qiuxs
	 * @see com.qiuxs.cuteframework.web.action.IBaseAction#list(java.util.Map, java.lang.String)
	 */
	@Override
	public ActionResult list(Map<String, String> params, String jsonData) {
		PageInfo pageInfo = PageSettings.preparePageInfo(params);
		List<T> list = super.list(JsonUtils.parseJSONObject(jsonData), pageInfo);
		return this.responseList(MapUtils.getBooleanValue(params, ActionConstants.PARAM_WRAPPER, false), list, pageInfo.getTotal(), pageInfo.getSumrow());
	}

	/**
	 * 响应列表数据
	 * 
	 * 2019年3月22日 下午8:52:39
	 * @auther qiuxs
	 * @param wrapper
	 * @param list
	 * @param total
	 * @param sumrow
	 * @return
	 */
	protected ActionResult responseList(boolean wrapper, List<T> list, int total, Map<String, ? extends Number> sumrow) {
		JSONArray jList = this.getService().translateBeans(list, wrapper);
		return new ActionResult(jList, total, sumrow);
	}

	/**
	 * 2019年3月22日 下午9:29:19
	 * qiuxs
	 * @see com.qiuxs.cuteframework.web.action.IBaseAction#get(java.util.Map)
	 */
	@Override
	public ActionResult get(Map<String, String> params) {
		T bean = super.getById(params);
		return this.responseEntity(MapUtils.getBooleanValue(params, ActionConstants.PARAM_WRAPPER, false), bean);
	}

	/**
	 * 相应单个bean
	 * 
	 * 2019年3月22日 下午9:04:28
	 * @auther qiuxs
	 * @param wrapper
	 * @param bean
	 * @return
	 */
	protected ActionResult responseEntity(boolean wrapper, T bean) {
		JSONObject jBean = this.getService().translateBean(bean, wrapper);
		return new ActionResult(jBean);
	}

	/**
	 * 2019年3月22日 下午9:29:39
	 * qiuxs
	 * @see com.qiuxs.cuteframework.web.action.IBaseAction#save(java.util.Map, java.lang.String)
	 */
	@Override
	public ActionResult save(Map<String, String> params, String jsonData) {
		JSONObject jsonObject = JsonUtils.parseJSONObject(jsonData);
		T bean = super.save(params, jsonObject);
		return new ActionResult(MapUtils.genMap(ActionResult.RES_KEY_VAL, bean.getId()));
	}

	/**
	 * 2019年3月22日 下午9:29:53
	 * qiuxs
	 * @see com.qiuxs.cuteframework.web.action.IBaseAction#delete(java.util.Map)
	 */
	@Override
	public ActionResult delete(Map<String, String> params) {
		super.deleteById(params);
		return ActionResult.SUCCESS_INSTANCE;
	}

	/**
	 * 2019年3月22日 下午9:30:18
	 * qiuxs
	 * @see com.qiuxs.cuteframework.web.action.IBaseAction#disable(java.util.Map)
	 */
	@Override
	public ActionResult disable(Map<String, String> params) {
		super.disableById(params);
		return ActionResult.SUCCESS_INSTANCE;
	}

	/**
	 * 2019年3月22日 下午9:30:41
	 * qiuxs
	 * @see com.qiuxs.cuteframework.web.action.IBaseAction#enable(java.util.Map)
	 */
	@Override
	public ActionResult enable(Map<String, String> params) {
		super.enableById(params);
		return ActionResult.SUCCESS_INSTANCE;
	}
}
