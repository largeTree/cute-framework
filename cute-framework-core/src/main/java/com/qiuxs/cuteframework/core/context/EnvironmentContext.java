package com.qiuxs.cuteframework.core.context;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@ConfigurationProperties(prefix = EnvironmentContext.PREFIX)
public class EnvironmentContext implements EnvironmentAware {

	private static EnvironmentContext environmentContext;

	protected static final String PREFIX = "env";

	/** 应用名 */
	private String appName;
	/** 服务器名 */
	private String serverId;
	/** 序列类型 */
	private String seqType;
	/** 序列所在数据库索引 */
	private int seqDbIndex;
	/** 是否测试模式 */
	private boolean isTest;
	/** 是否调试模式 */
	private boolean isDebug;
	/** 引入进来的配置文件 */
	private String version;
	/** 全局环境变量 */
	private Environment environment;

	public static String getAppName() {
		return getEnvContext().appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public static String getSeqType() {
		return getEnvContext().seqType;
	}

	public void setSeqType(String seqType) {
		this.seqType = seqType;
	}

	public static int getSeqDbIndex() {
		return getEnvContext().seqDbIndex;
	}

	public void setSeqDbIndex(int seqDbIndex) {
		this.seqDbIndex = seqDbIndex;
	}

	public static String getServerId() {
		return getEnvContext().serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public static boolean isTest() {
		return getEnvContext().isTest;
	}

	public void setTest(boolean isTest) {
		this.isTest = isTest;
	}

	public static boolean isDebug() {
		return getEnvContext().isDebug;
	}

	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}

	public static String getVersion() {
		return getEnvContext().version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public static String getEnvValue(String key) {
		return getEnvContext().environment.getProperty(key);
	}

	public static <T> T getEnvValue(String key, Class<T> targetType) {
		return getEnvContext().environment.getProperty(key, targetType);
	}

	public static <T> T getEnvValue(String key, Class<T> targetType, T defVal) {
		return getEnvContext().environment.getProperty(key, targetType, defVal);
	}

	public static EnvironmentContext getEnvContext() {
		if (EnvironmentContext.environmentContext == null) {
			EnvironmentContext.environmentContext = ApplicationContextHolder.getBean(EnvironmentContext.class);
		}
		return EnvironmentContext.environmentContext;
	}

}
