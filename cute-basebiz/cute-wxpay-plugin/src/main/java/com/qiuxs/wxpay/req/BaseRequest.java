package com.qiuxs.wxpay.req;

import java.io.Serializable;

import com.qiuxs.wxpay.resp.BaseResponse;

/**
 * 请求基类
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年5月14日 上午11:41:32 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class BaseRequest<RESP extends BaseResponse> implements Serializable {

	private static final long serialVersionUID = -637742778251649245L;

	/** 商户appId */
	private String mchAppid;
	/** 商户号 */
	private String mchid;
	/** 随机字符串 */
	private String nonceStr;
	/** 前面 */
	private String sign;

	public String getMchAppid() {
		return mchAppid;
	}

	public void setMchAppid(String mchAppid) {
		this.mchAppid = mchAppid;
	}

	public String getMchid() {
		return mchid;
	}

	public void setMchid(String mchid) {
		this.mchid = mchid;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

}
