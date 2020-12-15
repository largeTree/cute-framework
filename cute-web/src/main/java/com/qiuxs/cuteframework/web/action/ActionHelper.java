package com.qiuxs.cuteframework.web.action;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.basic.utils.JsonUtils;
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

	@SuppressWarnings("unchecked")
	public static ActionResult list(IDataPropertyService<?, ?> service, String listMethod, String statisMethod, ReqParam params, String jsonData) {
		if (listMethod == null) {
			listMethod = "findByMap";
		}
		JSONObject searchParams = null;
		if (StringUtils.isNotBlank(jsonData)) {
			searchParams = JsonUtils.parseJSONObject(jsonData);
		} else {
			searchParams = new JSONObject();
		}
		PageInfo pageInfo = PageSettings.preparePageInfo(params);
		// 传了统计方法的，设置为不自动汇总
		if (StringUtils.isNotBlank(statisMethod)) {
			pageInfo.setAutoStatis(false);
		}
		List<?> list = (List<?>) MethodUtils.invokeMethodByName(service, listMethod, new Object[] { searchParams, pageInfo });
		
		int total = 0;
		Map<String, ? extends Number> sumrow;
		if (StringUtils.isNotBlank(statisMethod)) {
			sumrow = (Map<String, ? extends Number>) MethodUtils.invokeMethodByName(service, statisMethod, new Object[] { searchParams });
			total = MapUtils.getIntValue(sumrow, MbiPageHook.DB_COUNT, 0);
		} else {
			sumrow = pageInfo.getSumrow();
			total = pageInfo.getTotal();
		}
		
		return responseList(service, list, params.getWrapper(), total, sumrow);
	}

	private static ActionResult responseList(IDataPropertyService<?, ?> service, List<?> list, boolean wrapper, int total, Map<String, ? extends Number> sumrow) {
		JSONArray finalList = service.translateBeans(list, wrapper);
		return new ActionResult(finalList, total, sumrow);
	}

}
