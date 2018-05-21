package com.qiuxs.cuteframework.web.utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 功能描述: Http请求转换工具<br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2018年4月23日 下午10:34:21 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class RequestUtils {

	/**
	 * 将请求参数转化为Map方便使用
	 *  
	 * @author qiuxs  
	 * @param request
	 * @return
	 */
	public static Map<String, String> getRequestParams(HttpServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		Map<String, String> params = new HashMap<>(parameterMap.size());
		parameterMap.forEach((key, val) -> {
			params.put(key, val[0]);
		});
		return params;
	}

}
