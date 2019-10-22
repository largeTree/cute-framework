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

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getSeqType() {
		return seqType;
	}

	public void setSeqType(String seqType) {
		this.seqType = seqType;
	}

	public int getSeqDbIndex() {
		return seqDbIndex;
	}

	public void setSeqDbIndex(int seqDbIndex) {
		this.seqDbIndex = seqDbIndex;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public boolean isTest() {
		return isTest;
	}

	public void setTest(boolean isTest) {
		this.isTest = isTest;
	}

	public boolean isDebug() {
		return isDebug;
	}

	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public String getEnvValue(String key) {
		return this.environment.getProperty(key);
	}

	public <T> T getEnvValue(String key, Class<T> targetType) {
		return this.environment.getProperty(key, targetType);
	}

	public <T> T getEnvValue(String key, Class<T> targetType, T defVal) {
		return this.environment.getProperty(key, targetType, defVal);
	}

	public static EnvironmentContext getEnvContext() {
		if (EnvironmentContext.environmentContext == null) {
			EnvironmentContext.environmentContext = ApplicationContextHolder.getBean(EnvironmentContext.class);
		}
		return EnvironmentContext.environmentContext;
	}

}
