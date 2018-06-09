package com.qiuxs.cuteframework.core.basic.ex;

public class LoginException extends LogicException {

	private static final long serialVersionUID = 8097214045978299511L;

	public LoginException() {
		super(ErrorCodes.SessionError.SESSION_INVALID, "无效会话，请重新登陆");
	}

	public LoginException(String msg) {
		super(ErrorCodes.SessionError.SESSION_INVALID, msg);
	}

	public LoginException(int code, String msg) {
		super(code, msg);
	}

}
