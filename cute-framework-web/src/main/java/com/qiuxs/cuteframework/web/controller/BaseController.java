package com.qiuxs.cuteframework.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.JsonUtils;
import com.qiuxs.cuteframework.core.persistent.dao.page.PageInfo;

public abstract class BaseController {

	protected static Logger log = LogManager.getLogger(BaseController.class);

	@ExceptionHandler
	public String handlerException(Throwable e) {
		JSONObject error = ExceptionUtils.logError(log, e);
		return error.toJSONString();
	}

	/**
	 * 根据参数生成分页信息
	 *  
	 * @author qiuxs  
	 * @param params
	 * @return
	 */
	public PageInfo preparePageInfo(Map<String, String> params) {
		PageInfo pageInfo = new PageInfo();
		
		return pageInfo;
	}

	public String responseSuccess() {
		return successResponse().toJSONString();
	}

	protected String responseVal(Object val) {
		JSONObject response = this.successResponse();
		response.put("data", JsonUtils.genJSON("val", val));
		return response.toJSONString();
	}

	protected String responseRes(List<?> rows) {
		if (rows == null) {
			rows = new ArrayList<>();
		}
		return this.responseRes(rows, (long) rows.size());
	}

	protected String responseRes(List<?> rows, Long count) {
		return this.responseRes(rows, count, null);
	}

	protected String responseRes(List<?> rows, Long count, Map<String, ? extends Number> sumrow) {
		JSONObject res = this.successResponse();
		JSONObject data = new JSONObject();
		data.put("rows", res);
		data.put("count", count);
		data.put("sumrow", sumrow);
		return this.responseRes(res);
	}

	/**
	 * 输出响应
	 * 
	 * @param res
	 * @return
	 */
	protected String responseRes(Object res) {
		JSONObject resp = this.successResponse();
		resp.put("data", JsonUtils.toJSONObject(res));
		return this.responseRes(resp);
	}

	/**
	 * 输出JSON相应
	 * @param res
	 * @return
	 */
	protected String responseRes(JSONObject res) {
		return res.toJSONString();
	}

	/**
	 * 构造默认响应JSON对象
	 * @return
	 */
	protected JSONObject successResponse() {
		JSONObject res = new JSONObject();
		res.put("code", "0");
		res.put("msg", "请求成功");
		return res;
	}
}
