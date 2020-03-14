package com.qiuxs.cuteframework.tech.log;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AsyncAppender;
import org.apache.logging.log4j.core.appender.db.jdbc.ConnectionSource;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.core.basic.i18n.I18nUtils;
import com.qiuxs.cuteframework.core.log.Console;
import com.qiuxs.cuteframework.core.persistent.database.lookup.DataSourceContext;

@Component
public class CuteJdbcAppenderConfiguration {
	private static boolean isInit = false; // 是否已初始化
	/** buffer满时不等待，直接写到ERROR_REF中 */
	private static final String ERROR_REF = "ErrorFile";

	@Resource
	private DataSource dataSource;

	// inner class
	class Connect implements ConnectionSource {
		@Override
		public Connection getConnection() throws SQLException {
			String oldDsId = DataSourceContext.setLogDb();
			try {
				return dataSource.getConnection();
			} finally {
				DataSourceContext.setUpDs(oldDsId);
			}
		}

		/**
		 * 校验链接是否可用
		 * 
		 * @return
		 */
		public boolean testConnection() {
			try {
				this.getConnection().close();
				return true;
			} catch (SQLException e) {
				Console.log.error("get LogDb Connection Failed ext = " + e.getLocalizedMessage());
				return false;
			}
		}

	}

	/**
	 * 判断是否配置了logdb
	 * 
	 * @return
	 */
	private boolean hasLogdb() {
		return DataSourceContext.getLogDb() != null;
	}

	/**
	 * 启动一个MyJdbc日志处理器。 使用系统设置的日志数据库。如果没有设置日志数据库，将不启动日志处理器。
	 * 
	 * @author liushanhong
	 * @param logTblName 日志数据库表名称
	 * @param colConfig  数据库表字段与日志信息映射关系
	 * @param loggerName Logger名称。该Logger的日志将以异步方式写到数据库表中。如果Logger名称没有定义，将不启动日志处理器。
	 * @param ctx        LoggerContext上下文环境
	 */
	public void startMyJdbcAppender(String loggerName, Appender asyncAppender, Configuration config) {

		// 配置中没有配置loggerName的Logger时，不启动日志处理
		if (!config.getLoggers().containsKey(loggerName)) {
			NoDbLogger.log.warn(I18nUtils.getDefaultLangMsg(LogConstant.MSG_LOG_LOGGER_NAME_NOTEXISTS, loggerName));
			return;
		}

		if (this.hasLogdb()) {
			LoggerConfig loggerConfig = config.getLoggerConfig(loggerName);
			loggerConfig.addAppender(asyncAppender, null, null);
		}
	}

	/**
	 * 生成一个JdbcAppender
	 * 
	 * @param logTblName
	 * @param colConfig
	 * @param config
	 * @return
	 */
	private Appender genJdbcAppender(String logTblName, CuteColumnConfig[] colConfig, Configuration config) {
		String appenderName = LogConstant.JDBC_APPENDER_PREFIX + System.currentTimeMillis();

		Connect conn = new Connect();
		if (!conn.testConnection()) {
			Console.log.warn("LogDb Connect Failed...");
			return null;
		}
		// 创建CuteJdbcAppender
		Appender appender = CuteJdbcAppender.createAppender(appenderName, "true", null, conn,
				LogConstant.JDBC_APPENDER_BUFFSIZE, logTblName, colConfig);
		appender.start();
		config.addAppender(appender);

		// 外面包一层异步Appender
		AppenderRef ref = AppenderRef.createAppenderRef(appenderName, null, null);
		AppenderRef[] refs = new AppenderRef[] { ref };
		String errorRef = null;
		if (config.getAppenders() != null && config.getAppenders().get(ERROR_REF) != null) {
			errorRef = ERROR_REF;
		}
		// 采用blocking模式：
		String asyncAppenderName = appenderName + LogConstant.JDBC_APPENDER_ASYNC_SUFFIX; // 异步Appender名称
		AsyncAppender asyncAppender = AsyncAppender.createAppender(refs, errorRef, false, 0,
				AbstractAppender.parseInt(LogConstant.JDBC_APPENDER_BUFFSIZE, 0), asyncAppenderName, true, null, config,
				false);
//		AsyncAppender asyncAppender = AsyncAppender.newBuilder().setAppenderRefs(refs).setErrorRef(errorRef).setBlocking(false)
//				.setShutdownTimeout(0).setBufferSize(LogConstant.JDBC_APPENDER_ASYNC_BUFFSIZE).setName(asyncAppenderName)
//				.setIncludeLocation(true).setConfiguration(config).setIgnoreExceptions(false).build();

		asyncAppender.start();
		config.addAppender(asyncAppender);
		return asyncAppender;
	}

	@PostConstruct
	private void init() {
		// 保证仅初始化一次
		if (isInit) {
			return;
		}
		isInit = true;
		reconfigureMyJdbcAppender();
	}

	/**
	 * 启动JdbcAppender
	 */
	public void reconfigureMyJdbcAppender() {
		// 没有配置logdb直接返回，不创建数据库日志记录器
		if (!this.hasLogdb()) {
			return;
		}
		final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		final Configuration config = ctx.getConfiguration();

		CuteColumnConfig[] cc = {
				CuteColumnConfig.createColumnConfig(config, LogConstant.COLUMN_SERVERID, "%mdc{serverId}", null, null,
						"false", "string"),
				CuteColumnConfig.createColumnConfig(config, LogConstant.COLUMN_USERID, "%mdc{userId}", null, null,
						"false", "long"),
				CuteColumnConfig.createColumnConfig(config, LogConstant.COLUMN_IP, "%mdc{ip}", null, null, "false",
						"string"),
				CuteColumnConfig.createColumnConfig(config, LogConstant.COLUMN_CLASS_NAME, "%maxLen{%class}{95}", null,
						null, "false", "string"),
				CuteColumnConfig.createColumnConfig(config, LogConstant.COLUMN_METHOD, "%method:%line", null, null,
						"false", "string"),
				CuteColumnConfig.createColumnConfig(config, LogConstant.COLUMN_LOG_TIME, null, null, "true", null,
						"string"),
				CuteColumnConfig.createColumnConfig(config, LogConstant.COLUMN_LEVEL, "%level", null, null, "false",
						"string"),
				// 最大长度：65535->65532由于最后新增"..."；65532->60000由于中文
				CuteColumnConfig.createColumnConfig(config, LogConstant.COLUMN_MSG,
						"%maxLen{%replace{%message}{\'}{\'\'}}{60000}", null, null, "false", "string"),
				// 最大长度：500->497由于最后新增"..."；497->400由于中文
				CuteColumnConfig.createColumnConfig(config, LogConstant.COLUMN_THROWBLE,
						"%maxLen{%replace{%throwable}{\'}{\'\'}}{400}", null, null, "false", "string"),
				CuteColumnConfig.createColumnConfig(config, LogConstant.COLUMN_STACKTRACE,
						"%maxLen{%replace{%rThrowable}{\'}{\'\'}}{60000}", null, null, "false", "string"),
				CuteColumnConfig.createColumnConfig(config, LogConstant.COLUMN_GLOBALID,
						"%mdc{" + LogConstant.COLUMN_GLOBALID + "}", null, null, null, "long"),
				CuteColumnConfig.createColumnConfig(config, LogConstant.COLUMN_THREAD_ID, "%thread", null, null,
						"false", "string"),
				CuteColumnConfig.createColumnConfig(config, LogConstant.COLUMN_ERRORCODE, "%K{errorCode}", null, null,
						"false", "int") };

		Appender asyncAppender = genJdbcAppender("mylog", cc, config);

		if (asyncAppender != null) {
			startMyJdbcAppender("com.qiuxs", asyncAppender, config);// LogManager.ROOT_LOGGER_NAME
			startMyJdbcAppender("com.qiuxs.cuteframework.core.log.Console", asyncAppender, config);
			// startMyJdbcAppender("com.hzecool.core.log.logger.Nagios", asyncAppender,
			// config);
			ctx.updateLoggers();
		}
	}
}
