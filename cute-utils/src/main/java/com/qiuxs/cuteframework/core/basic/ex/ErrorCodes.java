package com.qiuxs.cuteframework.core.basic.ex;

public class ErrorCodes {

	public static final int LOGIC_EXCEPTION_CODE = -10;

	public static class DataError {
		/** 更新时没有传递ID参数 */
		public static final int UPDATE_NO_ID = -2001;
		/** 无效主键 */
		public static final int INVALID_PRIMARY_KEY = -2002;
	}

	public static class SessionError {
		/** 无效会话 */
		public static final int SESSION_INVALID = -4001;

		/** 用户不存在 */
		public static final int USER_CODE_NOT_EXISTS = -4002;

		/** 密码错误 */
		public static final int PASSWORD_ERROR = -4003;
	}
}
