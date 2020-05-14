package com.qiuxs.wxpay.resp;

import java.io.Serializable;

import com.qiuxs.cuteframework.core.basic.utils.StringUtils;

/**
 * 返回基类
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年5月14日 上午11:43:04 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class BaseResponse implements Serializable {

	private static final long serialVersionUID = 5177938861772379222L;

	public static final String SUCCESS = "SUCCESS";
	public static final String FAIL = "FAIL";

	/** SUCCESS/FAIL
		此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断 */
	private String returnCode;
	/** 返回信息，如非空，为错误原因 签名失败 参数格式校验错误 */
	private String returnMsg;
	/** SUCCESS/FAIL，注意：当状态为FAIL时，存在业务结果未明确的情况。如果状态为FAIL，请务必关注错误代码（err_code字段），通过查询查询接口确认此次付款的结果。 */
	private String resultCode;
	/** 错误码信息，注意：出现未明确的错误码时（SYSTEMERROR等），请务必用原商户订单号重试，或通过查询接口确认此次付款的结果。 */
	private String errCode;
	/** 结果信息描述 */
	private String errCodeDes;

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrCodeDes() {
		return errCodeDes;
	}

	public void setErrCodeDes(String errCodeDes) {
		this.errCodeDes = errCodeDes;
	}

	public boolean haveSuccess() {
		boolean success = SUCCESS.equals(this.resultCode);
		if (!success) {
			return success;
		}
		if (StringUtils.isNotBlank(errCode)) {
			success = SUCCESS.equals(this.errCode);
		}
		return success;
	}

}
