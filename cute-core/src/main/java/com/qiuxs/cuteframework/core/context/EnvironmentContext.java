package com.qiuxs.cuteframework.core.context;

import com.qiuxs.cuteframework.core.basic.config.IConfiguration;
import com.qiuxs.cuteframework.core.basic.config.UConfigUtils;
import com.qiuxs.cuteframework.tech.mc.McFactory.McServerType;

/**
 * 环境参数上下文
 * 功能描述: <p>  
 * 新增原因: TODO<p>  
 * 新增日期: 2019年12月31日 上午10:05:29 <p>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class EnvironmentContext {

	protected static final String DOMAIN_KEY = "env";

	private static IConfiguration domain;

	public static String getAppName() {
		return getDoamin().getString("app-name");
	}

	public static String getSeqType() {
		return getDoamin().getString("seq-type");
	}

	public static int getSeqDbIndex() {
		return getDoamin().getInt("db-seq-index", 0);
	}

	public static String getServerId() {
		return getDoamin().getString("server-id");
	}

	public static boolean isTest() {
		return getDoamin().getBool("is-test", false);
	}

	public static boolean isDebug() {
		return getDoamin().getBool("is-debug", false);
	}

	public static String getVersion() {
		return getDoamin().getString("version");
	}

	public static int getWebIndex() {
		return getDoamin().getInt("web-index", 0);
	}

	public static boolean isTimerTaskOpen() {
		return getDoamin().getBool("timer-task-open", false);
	}

	public static McServerType getMcServerType() {
		String mcServerType = getDoamin().getString("mc-server-type", McServerType.local.name());
		return McServerType.valueOf(mcServerType);
	}

	public static String getEnvValue(String key) {
		return getDoamin().getString(key);
	}

	public static int getIntValue(String key, int defVal) {
		return getDoamin().getInt(key, defVal);
	}

	public static String getString(String key) {
		return getDoamin().getString(key);
	}

	public static String getString(String key, String defVal) {
		return getDoamin().getString(key, defVal);
	}

	public static boolean getBool(String key, boolean defVal) {
		return getDoamin().getBool(key, defVal);
	}

	private static IConfiguration getDoamin() {
		if (EnvironmentContext.domain == null) {
			EnvironmentContext.domain = UConfigUtils.getDomain(DOMAIN_KEY);
		}
		return EnvironmentContext.domain;
	}

}
