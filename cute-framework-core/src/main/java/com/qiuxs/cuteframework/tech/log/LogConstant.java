package com.qiuxs.cuteframework.tech.log;

public class LogConstant {
	
	public static final String COLUMN_IP = "ip";
	public static final String COLUMN_SERVERID = "server_id";
	public static final String COLUMN_USERID = "user_id";
	public static final String COLUMN_GLOBALID = "global_id";
	public static final String COLUMN_MSG = "msg";
	public static final String COLUMN_CLASS_NAME = "class_name";
	public static final String COLUMN_METHOD = "method";
	public static final String COLUMN_LEVEL = "level";
	public static final String COLUMN_THROWBLE = "throwable";
	public static final String COLUMN_STACKTRACE = "stacktrace";
	public static final String COLUMN_THREAD_ID = "thread_id";
	public static final String COLUMN_ERRORCODE = "error_code";
	public static final String COLUMN_LOG_TIME = "log_time";

	//liushanhong add 2017/2/8
	/**
	 * jdbc日志appender缓存大小
	 */
	public static final String JDBC_APPENDER_BUFFSIZE = "10";

	/**
	 * jdbc日志appender名称前缀
	 */
	public static final String JDBC_APPENDER_PREFIX = "MyJdbc-";

	/**
	 * jdbc日志异步appender名称后缀
	 */
	public static final String JDBC_APPENDER_ASYNC_SUFFIX = "-Async";

	/**
	 * jdbc日志异步appender缓存大小。缓存已满时，后续日志将被丢弃。
	 */
	public static final int JDBC_APPENDER_ASYNC_BUFFSIZE = 128;

	/**
	 * Logger名称不存在时，显示的消息
	 */
	public static final String MSG_LOG_LOGGER_NAME_NOTEXISTS = "log_logger_name_notexists";
}
