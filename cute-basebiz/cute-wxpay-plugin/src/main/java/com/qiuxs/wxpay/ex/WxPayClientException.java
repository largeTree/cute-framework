package com.qiuxs.wxpay.ex;

public class WxPayClientException extends RuntimeException {

	private static final long serialVersionUID = 1313434781614381490L;

	public WxPayClientException() {
		super();
	}

	public WxPayClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public WxPayClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public WxPayClientException(String message) {
		super(message);
	}

	public WxPayClientException(Throwable cause) {
		super(cause);
	}

}
