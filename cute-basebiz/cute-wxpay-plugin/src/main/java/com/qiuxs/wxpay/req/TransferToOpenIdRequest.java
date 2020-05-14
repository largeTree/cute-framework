package com.qiuxs.wxpay.req;

import com.qiuxs.wxpay.anno.WxPayApi;
import com.qiuxs.wxpay.resp.TransferToOpenIdResponse;

/**
 * 
 * 企业付款到零钱请求
 * 
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年5月14日 上午11:53:43 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
@WxPayApi("/mmpaymkttransfers/promotion/transfers")
public class TransferToOpenIdRequest extends BaseRequest<TransferToOpenIdResponse> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2642695615749874006L;
	
	
	public enum CheckName {
		/** 不校验真实姓名 */
		NO_CHECK,
		/** 校验真实姓名. */
		FORCE_CHECK
	}

	/**  微信支付分配的终端设备号. */
	private String deviceInfo;
	
	/**  商户订单号，需保持唯一性 (只能是字母或者数字，不能包含有其它字符). */
	private String partnerTradeNo;
	
	/**  商户appid下，某用户的openid. */
	private String openid;
	
	/**  NO_CHECK：不校验真实姓名 		FORCE_CHECK：强校验真实姓名. */
	private String checkName;
	
	/**  收款用户真实姓名。 		如果check_name设置为FORCE_CHECK，则必填用户真实姓名. */
	private String reUserName;
	
	/**  企业付款金额，单位为分. */
	private int amount;
	
	/**  企业付款备注，必填。注意：备注中的敏感词会被转成字符*. */
	private String desc;
	
	/**  该IP同在商户平台设置的IP白名单中的IP没有关联，该IP可传用户端或者服务端的IP。. */
	private String spbillCreateIp;
	

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
	 * Gets the 商户订单号，需保持唯一性 (只能是字母或者数字，不能包含有其它字符).
	 *
	 * @return the 商户订单号，需保持唯一性 (只能是字母或者数字，不能包含有其它字符)
	 */
	public String getPartnerTradeNo() {
		return partnerTradeNo;
	}

	/**
	 * Sets the 商户订单号，需保持唯一性 (只能是字母或者数字，不能包含有其它字符).
	 *
	 * @param partnerTradeNo the new 商户订单号，需保持唯一性 (只能是字母或者数字，不能包含有其它字符)
	 */
	public void setPartnerTradeNo(String partnerTradeNo) {
		this.partnerTradeNo = partnerTradeNo;
	}

	/**
	 * Gets the 商户appid下，某用户的openid.
	 *
	 * @return the 商户appid下，某用户的openid
	 */
	public String getOpenid() {
		return openid;
	}

	/**
	 * Sets the 商户appid下，某用户的openid.
	 *
	 * @param openid the new 商户appid下，某用户的openid
	 */
	public void setOpenid(String openid) {
		this.openid = openid;
	}

	/**
	 * Gets the nO_CHECK：不校验真实姓名 FORCE_CHECK：强校验真实姓名.
	 *
	 * @return the nO_CHECK：不校验真实姓名 FORCE_CHECK：强校验真实姓名
	 */
	public String getCheckName() {
		return checkName;
	}

	/**
	 * Sets the nO_CHECK：不校验真实姓名 FORCE_CHECK：强校验真实姓名.
	 *
	 * @param checkName the new nO_CHECK：不校验真实姓名 FORCE_CHECK：强校验真实姓名
	 */
	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}

	/**
	 * Gets the 收款用户真实姓名。 如果check_name设置为FORCE_CHECK，则必填用户真实姓名.
	 *
	 * @return the 收款用户真实姓名。 如果check_name设置为FORCE_CHECK，则必填用户真实姓名
	 */
	public String getReUserName() {
		return reUserName;
	}

	/**
	 * Sets the 收款用户真实姓名。 如果check_name设置为FORCE_CHECK，则必填用户真实姓名.
	 *
	 * @param reUserName the new 收款用户真实姓名。 如果check_name设置为FORCE_CHECK，则必填用户真实姓名
	 */
	public void setReUserName(String reUserName) {
		this.reUserName = reUserName;
	}

	/**
	 * Gets the 企业付款金额，单位为分.
	 *
	 * @return the 企业付款金额，单位为分
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Sets the 企业付款金额，单位为分.
	 *
	 * @param amount the new 企业付款金额，单位为分
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * Gets the 企业付款备注，必填。注意：备注中的敏感词会被转成字符*.
	 *
	 * @return the 企业付款备注，必填。注意：备注中的敏感词会被转成字符*
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * Sets the 企业付款备注，必填。注意：备注中的敏感词会被转成字符*.
	 *
	 * @param desc the new 企业付款备注，必填。注意：备注中的敏感词会被转成字符*
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * Gets the 该IP同在商户平台设置的IP白名单中的IP没有关联，该IP可传用户端或者服务端的IP。.
	 *
	 * @return the 该IP同在商户平台设置的IP白名单中的IP没有关联，该IP可传用户端或者服务端的IP。
	 */
	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}

	/**
	 * Sets the 该IP同在商户平台设置的IP白名单中的IP没有关联，该IP可传用户端或者服务端的IP。.
	 *
	 * @param spbillCreateIp the new 该IP同在商户平台设置的IP白名单中的IP没有关联，该IP可传用户端或者服务端的IP。
	 */
	public void setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
	}

}
