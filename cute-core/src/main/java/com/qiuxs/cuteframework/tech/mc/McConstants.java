package com.qiuxs.cuteframework.tech.mc;

import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.context.EnvironmentContext;

/**
 * 
 * 功能描述: 分布式常量 
 * <p>新增原因: TODO
 *  
 * @author fengdg   
 * @version 1.0.0
 * @since 2018年2月27日 下午3:09:50
 */
public class McConstants {
	public static final Integer DEFAULT_EXPIRE_TIME = 30 * 60;//30分钟

	//部署方式：一个机房一套Redis

	//子系统集群内的Redis配置项和配置值
	public static final String SUB_SYS_CLUSTER_MASTER_NAME_KEY = "mc_redis_master_name";
	public static final String SUB_SYS_CLUSTER_MASTER_NAME_VAL = "mymaster";
	/**不同子系统一般设置成不同值*/
	public static final String SUB_SYS_CLUSTER_DATABASE_KEY = "mc_redis_pool_db";
	public static final int SUB_SYS_CLUSTER_DATABASE_VAL = 0;

	//应用集群内的Redis配置项和配置值
	public static final String APP_CLUSTER_MASTER_NAME_KEY = "sess_redis_master_name";
	public static final String APP_CLUSTER_MASTER_NAME_VAL = "sessmaster";
	/**应用集群内的所有子系统需要配置成相同值*/
	public static final String APP_CLUSTER_DATABASE_KEY = "sess_redis_pool_db";

	//缺省的Redis配置项和配置值
	public static final String DEF_MASTER_NAME_KEY = SUB_SYS_CLUSTER_MASTER_NAME_KEY;
	public static final String DEF_MASTER_NAME_VAL = SUB_SYS_CLUSTER_MASTER_NAME_VAL;
	public static final String DEF_DATABASE_KEY = SUB_SYS_CLUSTER_DATABASE_KEY;
	public static final int DEF_DATABASE_VAL = SUB_SYS_CLUSTER_DATABASE_VAL;

	public static int getDefaultDatabase() {
		return getSubSysDatabase();
	}

	public static String getDefaultMasterName() {
		return getSubSysMasterName();
	}

	public static int getSubSysDatabase() {
		return EnvironmentContext.getIntValue(McConstants.SUB_SYS_CLUSTER_DATABASE_KEY, SUB_SYS_CLUSTER_DATABASE_VAL);
	}

	public static String getSubSysMasterName() {
		return EnvironmentContext.getString(McConstants.SUB_SYS_CLUSTER_MASTER_NAME_KEY, McConstants.SUB_SYS_CLUSTER_MASTER_NAME_VAL);
	}

	public static int getAppClusterDatabase() {
		int database = EnvironmentContext.getIntValue(McConstants.APP_CLUSTER_DATABASE_KEY, getDefaultDatabase());
		return database;
	}

	public static String getAppClusterMasterName() {
		String masterName = EnvironmentContext.getEnvValue(McConstants.APP_CLUSTER_MASTER_NAME_KEY);
		if (StringUtils.isEmpty(masterName)) {
			masterName = getDefaultMasterName();
		}
		return masterName;
	}

}
