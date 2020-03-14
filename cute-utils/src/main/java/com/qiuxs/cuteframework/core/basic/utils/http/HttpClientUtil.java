package com.qiuxs.cuteframework.core.basic.utils.http;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.basic.Constants;
import com.qiuxs.cuteframework.core.basic.utils.CollectionUtils;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.JsonUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;

/**
 * HttpClient封装工具
 * 
 * @author qiuxs
 *
 */
public class HttpClientUtil {

	/** 普通Http请求客户端 */
	private static HttpClient httpClient = HttpClientBuilder.create().build();
	/** SSLHttp请求客户端 */
	private static HttpClient httpsClient;
	static {
		try {
			SSLContext ctx = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				@Override
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(ctx);
			httpsClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * Http Get请求获取JSONObject结果
	 * 
	 * @param url
	 * @return
	 */
	public static JSONObject doGetJSONObject(String url) {
		return doGetJSONObject(url, null);
	}

	/**
	 * Http Get请求获取JSONObject结果
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static JSONObject doGetJSONObject(String url, Map<String, String> params) {
		return doGetJSONObject(url, params, null);
	}
	
	/**
	 * Http Get请求获取JSONObject结果
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static JSONObject doGetJSONObject(String url, Map<String, String> params, boolean sslFlag) {
		return doGetJSONObject(url, params, null, sslFlag);
	}
	
	/**
	 * Http Get请求获取JSONObject结果
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static JSONObject doGetJSONObject(String url, Map<String, String> params, Map<String, String> headers) {
		return doGetJSONObject(url, params, headers, false);
	}

	/**
	 * Http Get请求获取JSONObject结果
	 * 
	 * @param url
	 * 		地址
	 * @param params
	 * 		参数
	 * @param sslFlag 
	 * 		是否需要使用https
	 * @return
	 */
	public static JSONObject doGetJSONObject(String url, Map<String, String> params, Map<String, String> headers, boolean sslFlag) {
		return JsonUtils.parseJSONObject(doGetString(url, params, headers, sslFlag));
	}

	/**
	 * Http Get请求获取String结果
	 * 
	 * @param url
	 * @return
	 */
	public static String doGetString(String url) {
		return doGetString(url, null);
	}
	
	/**
	 * Http Get请求获取String结果
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String doGetString(String url, Map<String, String> params) {
		return doGetString(url, params, null, false);
	}
	
	/**
	 * Http Get请求获取String结果
	 *  
	 * @author qiuxs  
	 * @param url
	 * @param params
	 * @param headers
	 * @return
	 */
	public static String doGetString(String url, Map<String, String> params, Map<String, String> headers) {
		return doGetString(url, params, headers, false);
	}

	/**
	 * Https Get请求获取String结果
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String doGetString(String url, Map<String, String> params, Map<String, String> headers, boolean sslFlag) {
		return doGetString(builderFinalGetUrl(url, params), headers, sslFlag ? httpsClient : httpClient);
	}

	/**
	 * 执行Get请求
	 * 
	 * @param finalUrl
	 * @param client
	 * @return
	 */
	private static String doGetString(String finalUrl, Map<String, String> headers, HttpClient client) {
		HttpGet get = new HttpGet(finalUrl);
		try {
			// 拼接请求头
			if (CollectionUtils.isNotEmpty(headers)) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					get.addHeader(entry.getKey(), entry.getValue());
				}
			}
			
			HttpResponse resp = client.execute(get);
			int statusCode = resp.getStatusLine().getStatusCode();
			String respStr = EntityUtils.toString(resp.getEntity());
			if (statusCode == HttpStatus.SC_OK) {
				return respStr;
			} else {
				throw new HttpResponseException(statusCode, respStr);
			}
		} catch (IOException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	private static String builderFinalGetUrl(String url, Map<String, String> params) {
		String finalUrl = url;
		if (params != null && params.size() > 0) {
			finalUrl = StringUtils.append(finalUrl, finalUrl.contains("?") ? "&" : "?", buildQueryString(params));
		}
		return finalUrl;
	}

	private static String buildQueryString(Map<String, String> params) {
		StringBuilder queryString = new StringBuilder();
		for (Iterator<Map.Entry<String, String>> iter = params.entrySet().iterator(); iter.hasNext();) {
			Map.Entry<String, String> entry = iter.next();
			queryString.append(entry.getKey()).append("=").append(entry.getValue());
			if (iter.hasNext()) {
				queryString.append("&");
			}
		}
		return queryString.toString();
	}
	
	/**
	 * post表单返回json
	 *  
	 * @author qiuxs  
	 * @param url
	 * @param params
	 * @param sslFlag
	 * @return
	 */
	public static JSONObject doPostRetJson(String url, Map<String, String> params, boolean sslFlag) {
		return doPostRetJson(url, params, null, sslFlag);
	}
	/**
	 * post表单返回josn
	 *  
	 * @author qiuxs  
	 * @param url
	 * @param params
	 * @param headers
	 * @param sslFlag
	 * @return
	 */
	public static JSONObject doPostRetJson(String url, Map<String, String> params, Map<String, String> headers, boolean sslFlag) {
		String retString = doPostRetString(url, params, headers, sslFlag);
		return JsonUtils.parseJSONObject(retString);
	}
	
	/**
	 * post表单返回string
	 *  
	 * @author qiuxs  
	 * @param url
	 * @param params
	 * @param sslFlag
	 * @return
	 */
	public static String doPostRetString(String url, Map<String, String> params, boolean sslFlag) {
		return doPostRetString(url, params, null, sslFlag);
	}
	
	/**
	 * post表单返回string
	 *  
	 * @author qiuxs  
	 * @param url
	 * @param params
	 * @param headers
	 * @param sslFlag
	 * @return
	 */
	public static String doPostRetString(String url, Map<String, String> params, Map<String, String> headers, boolean sslFlag) {
		return doPostRetString(url, params, headers, null, sslFlag ? httpsClient : httpClient);
	}
	
	/**
	 * post表单
	 *  
	 * @author qiuxs  
	 * @param url
	 * @param params
	 * @param headers
	 * @param sslFlag
	 * @return
	 */
	private static String doPostRetString(String url, Map<String, String> params, Map<String, String> headers, String contentType, HttpClient client) {
		try {
			HttpPost post = new HttpPost(url);
			
			// 传了的有的情况下设置一下
			if (StringUtils.isNotBlank(contentType)) {
				post.addHeader("Content-type", contentType);
			}
			// 请求头
			// post.setHeader("Content-type", ContentType.create("application/x-www-form-urlencoded", Constants.DEFAULT_CHARSET).toString());
			if (headers != null) {
				headers.forEach((k, v) -> {
					post.addHeader(k, v);
				});
			}
			// 请求体
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			if (params != null) {
				params.forEach((k,v)->{
					builder.addPart(k, new StringBody(v, ContentType.create("text/plain", Constants.DEFAULT_CHARSET)));
				});
			}
			post.setEntity(builder.build());
			HttpResponse resp = client.execute(post);
			String respStr = EntityUtils.toString(resp.getEntity());
			int statusCode = resp.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				return respStr;
			} else {
				throw new HttpResponseException(statusCode, respStr);
			}
		} catch (IOException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * PostBody
	 * @author qiuxs
	 *
	 * @param url
	 * @param jsonString
	 * @return
	 *
	 * 创建时间：2018年8月7日 下午10:17:40
	 */
	public static JSONObject doPostJSONStringBodyRetJSONObject(String url, String jsonString) {
		return JsonUtils.parseJSONObject(doPostJSONStringBodyRetString(url, jsonString, false));
	}
	
	/**
	 * PostBody
	 * @author qiuxs
	 *
	 * @param url
	 * @param jsonString
	 * @return
	 *
	 * 创建时间：2018年8月7日 下午10:17:40
	 */
	public static JSONObject doPostJSONStringBodyRetJSONObject(String url, String jsonString, boolean sslFlag) {
		return JsonUtils.parseJSONObject(doPostJSONStringBodyRetString(url, jsonString, sslFlag));
	}
	
	/**
	 * PostBody
	 * @author qiuxs
	 *
	 * @param url
	 * @param jsonString
	 * @return
	 *
	 * 创建时间：2018年8月7日 下午10:17:40
	 */
	public static String doPostJSONStringBodyRetString(String url, String jsonString) {
		return doPostJSONStringBodyRetString(url, jsonString, false);
	}
	
	/**
	 * postBody
	 * @author qiuxs
	 *
	 * @param append
	 * @param jsonString
	 *
	 * 创建时间：2018年8月7日 下午10:05:55
	 */
	public static String doPostJSONStringBodyRetString(String url, String jsonString, boolean sslFlag) {
		return postStringBody(url, jsonString, ContentType.APPLICATION_JSON.toString(),
				sslFlag ? httpsClient : httpClient);
	}

	private static String postStringBody(String url, String bodyString, String contentType, HttpClient client) {
		HttpPost post = null;
		try {
			post = new HttpPost(url);
			post.setHeader("Content-type", contentType);
			StringEntity entity = new StringEntity(bodyString, Constants.DEFAULT_CHARSET);
			post.setEntity(entity);
			HttpResponse resp = client.execute(post);
			String respStr = EntityUtils.toString(resp.getEntity());
			int statusCode = resp.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				return respStr;
			} else {
				throw new HttpResponseException(statusCode, respStr);
			}
		} catch (IOException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

}
