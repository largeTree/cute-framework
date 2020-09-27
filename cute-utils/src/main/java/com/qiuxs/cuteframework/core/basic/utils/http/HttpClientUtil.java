package com.qiuxs.cuteframework.core.basic.utils.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.basic.Constants;
import com.qiuxs.cuteframework.core.basic.utils.CollectionUtils;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.JsonUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.basic.utils.io.IOUtils;

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
	 * 上传文件
	 *  
	 * @author qiuxs  
	 * @param url
	 * @param fileName
	 * @param body
	 * @return
	 */
	public static String doUpload(String url, String fileName, byte[] body) {
		return doUpload(url, fileName, body, false);
	}
	
	/**
	 * 上传文件
	 *  
	 * @author qiuxs  
	 * @param url
	 * @param fileName
	 * @param body
	 * @param sslFlag
	 * @return
	 */
	public static String doUpload(String url, String fileName, byte[] body, boolean sslFlag) {
		return doUpload(url, fileName, body, null, sslFlag);
	}
	
	/**
	 * 上传文件
	 *  
	 * @author qiuxs  
	 * @param url
	 * @param fileName
	 * @param body
	 * @param params
	 * @param sslFlag
	 * @return
	 */
	public static String doUpload(String url, String fileName, byte[] body, Map<String, String> params, boolean sslFlag) {
		return doUpload(url, fileName, body, params, null, sslFlag);
	}
	
	/**
	 * 上传文件
	 *  
	 * @author qiuxs  
	 * @param url
	 * @param fileName
	 * @param body
	 * @param parmas
	 * @param header
	 * @param sslFlag
	 * @return
	 */
	public static String doUpload(String url, String fileName, byte[] body, Map<String, String> parmas, Map<String, String> header, boolean sslFlag) {
		return doUpload(url, fileName, body, null, null, parmas, header, sslFlag);
	}
	
	/**
	 * 上传文件
	 *  
	 * @author qiuxs  
	 * @param url
	 * @param fileName
	 * @param body
	 * @param contentType
	 * @param params
	 * @param headers
	 * @param sslFlag
	 * @return
	 */
	public static String doUpload(String url, String fileName, byte[] body, String contentType, String boundary, Map<String, String> params, Map<String, String> headers, boolean sslFlag) {
		HttpEntity entity = doUploadEntity(url, fileName, body, contentType, boundary, params, headers, sslFlag ? httpsClient : httpClient);
		return httpEntityToString(entity);
	}
	
	private static String httpEntityToString(HttpEntity httpEntity) {
		try {
			return EntityUtils.toString(httpEntity);
		} catch (Throwable e) {
			throw ExceptionUtils.unchecked(e);
		}
	}
	
	/**
	 * 上传文件
	 *  
	 * @author qiuxs  
	 * @param url
	 * @param body
	 * @param params
	 * @param header
	 * @param sslFlag
	 * @return
	 */
	private static HttpEntity doUploadEntity(String url, String fileName, byte[] body, String contentType, String boundary, Map<String, String> params, Map<String, String> headers, HttpClient client) {
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
			
			builder.setBoundary(boundary);
			
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			// 文件
			builder.addBinaryBody(fileName, body, ContentType.APPLICATION_OCTET_STREAM, "123.jpg");
			
			// 其他参数
			if (params != null) {
				params.forEach((k,v)->{
					builder.addPart(k, new StringBody(v, ContentType.create("text/plain", Constants.DEFAULT_CHARSET)));
				});
			}
			post.setEntity(builder.build());
			HttpResponse resp = client.execute(post);
			StatusLine statusLine = resp.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				return resp.getEntity();
			} else {
				throw new HttpResponseException(statusCode, statusLine.getReasonPhrase());
			}
		} catch (IOException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}
	
	/**
	 * 下载文件
	 *  
	 * @author qiuxs  
	 * @param url
	 * @return
	 */
	public static byte[] download(String url) {
		return download(url, false);
	}
	
	/**
	 * 下载文件
	 *  
	 * @author qiuxs  
	 * @param url
	 * @param sslFlag
	 * @return
	 */
	public static byte[] download(String url, boolean sslFlag) {
		return download(url, null, sslFlag);
	}
	
	/**
	 * 下载文件
	 *  
	 * @author qiuxs  
	 * @param url
	 * @param params
	 * @param sslFlag
	 * @return
	 */
	public static byte[] download(String url, Map<String, String> params, boolean sslFlag) {
		return download(url, params, null, sslFlag);
	}
	
	/**
	 * 下载文件
	 *  
	 * @author qiuxs  
	 * @param url
	 * @param params
	 * @param headers
	 * @param sslFlag
	 * @return
	 */
	public static byte[] download(String url, Map<String, String> params, Map<String, String> headers, boolean sslFlag) {
		return download(url, params, headers, sslFlag ? httpsClient : httpClient);
	}
	
	/**
	 * 下载文件
	 *  
	 * @author qiuxs  
	 * @param url
	 * @param params
	 * @param headers
	 * @param client
	 * @return
	 */
	private static byte[] download(String url, Map<String, String> params, Map<String, String> headers, HttpClient client) {
		String finalUrl = builderFinalGetUrl(url, params);
		HttpEntity entity = doGetEntity(finalUrl, headers, client);
		return httpEntityToByteArray(entity);
	}
	
	private static byte[] httpEntityToByteArray(HttpEntity entity) {
		try {
			return EntityUtils.toByteArray(entity);
		} catch (Exception e) {
			throw ExceptionUtils.unchecked(e);
		}
	}
	
	/**
	 * java原生请求
	 *  
	 * @author qiuxs  
	 * @param urlStr
	 * @param header
	 * @param params
	 * @return
	 */
	public static String doGetNative(String urlStr, Map<String, String> header, Map<String, String> params) {
		urlStr = builderFinalGetUrl(urlStr, params);
		HttpURLConnection conn = null;
		BufferedReader reader = null;
		StringBuilder resString = new StringBuilder();
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			
			if (CollectionUtils.isNotEmpty(header)) {
				for (Map.Entry<String, String> entry : header.entrySet()) {
					conn.setRequestProperty(entry.getKey(), entry.getKey());
				}
			}
			
			conn.connect();
			
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = reader.readLine()) != null) {
				resString.append(line);
			}
		} catch (Exception e) {
			throw ExceptionUtils.unchecked(e);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
			if (reader != null) {
				IOUtils.closeQuietly(reader);
			}
		}
		return resString.toString();
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
	 * @return
	 */
	public static JSONObject doGetJSONObject(String url, boolean sslFlag) {
		return doGetJSONObject(url, null, sslFlag);
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
	 * 获取字符串
	 *  
	 * @author qiuxs  
	 * @param url
	 * @param params
	 * @return
	 */
	public static String doGetString(String url, boolean sslFlag) {
		return doGetString(url, null, null, false);
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
		try {
			return EntityUtils.toString(doGetEntity(finalUrl, headers, client));
		} catch (Exception e) {
			throw ExceptionUtils.unchecked(e);
		}
	}
	
	/**
	 * 获取请求实体
	 *  
	 * @author qiuxs  
	 * @param finalUrl
	 * @param headers
	 * @param client
	 * @return
	 */
	private static HttpEntity doGetEntity(String finalUrl, Map<String, String> headers, HttpClient client) {
		HttpGet get = new HttpGet(finalUrl);
		try {
			// 拼接请求头
			if (CollectionUtils.isNotEmpty(headers)) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					get.addHeader(entry.getKey(), entry.getValue());
				}
			}
			
			HttpResponse resp = client.execute(get);
			StatusLine statusLine = resp.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				return resp.getEntity();
			} else {
				throw new HttpResponseException(statusCode, statusLine.getReasonPhrase());
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
			String respStr = EntityUtils.toString(resp.getEntity(), Charset.forName(Constants.DEFAULT_CHARSET));
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
