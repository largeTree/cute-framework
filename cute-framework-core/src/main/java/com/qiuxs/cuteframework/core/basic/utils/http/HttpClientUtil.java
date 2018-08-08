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
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.basic.Constants;
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
	 * @param params
	 * @return
	 */
	public static JSONObject doGetJSONObject(String url, Map<String, String> params) {
		return JsonUtils.parseJSONObject(doGetString(url, params, false));
	}

	/**
	 * Https Get请求获取JSONObject结果
	 * 
	 * @param url
	 * 		地址
	 * @param params
	 * 		参数
	 * @param sslFlag 
	 * 		是否需要使用https
	 * @return
	 */
	public static JSONObject doGetJSONObject(String url, Map<String, String> params, boolean sslFlag) {
		return JsonUtils.parseJSONObject(doGetString(url, params, sslFlag));
	}

	/**
	 * Https Get请求获取String结果
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String doGetString(String url, Map<String, String> params) {
		return doGetString(url, params, false);
	}

	/**
	 * Https Get请求获取String结果
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String doGetString(String url, Map<String, String> params, boolean sslFlag) {
		return doGetString(builderFinalGetUrl(url, params), sslFlag ? httpsClient : httpClient);
	}

	/**
	 * 执行Get请求
	 * 
	 * @param finalUrl
	 * @param client
	 * @return
	 */
	private static String doGetString(String finalUrl, HttpClient client) {
		HttpGet get = new HttpGet(finalUrl);
		try {
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
