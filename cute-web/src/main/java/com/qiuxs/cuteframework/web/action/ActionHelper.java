package com.qiuxs.cuteframework.web.action;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.basic.bean.Pair;
import com.qiuxs.cuteframework.core.basic.utils.CollectionUtils;
import com.qiuxs.cuteframework.core.basic.utils.MapUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.basic.utils.reflect.MethodUtils;
import com.qiuxs.cuteframework.core.persistent.database.dao.page.PageInfo;
import com.qiuxs.cuteframework.core.persistent.database.dao.page.PageSettings;
import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDataPropertyService;
import com.qiuxs.cuteframework.tech.mybatis.interceptor.hook.MbiPageHook;
import com.qiuxs.cuteframework.web.bean.ActionResult;
import com.qiuxs.cuteframework.web.bean.ReqParam;

public class ActionHelper {

	/**
	 * action查询列表
	 *  
	 * @author qiuxs  
	 * @param service
	 * @param listMethod
	 * @param statisMethod
	 * @param params
	 * @param searchParams
	 * @param page
	 * @param colFieldNames 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ActionResult list(IDataPropertyService<?, ?> service, String listMethod, String statisMethod, ReqParam params, JSONObject searchParams, boolean page, List<Pair<String, String>> colFieldNames) {
		if (listMethod == null) {
			listMethod = "findByMap";
		}
		
		if (searchParams == null) {
			searchParams = new JSONObject();
		}
		
		Integer total;
		List<?> list;
		Map<String, ? extends Number> sumrow = null;
		if (page) {
			// 分页的情况
    		PageInfo pageInfo = PageSettings.preparePageInfo(params);
    		// 传了统计方法的，设置为不自动汇总
    		if (StringUtils.isNotBlank(statisMethod)) {
    			pageInfo.setAutoStatis(false);
    		} else if (CollectionUtils.isNotEmpty(colFieldNames)) {
    			colFieldNames.forEach(item -> {
    				pageInfo.addSumColumn(item);
    			});
    		}
    		list = (List<?>) MethodUtils.invokeMethod(service, listMethod, new Class[] { Map.class, PageInfo.class }, new Object[] { searchParams, pageInfo });
    		sumrow = pageInfo.getSumrow();
    		total = pageInfo.getTotal();
		} else {
			// 不分页的情况
			list = (List<?>) MethodUtils.invokeMethod(service, listMethod, new Class[] { Map.class }, new Object[] { searchParams });
			total = list != null ? list.size() : 0;
		}
		
		// 手动指定的合计方法
		if (StringUtils.isNotBlank(statisMethod)) {
			sumrow = (Map<String, ? extends Number>) MethodUtils.invokeMethodByName(service, statisMethod, new Object[] { searchParams });
			total = MapUtils.getIntValue(sumrow, MbiPageHook.DB_COUNT, 0);
			sumrow.remove(MbiPageHook.DB_COUNT);
		}
		
		// 发送响应
		return responseList(service, list, params.getWrapper(), total, sumrow);
	}

	/**
	 * 构造响应数据
	 *  
	 * @author qiuxs  
	 * @param service
	 * @param list
	 * @param wrapper
	 * @param total
	 * @param sumrow
	 * @return
	 */
	private static ActionResult responseList(IDataPropertyService<?, ?> service, List<?> list, boolean wrapper, int total, Map<String, ? extends Number> sumrow) {
		JSONArray finalList = service.translateBeans(list, wrapper);
		return new ActionResult(finalList, total, sumrow);
	}

}
