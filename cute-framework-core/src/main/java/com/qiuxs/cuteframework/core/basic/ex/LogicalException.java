package com.qiuxs.cuteframework.core.basic.ex;

/**
 * 逻辑异常
 * 
 * @author qiuxs
 *
 */
public class LogicalException extends RuntimeException {
	private static final long serialVersionUID = -4514222607775249304L;

	private int code;

	public LogicalException(int code) {
		this.code = code;
	}

	public LogicalException(int code, String msg) {
		super(msg);
		this.code = code;
	}

	public LogicalException(int code, String msg, Throwable t) {
		super(msg, t);
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}

}
