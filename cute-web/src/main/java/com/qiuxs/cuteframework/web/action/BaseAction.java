package com.qiuxs.cuteframework.web.action;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.JsonUtils;
import com.qiuxs.cuteframework.core.basic.utils.MapUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.persistent.database.dao.IBaseDao;
import com.qiuxs.cuteframework.core.persistent.database.entity.IEntity;
import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDataPropertyService;
import com.qiuxs.cuteframework.web.bean.ActionResult;
import com.qiuxs.cuteframework.web.bean.ReqParam;

/**
 * 基础Action提供基础Api
 * @author qiuxs
 * 2019年3月22日 下午8:06:00
 * @param <PK>
 * @param <T>
 * @param <D>
 * @param <S>
 */
public abstract class BaseAction<PK extends Serializable, T extends IEntity<PK>, D extends IBaseDao<PK, T>, S extends IDataPropertyService<PK, T>>
        extends AbstractDataPropertyAction<PK, T, D, S> implements IBaseAction {

	/**
	 * 2019年3月22日 下午9:28:46
	 * qiuxs
	 * @see com.qiuxs.cuteframework.web.action.IBaseAction#list(java.util.Map, java.lang.String)
	 */
	@Override
	public ActionResult list(ReqParam params, String jsonData) {
		return this.list(null, params, jsonData);
		// return this.responseList(MapUtils.getBooleanValue(params, ActionConstants.PARAM_WRAPPER, false), list, pageInfo.getTotal(), pageInfo.getSumrow());
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
	protected ActionResult responseList(boolean wrapper, List<?> list, Integer total, Map<String, ? extends Number> sumrow) {
		JSONArray jList = this.getService().translateBeans(list, wrapper);
		return new ActionResult(jList, total == null ? list.size() : total, sumrow);
	}

	/**
	 * 2019年3月22日 下午9:29:19
	 * qiuxs
	 * @see com.qiuxs.cuteframework.web.action.IBaseAction#get(java.util.Map)
	 */
	@Override
	public ActionResult get(ReqParam params) {
		T bean = this.getById(params);
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
	public ActionResult save(ReqParam params, String jsonData) {
		JSONObject jsonObject = JsonUtils.parseJSONObject(jsonData);
		T bean = this.save(params, jsonObject);
		return new ActionResult(MapUtils.genMap(ActionResult.RES_KEY_VAL, bean.getId()));
	}

	/**
	 * 2019年3月22日 下午9:29:53
	 * qiuxs
	 * @see com.qiuxs.cuteframework.web.action.IBaseAction#delete(java.util.Map)
	 */
	@Override
	public ActionResult delete(ReqParam params) {
		this.deleteById(params);
		return ActionResult.SUCCESS_INSTANCE;
	}

	/**
	 * 2019年3月22日 下午9:30:18
	 * qiuxs
	 * @see com.qiuxs.cuteframework.web.action.IBaseAction#disable(java.util.Map)
	 */
	@Override
	public ActionResult disable(ReqParam params) {
		this.disableById(params);
		return ActionResult.SUCCESS_INSTANCE;
	}

	/**
	 * 2019年3月22日 下午9:30:41
	 * qiuxs
	 * @see com.qiuxs.cuteframework.web.action.IBaseAction#enable(java.util.Map)
	 */
	@Override
	public ActionResult enable(ReqParam params) {
		this.enableById(params);
		return ActionResult.SUCCESS_INSTANCE;
	}

	/**
	 * 指定service的查询方法
	 *  
	 * @author qiuxs  
	 * @param listMethod
	 * @param params
	 * @param jsonData
	 * @return
	 */
	protected ActionResult list(String listMethod, ReqParam params, String jsonData) {
		return this.list(listMethod, params, this.transToSearchParams(jsonData));
	}
	
	/**
	 * 支持自定义统计方法
	 *  
	 * @author qiuxs  
	 * @param listMethod
	 * @param statisMethod
	 * @param params
	 * @param jsonData
	 * @return
	 */
	protected ActionResult list(String listMethod, String statisMethod, ReqParam params, String jsonData) {
		return this.list(listMethod, statisMethod, params, this.transToSearchParams(jsonData));
	}
	
	/**
	 * 查询参数转换
	 *  
	 * @author qiuxs  
	 * @param jsonData
	 * @return
	 */
	protected JSONObject transToSearchParams(String jsonData) {
		if (StringUtils.isNotBlank(jsonData)) {
			return JsonUtils.parseJSONObject(jsonData);
		} else {
			return new JSONObject();
		}
	}
	
	/**
	 * 自动合计列表方法
	 *  
	 * @author qiuxs  
	 * @param listMethod
	 * @param params
	 * @param searchParams
	 * @return
	 */
	protected ActionResult list(String listMethod, ReqParam params, JSONObject searchParams) {
		return this.list(listMethod, null, params, searchParams);
	}

	/**
	 * 手动合计列表方法
	 *  
	 * @author qiuxs  
	 * @param listMethod
	 * @param statisMethod
	 * @param params
	 * @param searchParams
	 * @return
	 */
	protected ActionResult list(String listMethod, String statisMethod, ReqParam params, JSONObject searchParams) {
		return ActionHelper.list(this.getService(), listMethod, statisMethod, params, searchParams);
	}
	
	/**
	 * 检查参数是否存在
	 *  
	 * @author qiuxs  
	 * @param params
	 * @param names
	 */
	public void checkParams(Map<String, ?> params, String... names) {
		if (params == null) {
			ExceptionUtils.throwLogicalException("params_is_null");
		}
		if (names == null || names.length == 0) {
			return;
		}
		for (String name : names) {
			if (!params.containsKey(name)) {
				ExceptionUtils.throwLogicalException("param_required", name);
			}
		}
	}
	
	
}
