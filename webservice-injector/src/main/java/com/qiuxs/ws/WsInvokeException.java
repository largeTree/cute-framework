package com.qiuxs.ws;

public class WsInvokeException extends RuntimeException {

	private static final long serialVersionUID = -7343448094323825417L;

	public WsInvokeException() {
	}

	public WsInvokeException(String msg, Throwable e) {
		super(msg, e);
	}

}
