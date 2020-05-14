package com.qiuxs.wxpay;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.KeyStore;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.BeanUtils;

import com.qiuxs.cuteframework.core.basic.Constants;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.basic.utils.TypeAdapter;
import com.qiuxs.cuteframework.core.basic.utils.generator.RandomGenerator;
import com.qiuxs.cuteframework.core.basic.utils.io.IOUtils;
import com.qiuxs.cuteframework.core.basic.utils.reflect.FieldUtils;
import com.qiuxs.cuteframework.core.basic.utils.reflect.ReflectUtils;
import com.qiuxs.cuteframework.core.basic.utils.security.MD5Util;
import com.qiuxs.wxpay.anno.WxPayApi;
import com.qiuxs.wxpay.ex.WxPayClientException;
import com.qiuxs.wxpay.req.BaseRequest;
import com.qiuxs.wxpay.resp.BaseResponse;

/**
 * 微信支付客户端工具
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年5月14日 下午2:10:41 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class WxPayClient {
	
	private static Logger log = LogManager.getLogger(WxPayClient.class);

	/** 接口地址 */
	private final String host;
	/** 证书路径 */
	private final String certPath;
	/** 商户appId */
	private final String mchAppId;
	/** 商户号 */
	private final String mchId;
	/** 密钥 */
	private final String apiKey;
	
	/** 是否已经初始化 */
	private boolean started;
	/** 调用微信接口使用的httpClient */
	private CloseableHttpClient httpClient;

	public WxPayClient(String host, String certPath, String mchAppId, String mchId, String apiKey) {
		super();
		this.checkParam("host", host);
		this.checkParam("certPath", certPath);
		this.checkParam("mchAppId", mchAppId);
		this.checkParam("mchId", mchId);
		this.checkParam("apiKey", apiKey);
		this.host = host;
		this.certPath = certPath;
		this.mchAppId = mchAppId;
		this.mchId = mchId;
		this.apiKey = apiKey;
	}
	
	private void checkParam(String paramName, String val) {
		if (StringUtils.isBlank(val)) {
			throw new IllegalArgumentException(paramName + " not allow empty");
		}
	}

	/**
	 * 执行一个请求
	 *  
	 * @author qiuxs  
	 * @param <RESP>
	 * @param req
	 * @return
	 */
	public <RESP extends BaseResponse> RESP execute(BaseRequest<RESP> req) {
		if (!started) {
			throw new WxPayClientException("WxPayClient not started");
		}
		if (req == null) {
			throw new NullPointerException("req is null");
		}

		@SuppressWarnings("rawtypes")
		Class<? extends BaseRequest> reqClz = req.getClass();

		WxPayApi wxPayApiAnno = reqClz.getAnnotation(WxPayApi.class);
		if (wxPayApiAnno == null) {
			throw new IllegalArgumentException(reqClz.getName() + ", must add an @WxPayApi annotation");
		}

		this.initRequest(req);

		String xml = this.parseToXml(req);

		Document respXml = this.sendRequest(wxPayApiAnno.value(), xml);
		
		RESP res = this.parseToResponse(respXml, ReflectUtils.getSuperClassGenricType(reqClz, 0));

		return res;
	}

	/***
	 * 初始化请求
	 *  
	 * @author qiuxs  
	 * @param req
	 */
	private void initRequest(BaseRequest<?> req) {
		String nonceStr = RandomGenerator.getRandomStringByLength(32);
		req.setNonceStr(nonceStr);
		req.setMchAppid(this.mchAppId);
		req.setMchid(this.mchId);
	}

	/**
	 * 转为xml
	 *  
	 * @author qiuxs  
	 * @param req
	 * @return
	 */
	private String parseToXml(BaseRequest<?> req) {
		List<Field> fields = FieldUtils.getDeclaredFields(req.getClass());
		SortedMap<String, String> params = new TreeMap<String, String>();
		try {
			for (Field field : fields) {
				int fieldMod = field.getModifiers();
				if (Modifier.isStatic(fieldMod)) {
					continue;
				}
				String fieldName = field.getName();
				Object value = FieldUtils.getFieldValue(req, fieldName);
				if (value != null) {
					params.put(StringUtils.humpToUnderline(fieldName), String.valueOf(value));
				}
			}
		} catch (ReflectiveOperationException e) {
			throw new WxPayClientException("parse request failed, ext = " + e.getLocalizedMessage(), e);
		}
		
		String sign = this.createSign(params, this.apiKey);
		params.put("sign", sign);
		Document xml = DocumentHelper.createDocument();
		Element rootE = xml.addElement("xml");
		for (Map.Entry<String, String> entry : params.entrySet()) {
			Element element = rootE.addElement(entry.getKey());
			element.setText(entry.getValue());
		}
		return xml.asXML();
	}

	/**
	 * 发送请求
	 *  
	 * @author qiuxs  
	 * @param xml
	 * @return
	 */
	private Document sendRequest(String api, String xml) {
		String url = this.host + api;
		HttpPost post = new HttpPost(url);
		post.addHeader("Content-Type", "text/xml");
		StringEntity se = new StringEntity(xml, Constants.UTF_8);
		post.setEntity(se);
		CloseableHttpResponse response = null;
		long startTime = System.nanoTime();
		try {
			response = this.httpClient.execute(post);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				SAXReader reader = new SAXReader();
				Document document = reader.read(entity.getContent());
				log.info("call " + api + ", params = " + xml + " finished, costMs = " + ((System.nanoTime() - startTime) / 1000000));
				return document;
			}
		} catch (Exception e) {
			String msg = "call " + api + "failed, ext = " + e.getLocalizedMessage();
			log.error(msg, e);
			throw new WxPayClientException(msg, e);
		} finally {
			IOUtils.closeQuietly(response);
		}
		return null;
	}

	/**
	 * 转换成结果对象
	 *  
	 * @author qiuxs  
	 * @param <RESP>
	 * @param xml
	 * @param respClass
	 * @return
	 */
	private <RESP extends BaseResponse> RESP parseToResponse(Document xml, Class<RESP> respClass) {
		Element rootE = xml.getRootElement();
		RESP resp = BeanUtils.instantiate(respClass);
		for (@SuppressWarnings("unchecked")
		Iterator<Element> iter = rootE.elementIterator(); iter.hasNext();) {
			Element next = iter.next();
			String name = next.getName();
			String fieldName = StringUtils.UnderlineToHump(name);
			Field field = FieldUtils.getAccessibleField(respClass, fieldName);
			if (field == null) {
				if (log.isWarnEnabled()) {
					log.warn("Unkonw failed[" + fieldName + "], in class[" + respClass.getName() + "]");
				}
			} else {
				Class<?> type = field.getType();
				Object val = TypeAdapter.adapter(next.getText(), type);
				try {
					field.set(resp, val);
				} catch (Exception e) {
					log.error("set " + fieldName + " to resp failed, ext = " + e.getLocalizedMessage(), e);
				}
			}
		}
		return resp;
	}

	/**
	 * 初始化证书配置
	 *  
	 * @author qiuxs
	 */
	public void statr() {
		InputStream in = null;
		try {
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			in = new FileInputStream(this.certPath);
			char[] password = this.mchId.toCharArray();
			keyStore.load(in, password);
			SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keyStore, password).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new String[] { "TLSv1" }, null, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			this.httpClient = httpClient;
			this.started = true;
		} catch (Exception e) {
			throw new WxPayClientException(e);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	/**
	 * @Title: createSign
	 * @Description: 签名算法,创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	 * 参照：https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=4_3
	 */
	private String createSign(SortedMap<String, String> params, String appKey) {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + appKey);
		String sign = MD5Util.MD5Encode(sb.toString(), Constants.UTF_8).toUpperCase();
		return sign;
	}

}
