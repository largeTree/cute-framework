package com.hzecool.fdn.utils.net;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hzecool.fdn.FdnCommonLogger;
import com.hzecool.fdn.exception.utils.ExceptionUtil;
import com.hzecool.fdn.i18n.I18nUtils;
import com.hzecool.fdn.threadlocal.UserTLVarHolder;
import com.hzecool.fdn.utils.ListUtils;
import com.hzecool.fdn.utils.converter.JsonUtils;
import com.hzecool.fdn.utils.date.DateFormatUtils;

/**
 * 
 * 功能描述: 请求工具类 
 * <p>新增原因: TODO
 *  
 * @author fengdg   
 * @version 1.0.0
 * @since 2017年3月18日 上午11:29:50
 */
public class RequestUtils{
	@SuppressWarnings("unused")
    private static final Logger log = Logger.getLogger(RequestUtils.class);
	public static final String PARAM_JSON_PARAM = "jsonParam";
	public static final String PARAM_LANGUAGE = "language";
	public static final String PARAM_MAC_CODE = "macCode";
    /**客户端IP地址在参数中的key值*/
    public static final String PARAM_KEY_CLI_IP = "cliIp__";
    /**服务器的上下文 url在参数中的key值. 如http://127.0.0.1:8080/slh*/
    public static final String PARAM_KEY_SRV_CTX_URL = "srvCtxUrl__";
    /**服务器的主机名或IP. 如127.0.0.1*/
    public static final String PARAM_KEY_SRV_HOST = "srvHost__";
    /**服务器的端口. 如8080*/
    public static final String PARAM_KEY_SRV_PORT = "srvPort__";
    /**请求标识：兼容一代*/
    public static final String PARAM_KEY_REQ_SN = "reqsn";
    public static final String PARAM_KEY_SRV_SCHEME = "srvScheme__";

    /**请求的网关路径*/
	public static final String PATH_API_GATEWAY_PREFIX = "/api.do?apiKey=";
	/**
	 * 获取长整形值，若没有按默认值返回
	 * @param request
	 * @param name
	 * @param defaultValue
	 * @return
	 * @author zhangyz created on 2014-4-26
	 */
	public static Long getLongValue(ServletRequest request,String name, Long... defaultValue){
		String value = request.getParameter(name);
        return com.hzecool.fdn.utils.StringUtils.getLong(value, defaultValue);
	}
	
	/**
	 *  获取长整形值，若没有按默认值返回
	 * @param reqParam
	 * @param name
	 * @param defaultValue 默认值
	 * @return
	 */
	public static Long getLongValue(Map<String, String> reqParam, String name, Long... defaultValue){
		String value = reqParam.get(name);
        return com.hzecool.fdn.utils.StringUtils.getLong(value, defaultValue);
	}
	
	/**
	 * 获取整形值，若没有则抛出异常
	 * @param request
	 * @param name
	 * @return
	 * @author zhangyz created on 2014-4-26
	 */
	public static Long getLongValueMust(ServletRequest request, String name){
        String value = request.getParameter(name);        
        return com.hzecool.fdn.utils.StringUtils.getLongMust(value, name);
    }

	/**
	 * 获取整形值，若没有则抛出异常
	 * @param request
	 * @param name
	 * @return
	 * @author zhangyz created on 2014-4-26
	 */
	public static Long getLongValueMust(Map<String, ?> reqParam, String name){
        Object value = reqParam.get(name);     
        if (value == null)
        	return null;
        return com.hzecool.fdn.utils.StringUtils.getLongMust(value.toString(), name);
    }
	
	/**
	 * 获取整形值，若没有按默认值返回
	 * @param request
	 * @param name
	 * @param defaultValue
	 * @return
	 * @author zhangyz created on 2014-4-26
	 */
	public static Integer getIntValue(ServletRequest request,String name,Integer... defaultValue){
		String value = request.getParameter(name);
        return com.hzecool.fdn.utils.StringUtils.getInteger(value, defaultValue);
	}
	
	public static Integer getIntValue(Map<String, ?> request,String name, Integer... defaultValue){
		Object value = request.get(name);
		if (value == null)
			return null;
		String strValue;
		if (value instanceof String)
			strValue = (String)value;
		else
			strValue = value.toString();
        return com.hzecool.fdn.utils.StringUtils.getInteger(strValue, defaultValue);
	}

	/**
	 * 返回整形值，若没有则抛出异常
	 * @param request
	 * @param name
	 * @return
	 * @author zhangyz created on 2014-4-26
	 */
	public static Integer getIntValueMust(ServletRequest request,String name){
        String value = request.getParameter(name);        
        return com.hzecool.fdn.utils.StringUtils.getIntegerMust(value, name);
    }
	
	public static Integer getIntValueMust(Map<String, String> request,String name){
        String value = request.get(name);        
        return com.hzecool.fdn.utils.StringUtils.getIntegerMust(value, name);
    }
	
	public static Boolean getBooleanValue(Map<String, ?> reqParam,
	        String name, Boolean... defaultValue){
        Object value = reqParam.get(name);
        String strValue = null;
        if (value != null)
        	strValue = value.toString();
        return com.hzecool.fdn.utils.StringUtils.getBoolean(strValue, defaultValue);
    }
    
    public static Boolean getBooleanValueMust(Map<String, ?> reqParam, String name) {
        Object value = reqParam.get(name);
        String strValue = null;
        if (value != null)
        	strValue = value.toString();
        return com.hzecool.fdn.utils.StringUtils.getBooleanMust(strValue, name);
    }
	
	public static Boolean getBooleanValue(ServletRequest request,
	        String name, Boolean... defaultValue){
        String value = request.getParameter(name);
        return com.hzecool.fdn.utils.StringUtils.getBoolean(value, defaultValue);
    }
    
    public static Boolean getBooleanValueMust(ServletRequest request, String name){
        String value = request.getParameter(name);        
        return com.hzecool.fdn.utils.StringUtils.getBooleanMust(value, name);
    }
    
    public static Date getDateTimeValue(ServletRequest request,String name) {
        return getDateTimeValueInner(request, name, DateFormatUtils.DATE_TIME_PATTERN);
    }
    
    public static Date getDateValue(ServletRequest request,String name) {
        return getDateTimeValueInner(request, name, DateFormatUtils.DATE_PATTERN);
    }
    
    public static Date getDateTimeValue(Map<String, String> request,String name) {
        return getDateTimeValueInner(request, name, DateFormatUtils.DATE_TIME_PATTERN);
    }
    
    public static Date getDateValue(Map<String, String> request,String name) {
        return getDateTimeValueInner(request, name, DateFormatUtils.DATE_PATTERN);
    }
    
    private static Date getDateTimeValueInner(ServletRequest request,String name, String patter) {
        String strTime = RequestUtils.getStringValue(request, name);
        if (strTime != null)
            return DateFormatUtils.parse(strTime, patter);
        else
            return null;
    }
    
    private static Date getDateTimeValueInner(Map<String, String> request, String name, String patter) {
        String strTime = RequestUtils.getStringValue(request, name);
        if (strTime != null)
            return DateFormatUtils.parse(strTime, patter);
        else
            return null;
    }
    
	/**
	 * 获取字符值，若没有按默认值返回
	 * @param request
	 * @param name
	 * @param defaultValue
	 * @return
	 * @author zhangyz created on 2014-4-26
	 */
	public static String getStringValue(ServletRequest request,String name,String... defaultValue){
		String value = request.getParameter(name);		
        return com.hzecool.fdn.utils.StringUtils.getString(value, defaultValue);
	}
	
	public static String getStringValue(Map<String, String> request,String name,String... defaultValue){
		String value = request.get(name);		
        return com.hzecool.fdn.utils.StringUtils.getString(value, defaultValue);
	}
	
	/**
	 * 获取字符值，若没有则返回异常json串
	 * @param request
	 * @param name
	 * @return
	 * @author zhangyz created on 2014-4-26
	 */
	public static String getStringValueMust(ServletRequest request, String name){
		String value = request.getParameter(name);
        return com.hzecool.fdn.utils.StringUtils.getStringMust(value, name);
	}
	
	public static String getStringValueMust(Map<String, String> request, String name){
		String value = request.get(name);
        return com.hzecool.fdn.utils.StringUtils.getStringMust(value, name);
	}
	
    /**
     * 获取双浮点型值，若没有按默认值返回
     * @param request
     * @param name
     * @param defaultValue
     * @return
     * @Author:zhanggd created on 2014-7-26
     */
    public static Double getDoubleValue(ServletRequest request,String name, Double... defaultValue){
        String value = request.getParameter(name);
        return com.hzecool.fdn.utils.StringUtils.getDouble(value, defaultValue);
    }
    
    /**
     * 获取双浮点型值，若没有则抛出异常
     * @param request
     * @param name
     * @return
     * @author zhangyz created on 2014-4-26
     */
    public static Double getDoubleValueMust(ServletRequest request, String name){
        String value = request.getParameter(name);        
        return com.hzecool.fdn.utils.StringUtils.getDoubleMust(value, name);
    }
    
    /**
     * 获取BigDecimal型值，若没有返回默认值
     *  
     * @author maozj  
     * @param request
     * @param name
     * @return
     */
    public static BigDecimal getBigDecimalValue(ServletRequest request,String name, BigDecimal... defaultValue){
        String value = request.getParameter(name);
        return com.hzecool.fdn.utils.StringUtils.getBigDecimal(value, defaultValue);
    }
    
    /**
     * 获取BigDecimal型值，若没有则抛出异常
     *  
     * @author maozj  
     * @param request
     * @param name
     * @return
     */
    public static BigDecimal getBigDecimalValueMust(ServletRequest request, String name) {
    	String value = request.getParameter(name);
        return com.hzecool.fdn.utils.StringUtils.getBigDecimalMust(value, name);
	}
    
    /**
     * 返回列表型整形参数，原始参数是逗号分隔的
     * @param request
     * @param name 参数名
     * @return
     */
    public static List<Integer> getListIntValue(ServletRequest request, String name) {
    	String strParams = RequestUtils.getStringValue(request, name);
        if (strParams != null && strParams.length() > 0)
        	return ListUtils.stringToIntegerList(strParams);
        else
        	return null;
    }

    /**
     * 返回列表型长整形参数，原始参数是逗号分隔的
     * @param request
     * @param name 参数名
     * @return
     */
    public static List<Long> getListLongValue(ServletRequest request, String name) {
    	String strParams = RequestUtils.getStringValue(request, name);
        if (strParams != null && strParams.length() > 0)
        	return ListUtils.stringToLongList(strParams);
        else
        	return null;
    }

    /**
     * 返回列表型字符参数，原始参数是逗号分隔的
     * @param request
     * @param name 参数名
     * @return
     */
    public static List<String> getListStringValue(ServletRequest request, String name) {
    	String strParams = RequestUtils.getStringValue(request, name);
        if (strParams != null && strParams.length() > 0)
        	return ListUtils.stringToList(strParams);
        else
        	return null;
    }
    
	public static String[] getRequestParam(ServletRequest request, String name){
		String value = request.getParameter(name);
		String ids[] = null;
		if(StringUtils.isNotBlank(value)){
			return StringUtils.splitByWholeSeparator(value, ",");
		}
		return ids;
	}
    
    /**
	 * 获得Request里的所有参数，并解析json字符串，以Map的形式返回.
	 * jsonParam格式错误则抛出异常
	 * @param request
	 * @return
	 */
	public static Map<String, String> getRequestParamsMap(ServletRequest request) {
		return getRequestParamsMap(request, true);
	}
	
    /**
	 * 获得Request里的所有参数，并解析json字符串，以Map的形式返回.
	 * jsonParam格式错误仅日志记录
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> getRequestParamsMapOpt(ServletRequest request) {
		return getRequestParamsMap(request, false);
	}

	/**
	 * 将客户端上传jsonParam中第一层简单类型参数（非对象）按字符串格式填充到普通参数中，方便后续使用。
	 * @author lsh  
	 * @param params 客户端上传的普通Map型参数
	 * @param jsonParam 客户端上传的jsonParam参数字符串
	 */
	public static void fillJsonParam(Map<String, String> params, String jsonParam) {
		if(!(jsonParam.startsWith("{") || jsonParam.startsWith("["))) { //不是以{或[开头时，可能开头有空白字符，先去掉
			jsonParam = jsonParam.trim();
		}
		
		//检查jsonParam是否为JSONObject格式，不处理数组格式
		if(jsonParam.startsWith("{")) {
			JSONObject jo = JsonUtils.toJSONObject(jsonParam);
			Set<String> keys = jo.keySet();
			for (String key : keys) {
				Object val = jo.get(key);
				
				//仅设置简单类型到普通参数中
				if(val != null && !(val instanceof JSON)) {
					params.put(key, val.toString());
				}
			}
		}
	}
	
	public static void fillClientInfo(HttpServletRequest req, Map<String, String> reqParam) {
		//取客户端IP地址
		String cliIp;
		try {
			cliIp = RequestUtils.getRemoteAddr(req);
		} catch (Throwable t) { //发生异常时，IP地址设置为空，不能影响正常业务流程
			cliIp = "";
			FdnCommonLogger.log.error("取客户端IP失败", t);
		}
		reqParam.put(RequestUtils.PARAM_KEY_CLI_IP, cliIp);
	}

	public static void fillServerInfo(HttpServletRequest req, Map<String, String> reqParam) {
		String requestUrl = req.getRequestURL().toString();
		String ctxPath = req.getContextPath();
		String hostPort = StringUtils.substringBefore(requestUrl, ctxPath + "/");
		requestUrl = hostPort + ctxPath;
		//如果域名中带slhcdn，使用默认端口(http:80 https:443),但是requestUrl取到会带nginx的端口，要把它去掉
		if(StringUtils.contains(requestUrl, "slhcdn")){
			//去掉端口
			requestUrl = requestUrl.replaceAll(":\\d{4}", "");
		}
		//请求根url
		reqParam.put(RequestUtils.PARAM_KEY_SRV_CTX_URL, requestUrl);
		reqParam.put(RequestUtils.PARAM_KEY_SRV_SCHEME, req.getScheme());

		//解析请求url，读取请求的ip和端口
		hostPort = StringUtils.substringAfter(hostPort, "://");
		String[] arrElementRequestUrl = StringUtils.split(hostPort, ':');
		if(arrElementRequestUrl.length > 0){
			reqParam.put(RequestUtils.PARAM_KEY_SRV_HOST, arrElementRequestUrl[0]);
		}
		if(arrElementRequestUrl.length > 1){
			reqParam.put(RequestUtils.PARAM_KEY_SRV_PORT, arrElementRequestUrl[1]);
		}
	}

	public static Map<String, String> getRequestParamsMap(ServletRequest request, boolean jsonParamMust) {
		//直接设置语言线程变量
		UserTLVarHolder.setLangDirect(I18nUtils.getUniformLanguage(request.getParameter(PARAM_LANGUAGE)));
		Map<String, String> params = new HashMap<String, String>();
		String jsonStr = request.getParameter("jsonParam");
		if (StringUtils.isNotBlank(jsonStr)) {
			try {
				//改成仅设置第一层简单类型到普通参数中。lsh update 2017/12/23
				fillJsonParam(params, jsonStr);
//				if (jsonStr.startsWith("[")) {
//					JsonUtils.toJSONArray(jsonStr);
//				} else {
//					Map<String, String> map = JsonUtils.jsonString2StringMap(jsonStr);
//					params.putAll(map);
//				}
			} catch (Throwable ex) {
				String msg = jsonStr + " 客户端传递参数格式错误:" + ex.getMessage();
				if (jsonParamMust) {
					ExceptionUtil.throwParamException(msg);
				} else {
					FdnCommonLogger.log.warn(msg);
				}
			}
		}
		Set<String> keysSet = request.getParameterMap().keySet();
		for (String key : keysSet) {
			params.put(key, request.getParameter(key));
		}
		params.put(RequestUtils.PARAM_KEY_REQ_SN, String.valueOf(request.getAttribute(RequestUtils.PARAM_KEY_REQ_SN)));
		return params;
	}
	
	/**
	 * 获得Request里的所有参数，并解析json字符串，以Map的形式返回,参数urldecode解码("utf-8")
	 *  
	 * @author maozj  
	 * @param request
	 * @return
	 */
	public static Map<String, String> getRequestParamsMapWithDecode(ServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		String jsonStr = request.getParameter("jsonParam");
		if (StringUtils.isNotBlank(jsonStr)) {
			try {
				@SuppressWarnings("deprecation")
				Map<String, String> map = JsonUtils.json2Map(jsonStr);
				params.putAll(map);
			} catch (Throwable ex) {
				throw new RuntimeException(jsonStr + " 客户端传递参数格式错误:" + ex.getMessage());
			}
		}

		Set<String> keysSet = request.getParameterMap().keySet();
		for (String key : keysSet) {
			try {
				String value = URLDecoder.decode(request.getParameter(key), "utf-8");
				params.put(key, value);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		
		return params;
	}
	
	/**
	 * 获取Request中的通用参数
	 *  
	 * @author fengdg  
	 * @param request
	 * @return
	 */
	public static Map<String, String> getRequestCommonParamsMap(ServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		Set<String> keysSet = request.getParameterMap().keySet();
		for (String key : keysSet) {
			params.put(key, request.getParameter(key));
		}
		return params;
	}
	
	/**
	 * 获取Request中的业务参数
	 *  
	 * @author fengdg  
	 * @param request
	 * @return
	 */
	public static Map<String, String> getRequestBizParamsMap(ServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		String jsonStr = request.getParameter("jsonParam");
		if (StringUtils.isNotBlank(jsonStr)) {
			try {
				Map<String, String> map = JsonUtils.jsonString2StringMap(jsonStr);
				params.putAll(map);
			} catch (Throwable ex) {
				throw new RuntimeException(jsonStr + " 客户端传递参数格式错误:" + ex.getMessage());
			}
		}
		return params;
	}

	/**
	 * 获取本应用url
	 * 
	 * @author maozj
	 * @param request
	 * @return 路径 ex: http://bdev.hzdlsoft.com/slb
	 */
	public static String getRequestUrl(HttpServletRequest request) {
		String localUrl =  StringUtils.substringBefore(request.getRequestURL().toString(), 
				request.getServletPath());
		localUrl = localUrl.replaceAll(":(80|443)/", "/");
		return localUrl;
	}

	/**
	 * 获取本次请求的完整url
	 *  
	 * @author maozj  
	 * @param request
	 * @return url ex: http://bdev.hzdlsoft.com/slb/test.do?id=0001
	 */
	public static String getRequestUrlAll(HttpServletRequest request) {
		String requestUrl =getRequestUrl(request);
		String queryString = request.getQueryString();
		String url = requestUrl + "?" + queryString;
		return url;
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
	     if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {   
	      ipAddress = request.getHeader("Proxy-Client-IP");   
	     }   
	     if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {   
	         ipAddress = request.getHeader("WL-Proxy-Client-IP");   
	     }   
	     if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {   
	      ipAddress = request.getRemoteAddr();   
	      if(ipAddress.equals("127.0.0.1")){   
	       //根据网卡取本机配置的IP   
	       InetAddress inet = InetAddress.getLocalHost();   
		   ipAddress= inet.getHostAddress();   
	      }   
	    }   
	     //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割   
	     if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15   
	         if(ipAddress.indexOf(",")>0){   
	             ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));   
	         }   
	     }   
	     return ipAddress;    
	  }

	/**
	 * 将url中的参数转为map
	 *
	 * 例如:
	 * 将http://139.196.124.16:8003/slh/readFile.do?fileid=34056285&epid=13113
	 * 转为Map(fileid,34056285)(epid,13113)
	 *
 	 * @param url
	 * @return
	 */
	public static Map<String, String> urlParam2Map(String url) {
		Map<String, String> map = null;
		if (url != null && url.indexOf("&") > -1 && url.indexOf("=") > -1) {
			map = new HashMap<>();
			String[] arrTemp = url.split("&");
			for (String str : arrTemp) {
				String[] paramValue = str.split("=");
				map.put(paramValue[0], paramValue[1]);
			}
		}
		return map;
	}
}
