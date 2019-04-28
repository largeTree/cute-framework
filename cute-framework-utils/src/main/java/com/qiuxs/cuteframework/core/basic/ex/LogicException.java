package com.qiuxs.cuteframework.core.basic.ex;

public class LogicException extends RuntimeException {

	private static final long serialVersionUID = -6661447236163045954L;

	private int errorCode;

	public LogicException() {
	}

	public LogicException(String msg) {
		super(msg);
	}

	public LogicException(int errorCode, String msg) {
		this(msg);
		this.errorCode = errorCode;
	}

	public LogicException(int errorCode, String msg, Exception e) {
		super(msg, e);
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
