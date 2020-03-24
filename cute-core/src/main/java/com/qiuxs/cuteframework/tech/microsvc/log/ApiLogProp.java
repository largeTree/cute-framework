package com.qiuxs.cuteframework.tech.microsvc.log;

import java.io.Serializable;

public class ApiLogProp implements Serializable {

	private static final long serialVersionUID = 3394454229211021474L;

	/** 服务名 */
	private String svcName;
	/** 来源服务器 */
	private String fromSrv;
	/** 目标服务器 */
	private String toSrv;
	/** 来源 */
	private String fromApp;
	/** 目标 */
	private String toApp;
	/** 全局流水号 */
	private Long globalId;
	/** 客户端IP */
	private String clientIp;
	/** 内部请求ID */
	private String requestId;

	public String getSvcName() {
		return svcName;
	}

	public void setSvcName(String svcName) {
		this.svcName = svcName;
	}

	public String getFromSrv() {
		return fromSrv;
	}

	public void setFromSrv(String fromSrv) {
		this.fromSrv = fromSrv;
	}

	public String getToSrv() {
		return toSrv;
	}

	public void setToSrv(String toSrv) {
		this.toSrv = toSrv;
	}

	public String getFromApp() {
		return fromApp;
	}

	public void setFromApp(String fromApp) {
		this.fromApp = fromApp;
	}

	public String getToApp() {
		return toApp;
	}

	public void setToApp(String toApp) {
		this.toApp = toApp;
	}

	public Long getGlobalId() {
		return globalId;
	}

	public void setGlobalId(Long globalId) {
		this.globalId = globalId;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

}
