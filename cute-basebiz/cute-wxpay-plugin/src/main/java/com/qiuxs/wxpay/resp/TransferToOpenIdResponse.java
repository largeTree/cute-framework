package com.qiuxs.wxpay.resp;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * 企业付款到个人  根据openId付款到零钱响应数据
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年5月14日 上午11:47:50 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class TransferToOpenIdResponse extends BaseResponse {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -835217973314863304L;

	/**  申请商户号的appid或商户号绑定的appid（企业号corpid即为此appId）. */
	private String mchAppid;
	
	/**  微信支付分配的商户号. */
	private String mchid;
	
	/**  微信支付分配的终端设备号. */
	private String deviceInfo;
	
	/**  随机字符串，不长于32位. */
	private String nonceStr;
	
	/**  商户订单号，需保持历史全局唯一性(只能是字母或者数字，不能包含有其它字符). */
	private String partnerTradeNo;
	
	/**  企业付款成功，返回的微信付款单号。. */
	private String paymentNo;
	
	/**  企业付款成功时间. */
	private Date paymentTime;

	/**
	 * Gets the 申请商户号的appid或商户号绑定的appid（企业号corpid即为此appId）.
	 *
	 * @return the 申请商户号的appid或商户号绑定的appid（企业号corpid即为此appId）
	 */
	public String getMchAppid() {
		return mchAppid;
	}

	/**
	 * Sets the 申请商户号的appid或商户号绑定的appid（企业号corpid即为此appId）.
	 *
	 * @param mchAppid the new 申请商户号的appid或商户号绑定的appid（企业号corpid即为此appId）
	 */
	public void setMchAppid(String mchAppid) {
		this.mchAppid = mchAppid;
	}

	/**
	 * Gets the 微信支付分配的商户号.
	 *
	 * @return the 微信支付分配的商户号
	 */
	public String getMchid() {
		return mchid;
	}

	/**
	 * Sets the 微信支付分配的商户号.
	 *
	 * @param mchid the new 微信支付分配的商户号
	 */
	public void setMchid(String mchid) {
		this.mchid = mchid;
	}

	/**
	 * Gets the 微信支付分配的终端设备号.
	 *
	 * @return the 微信支付分配的终端设备号
	 */
	public String getDeviceInfo() {
		return deviceInfo;
	}

	/**
	 * Sets the 微信支付分配的终端设备号.
	 *
	 * @param deviceInfo the new 微信支付分配的终端设备号
	 */
	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	/**
	 * Gets the 随机字符串，不长于32位.
	 *
	 * @return the 随机字符串，不长于32位
	 */
	public String getNonceStr() {
		return nonceStr;
	}

	/**
	 * Sets the 随机字符串，不长于32位.
	 *
	 * @param nonceStr the new 随机字符串，不长于32位
	 */
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	/**
	 * Gets the 商户订单号，需保持历史全局唯一性(只能是字母或者数字，不能包含有其它字符).
	 *
	 * @return the 商户订单号，需保持历史全局唯一性(只能是字母或者数字，不能包含有其它字符)
	 */
	public String getPartnerTradeNo() {
		return partnerTradeNo;
	}

	/**
	 * Sets the 商户订单号，需保持历史全局唯一性(只能是字母或者数字，不能包含有其它字符).
	 *
	 * @param partnerTradeNo the new 商户订单号，需保持历史全局唯一性(只能是字母或者数字，不能包含有其它字符)
	 */
	public void setPartnerTradeNo(String partnerTradeNo) {
		this.partnerTradeNo = partnerTradeNo;
	}

	/**
	 * Gets the 企业付款成功，返回的微信付款单号。.
	 *
	 * @return the 企业付款成功，返回的微信付款单号。
	 */
	public String getPaymentNo() {
		return paymentNo;
	}

	/**
	 * Sets the 企业付款成功，返回的微信付款单号。.
	 *
	 * @param paymentNo the new 企业付款成功，返回的微信付款单号。
	 */
	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}

	/**
	 * Gets the 企业付款成功时间.
	 *
	 * @return the 企业付款成功时间
	 */
	public Date getPaymentTime() {
		return paymentTime;
	}

	/**
	 * Sets the 企业付款成功时间.
	 *
	 * @param paymentTime the new 企业付款成功时间
	 */
	public void setPaymentTime(Date paymentTime) {
		this.paymentTime = paymentTime;
	}

}
