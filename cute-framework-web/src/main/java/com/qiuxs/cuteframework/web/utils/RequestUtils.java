package com.qiuxs.cuteframework.web.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.qiuxs.cuteframework.core.basic.constants.SymbolConstants;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.web.action.ActionConstants;

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
	 * 拆分请求字符串
	 * @param queryString
	 * @return
	 */
	public static Map<String, String> splitQueryString(String queryString) {
		if (StringUtils.isBlank(queryString)) {
			return Collections.emptyMap();
		}
		String[] k_vs = queryString.split(SymbolConstants.SEPARATOR_AND);
		Map<String, String> kvMap = new HashMap<>();
		for (String kv : k_vs) {
			String[] k_v = kv.split(SymbolConstants.SEPARATOR_EQ);
			kvMap.put(k_v[0], k_v[1]);
		}
		return kvMap;
	}

	/**
	 * 将请求参数转化为Map方便使用
	 *  
	 * @author qiuxs  
	 * @param request
	 * 请求对象
	 * @param compressType 
	 * 	压缩方式
	 * @return
	 */
	public static Map<String, String> getRequestParams(HttpServletRequest request, String compressType) {
		Map<String, String> params = getRequestParams(request);
		String jsonParam = params.get(ActionConstants.PARAM_JSONPARAM);
		if (compressType != null && jsonParam != null) {
			// TODO 解压jsonParam
		}
		return params;
	}

	/**
	 * 将请求参数转化为Map方便使用
	 *  
	 * @author qiuxs  
	 * @param request
	 * 请求对象
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

	/**
	 * 获取客户端IP地址，如果是ngingx转发，从http header中获取真实IP
	 * @param req
	 * @return
	 */
	public static String getRemoteAddr(HttpServletRequest request) throws UnknownHostException {
		String ipAddress = null;
		//ipAddress = request.getRemoteAddr();   
		ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1") || "0:0:0:0:0:0:0:1".equals(ipAddress)) {
				//根据网卡取本机配置的IP   
				InetAddress inet = InetAddress.getLocalHost();
				ipAddress = inet.getHostAddress();
			}
		}
		//对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割   
		if (ipAddress != null && ipAddress.length() > 15) { //"***.***.***.***".length() = 15   
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}
}
