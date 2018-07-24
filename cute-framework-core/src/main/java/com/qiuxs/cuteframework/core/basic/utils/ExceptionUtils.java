package com.qiuxs.cuteframework.core.basic.utils;

import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.basic.Constants;
import com.qiuxs.cuteframework.core.basic.ex.ErrorCodes;
import com.qiuxs.cuteframework.core.basic.ex.LogicException;
import com.qiuxs.cuteframework.core.basic.ex.LoginException;
import com.qiuxs.cuteframework.core.basic.i18n.I18nUtils;

public class ExceptionUtils {

	public static final String ERROR_CODE = "error_code";
	public static final String ERROR_MSG = "msg";

	/**
	 * 抛出默认登陆异常
	 * 
	 * @author qiuxs
	 */
	public static void throwLoginException() {
		throw new LoginException();
	}

	/**
	 * 登陆异常
	 * 
	 * @author qiuxs
	 */
	public static void throwLoginException(String msgId, String... args) {
		throwLoginException(ErrorCodes.SessionError.SESSION_INVALID, msgId, args);
	}

	/**
	 * 登陆异常
	 * 
	 * @author qiuxs
	 */
	public static void throwLoginException(int errorCode, String msgId, String... args) {
		String formatedMsg = I18nUtils.getMessageByLang(Constants.DEFAULT_LANG, msgId, args);
		throw new LoginException(errorCode, formatedMsg);
	}

	/**
	 * 使用指定的消息格式抛出逻辑异常
	 * 
	 * @param msg
	 * @param args
	 */
	public static void throwLogicalException(String msgId, String... args) {
		throwLogicalException(ErrorCodes.LOGIC_EXCEPTION_CODE, msgId, args);
	}

	public static void throwLogicalException(int errorCode, String msgId, String... args) {
		String formatedMsg = I18nUtils.getMessageByLang(Constants.DEFAULT_LANG, msgId, args);
		throwLogicalExceptionInner(errorCode, formatedMsg);
	}

	private static void throwLogicalExceptionInner(int errorCode, String msg) {
		throw new LogicException(errorCode, msg);
	}

	public static void throwRuntimeException(String message) {
		throwRuntimeException(message, null);
	}

	public static void throwRuntimeException(String message, Throwable cause) {
		throw new RuntimeException(message, cause);
	}

	/**
	 * 记录错误日志
	 * 
	 * @param log
	 * @param e
	 * @return
	 */
	public static JSONObject logError(Logger log, Throwable e) {
		JSONObject error = buildError(e);
		if (log != null) {
			log.error(error.getString(ERROR_CODE), e);
		}
		return error;
	}

	/**
	 * 获取错误详情
	 * 
	 * @param e
	 * @return
	 */
	public static JSONObject buildError(Throwable e) {
		JSONObject error = new JSONObject();
		e = getRtootThrowable(e);
		if (e instanceof LogicException) {
			error.put(ERROR_CODE, ((LogicException) e).getErrorCode());
			error.put(ERROR_MSG, e.getLocalizedMessage());
		} else {
			int errorCode = genErrorCode();
			error.put(ERROR_MSG, "服务端错误：" + errorCode);
			error.put(ERROR_CODE, errorCode);
		}
		return error;
	}

	/**
	 * 生成错误代码
	 * 
	 * @return
	 */
	private static int genErrorCode() {
		return (int) ((Math.random() * 9 + 1) * 100000);
	}

	/**
	 * 获取最原始的异常对象
	 * 
	 * @param e
	 * @return
	 */
	private static Throwable getRtootThrowable(Throwable e) {
		Throwable cause = e.getCause();
		if (cause == null) {
			return e;
		}
		for (;;) {
			e = cause.getCause();
			if (e == null) {
				return cause;
			} else {
				cause = e.getCause();
			}
		}
	}

	public static RuntimeException unchecked(Exception e) {
		return new RuntimeException(e);
	}
}
