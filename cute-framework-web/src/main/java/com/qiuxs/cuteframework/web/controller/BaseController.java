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
import com.qiuxs.cuteframework.core.persistent.database.dao.page.PageInfo;
import com.qiuxs.cuteframework.web.bean.ResponseResult;

/**
 * 控制器基类
 * 提供统一的异常处理
 * 输出响应相关能力
 * @author qiuxs
 *
 */
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
		return response(this.successResponse());
	}

	protected String responseVal(Object val) {
		ResponseResult response = this.successResponse();
		response.setData(JsonUtils.genJSON("val", val));
		return this.response(response);
	}

	protected String responseRes(List<?> rows) {
		if (rows == null) {
			rows = new ArrayList<>();
		}
		return this.responseRes(rows, rows.size());
	}

	protected String responseRes(List<?> rows, int total) {
		return this.responseRes(rows, total, null);
	}

	protected String responseRes(List<?> rows, int total, Map<String, ? extends Number> sumrow) {
		ResponseResult resp = new ResponseResult(rows, total, sumrow);
		return this.response(resp);
	}

	/**
	 * 输出响应
	 * 
	 * @param res
	 * @return
	 */
	protected String responseRes(Object res) {
		ResponseResult resp = this.successResponse();
		resp.setData(res);
		return this.response(resp);
	}

	/**
	 * 输出JSON响应
	 * @param res
	 * @return
	 */
	protected String responseRes(JSONObject res) {
		ResponseResult resp = this.successResponse();
		resp.setData(res);
		return this.response(resp);
	}

	protected String response(ResponseResult resp) {
		return JsonUtils.toJSONString(resp);
	}

	/**
	 * 构造默认响应JSON对象
	 * @return
	 */
	private ResponseResult successResponse() {
		ResponseResult res = ResponseResult.makeSuccess();
		return res;
	}
}
