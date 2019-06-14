package com.hzecool.frm.monitor.bean;

/**
 * 日志记录对象 
 * @author laisf   
 * @version 1.0.0
 */
public class LogInfoBean {
	private long start;//开始时间戳
	private String requrl;//请求url
	private String reqsn;//线程标识
	
	public long getStart() {
		return start;
	}
	public void setStart(long start) {
		this.start = start;
	}
	public String getRequrl() {
		return requrl;
	}
	public void setRequrl(String requrl) {
		this.requrl = requrl;
	}
	public String getReqsn() {
		return reqsn;
	}
	public void setReqsn(String reqsn) {
		this.reqsn = reqsn;
	}
	
}
