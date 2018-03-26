package com.qiuxs.cuteframework.core.basic.config.uconfig.ex;

/**
 * 统一配置相关异常
 * @author qiuxs
 *
 */
public class UConfigException extends RuntimeException {

	private static final long serialVersionUID = 1764372385718820303L;

	public UConfigException(String msg) {
		super(msg);
	}

	public UConfigException(String msg, Exception e) {
		super(msg, e);
	}

	public UConfigException(Exception e) {
		super(e);
	}

}
