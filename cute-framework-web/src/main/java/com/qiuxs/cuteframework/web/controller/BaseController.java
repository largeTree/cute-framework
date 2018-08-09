package com.qiuxs.cuteframework.web.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.JsonUtils;
import com.qiuxs.cuteframework.core.basic.utils.ReflectUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.basic.utils.TypeAdapter;
import com.qiuxs.cuteframework.core.persistent.database.dao.page.PageInfo;
import com.qiuxs.cuteframework.web.WebConstants;
import com.qiuxs.cuteframework.web.bean.ResponseResult;
import com.qiuxs.cuteframework.web.controller.api.ApiConfig;
import com.qiuxs.cuteframework.web.controller.api.Param;
import com.qiuxs.cuteframework.web.controller.handler.HandlerMappinHolder;
import com.qiuxs.cuteframework.web.utils.RequestUtils;

/**
 * 控制器基类
 * 提供统一的异常处理
 * 输出响应相关能力
 * @author qiuxs
 *
 */
public abstract class BaseController {

	protected static Logger log = LogManager.getLogger(BaseController.class);

	@RequestMapping(value = "/*", produces = WebConstants.DEFAULT_REQUEST_PRODUCES)
	public String dispatch(HttpServletRequest request, HttpServletResponse resp) throws Exception {
		String ctxPath = request.getContextPath();
		String uri = request.getRequestURI();
		uri = uri.substring(ctxPath.length(), uri.length());
		String[] mappings = HandlerMappinHolder.getMappings(this.getClass());
		
		String urlPrefix = null;
		for (String mapping : mappings) {
			if (uri.startsWith(mapping)) {
				urlPrefix = mapping;
			}
		}
		
		String apiKey = uri.substring(urlPrefix.length(), uri.length());
		ApiConfig apiConfig = HandlerMappinHolder.getApiConfig(this.getClass(), apiKey);
		
		if (apiConfig == null) {
			return sendNotFound(resp);
		}
		
		Object resObj = this.invokeMethod(apiConfig, request);
		return this.parseResponse(resObj);
	}

	private String sendNotFound(HttpServletResponse resp) {
		resp.setStatus(HttpStatus.NOT_FOUND.value());
		return "404 Not Found";
	}

	/**
	 * 转换结果为标准格式
	 * @author qiuxs
	 *
	 * @param obj
	 * @return
	 *
	 * 创建时间：2018年8月7日 下午9:25:24
	 */
	private String parseResponse(Object obj) {
		if (obj == null) {
			return this.responseSuccess();
		}
		Class<? extends Object> resClz = obj.getClass();
		if (obj instanceof ResponseResult) {
			// 包装好的结果
			return this.response((ResponseResult) obj);
		} else if (obj instanceof List) {
			// 列表数据
			List<?> list = (List<?>) obj;
			return this.responseRes(list, list.size());
		} else if (obj instanceof Map) {
			// Map数据
			Map<?, ?> mapData = (Map<?, ?>) obj;
			return this.responseRes(mapData);
		} else if (resClz.isPrimitive() || ReflectUtils.isPrimitivePackagingClass(resClz)
				|| resClz.isAssignableFrom(String.class)) {
			// 基础数据类型或者字符串
			return this.responseVal(obj);
		} else {
			// 未知类型
			return this.responseRes(obj);
		}
	}

	/**
	 * 执行实际的方法
	 * @author qiuxs
	 *
	 * @param apiConfig
	 * @param request
	 * @return
	 *
	 * 创建时间：2018年8月6日 下午10:23:14
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	private Object invokeMethod(ApiConfig apiConfig, HttpServletRequest request) throws ReflectiveOperationException {
		Parameter[] parameters = apiConfig.getParameters();
		Object[] args = this.parseArgs(request, parameters);
		Object resObj = apiConfig.getMethod().invoke(this, args);
		return resObj;
	}

	/**
	 * 根据方法参数列表转换参数
	 * @author qiuxs
	 *
	 * @param request
	 * @param parameters
	 * @return
	 *
	 * 创建时间：2018年8月6日 下午10:51:22
	 */
	private Object[] parseArgs(HttpServletRequest request, Parameter[] parameters) {
		Map<String, String> mapParams = RequestUtils.getRequestParams(request);
		Object[] args = new Object[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			Parameter param = parameters[i];
			Class<?> type = param.getType();
			Param paramAnno = param.getAnnotation(Param.class);
			if (paramAnno == null) {
				// Map类型直接把所有参数放入
				if (type.isAssignableFrom(Map.class)) {
					args[i] = mapParams;
				} else {
					String paramName = param.getName();
					String paramVal = mapParams.get(paramName);
					args[i] = this.parseOneParam(type, paramVal);
				}
			} else {
				String paramName = paramAnno.value();
				String paramVal = mapParams.get(paramName);
				args[i] = this.parseOneParam(type, paramVal);
			}
		}
		return args;
	}

	private Object parseOneParam(Class<?> type, String val) {
		Object finalVal = null;
		// 不为空判断是否是json参数
		if (StringUtils.isNotBlank(val)) {
			if (val.startsWith("{")) {
				try {
					finalVal = JsonUtils.parseObject(val, type);
				} catch (Exception e) {
					// TODO log this
				}
			} else if (val.startsWith("[")) {
				try {
					List<?> beanList = JsonUtils.parseArray(val, type);
					if (type.isArray()) {
						finalVal = beanList.toArray();
					} else if (type.isAssignableFrom(List.class)) {
						finalVal = beanList;
					} else if (type.isAssignableFrom(String.class)) {
						finalVal = val;
					}
				} catch (Exception e) {
					// TODO log this
				}
			} else if (type.isPrimitive() || ReflectUtils.isPrimitivePackagingClass(type)
					|| type.isAssignableFrom(CharSequence.class)) {
				// 基础类型或基础类型包装类或字符串
				finalVal = TypeAdapter.adapter(val, type);
			}
		}
		return finalVal;
	}

	@PostConstruct
	private void register() {
		HandlerMappinHolder.register(this);
	}

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
	 * 输出MapData
	 * @author qiuxs
	 *
	 * @param mapData
	 * @return
	 *
	 * 创建时间：2018年8月7日 下午9:21:31
	 */
	protected String responseRes(Map<?, ?> mapData) {
		ResponseResult resp = this.successResponse();
		resp.setData(mapData);
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
