package com.qiuxs.cuteframework.core.basic.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.MapMessage;

import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.basic.Constants;
import com.qiuxs.cuteframework.core.basic.ex.ErrorCodes;
import com.qiuxs.cuteframework.core.basic.ex.LogicException;
import com.qiuxs.cuteframework.core.basic.ex.LoginException;
import com.qiuxs.cuteframework.core.basic.i18n.I18nUtils;

public class ExceptionUtils {

	public static final String ERROR_CODE = "errorCode";
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
	public static void throwLoginException(String msgId, Object... args) {
		throwLoginException(ErrorCodes.SessionError.SESSION_INVALID, msgId, args);
	}

	/**
	 * 登陆异常
	 * 
	 * @author qiuxs
	 */
	public static void throwLoginException(int errorCode, String msgId, Object... args) {
		String formatedMsg = I18nUtils.getMessageByLang(Constants.DEFAULT_LANG, msgId, args);
		throw new LoginException(errorCode, formatedMsg);
	}

	/**
	 * 使用指定的消息格式抛出逻辑异常
	 * 
	 * @param msg
	 * @param args
	 */
	public static void throwLogicalException(String msgId, Object... args) {
		throwLogicalException(ErrorCodes.LOGIC_EXCEPTION_CODE, msgId, args);
	}

	/**
	 * 抛出指定错误代码，错误消息的逻辑异常
	 * @author qiuxs
	 *
	 * @param errorCode
	 * @param msgId
	 * @param args
	 *
	 * 创建时间：2018年7月26日 下午10:11:23
	 */
	public static void throwLogicalException(int errorCode, String msgId, Object... args) {
		String formatedMsg = I18nUtils.getMessageByLang(Constants.DEFAULT_LANG, msgId, args);
		throwLogicalExceptionInner(errorCode, formatedMsg);
	}

	private static void throwLogicalExceptionInner(int errorCode, String msg) {
		throw new LogicException(errorCode, msg);
	}

	/**
	 * 抛出运行时异常
	 * @author qiuxs
	 *
	 * @param message
	 *
	 * 创建时间：2018年7月26日 下午10:11:50
	 */
	public static void throwRuntimeException(String message) {
		throwRuntimeException(message, null);
	}

	/**
	 * 抛出制定消息的及源错误的运行时异常
	 * @author qiuxs
	 *
	 * @param message
	 * @param cause
	 *
	 * 创建时间：2018年7月26日 下午10:11:59
	 */
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
		MapMessage msg = new MapMessage();
		msg.put("message", error.getString(ERROR_MSG));
		msg.put(ERROR_CODE, error.getString("code"));
		if (log != null) {
			log.error(msg, e);
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
		if (e instanceof LogicException) {
			error.put("code", ((LogicException) e).getErrorCode());
			error.put(ERROR_MSG, e.getLocalizedMessage());
		} else {
			int errorCode = genErrorCode();
			error.put(ERROR_MSG, "服务端错误：" + errorCode);
			error.put("code", errorCode);
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
	public static Throwable getRtootThrowable(Throwable e) {
		Throwable cause;
		for (;;) {
			cause = e.getCause();
			if (cause == null) {
				return e;
			} else {
				e = cause;
			}
		}
	}
	
	/**
	 * 获取异常的堆栈信息
	 *  
	 * @author qiuxs  
	 * @param e
	 * @return
	 */
	public static String getStackTrace(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		try {
			e.printStackTrace(pw);
			return sw.toString();
		} finally {
			try {
				pw.close();
				sw.close();
			} catch (IOException e1) {
			}
		}
	}

	public static RuntimeException unchecked(Throwable e) {
		return new RuntimeException(e);
	}
}
