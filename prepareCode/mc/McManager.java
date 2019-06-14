package com.hzecool.core.cache.mc;

import com.hzecool.core.cache.mc.McFactory.ServerType;
import com.hzecool.core.concurrent.lock.RedisLockRetryThread;
import com.hzecool.core.context.EnvironmentHolder;
import com.hzecool.core.log.logger.Console;
import com.hzecool.tech.redis.RedisPool;

/**
 * 配置，启动，停止等
 * 
 * @author JinXinhua 2016-05-26
 *
 */
public class McManager {
	private static String mcType;
	private static boolean poolInited = false;
	private static RedisLockRetryThread lockRetryThread = null;

	/**
	 * 环境变量mc_type(缓存类别){Hazelcast|Redis|Local}<br>
	 * 2016-06-12 JinXinhua islocal配置统一成mc_type
	 * 
	 * @throws Exception
	 */
	public static void init() {	
		switch (getMcType()) {
		case "redis":
			initRedis();
			break;
		case "local":
			initLocal();
			break;
		default:
			Console.logger.info("未支持的mc_type=" + mcType);
		}

		// redis序列和配置缓存共同，所以在这儿初始化
		String seqType = EnvironmentHolder.getEnvParam("seq_type");
		if ("redis".equalsIgnoreCase(seqType)) {
			initRedisPool();
		}
	}


	/**
	 * mc_redis_pool_type=standalone(默认单机版,为了开发方便） =sentinel(哨兵,线上高可用版) <br>
	 * mc_redis_pool_addr,主机:端口,多个用逗号分割,例子：<br>
	 * 单机：127.0.0.1:6379(默认)<br>
	 * 哨兵：192.168.4.100:26380,192.168.4.100:26381,192.168.4 .100:26382<br>
	 * mc_redis_pool_db,数据库下标默认0,多个集群共用时使用<br>
	 * mc_redis_pool_pass,redis密码,默认无
	 * 
	 */
	public static void initRedisPool() {
		if (!poolInited) {
			String poolType = EnvironmentHolder.getEnvParam("mc_redis_pool_type");
			String poolAddr = EnvironmentHolder.getEnvParam("mc_redis_pool_addr");
			int dbIndex = EnvironmentHolder.getEnvParamWithDefault("mc_redis_pool_db", 0);
			String password = EnvironmentHolder.getEnvParam("mc_redis_pool_pass");
			String masterName = EnvironmentHolder.getEnvParam("mc_redis_master_name");
			RedisPool.init(poolType, poolAddr, dbIndex, password, masterName);
			Console.logger.info("RedisPool init(),poolType=" + poolType + ",poolAddr=" + poolAddr + ",dbIndex=" 
			+ dbIndex + ",password=" + password + ",masterName=" + masterName);
			poolInited = true;
		}
	}

	private static void initRedis() {
		initRedisPool();
		lockRetryThread = new RedisLockRetryThread();
		lockRetryThread.start();
		McFactory.init(ServerType.redis, null);
		Console.logger.info("McFactory init redis,RedisLockRetryThread start");
	}

	private static void initLocal() {
		McFactory.init(ServerType.local, null);
		Console.logger.info("McFactory init local");
	}

	public static void destroy() {
		switch (getMcType()) {
		case "redis":
			destroyRedis();
			break;
		case "local":
			Console.logger.info("mc_type=" + mcType);
			break;
		default:
			Console.logger.info("未支持的mc_type=" + mcType);
		}

		String seqType = EnvironmentHolder.getEnvParam("seq_type");
		if ("redis".equalsIgnoreCase(seqType)) {
			RedisPool.destroy();
			Console.logger.info("RedisLockRetryThread RedisPool destroy");
		}
	}

	public static void restart() {
		destroyRedis();
		lockRetryThread = null;
		poolInited = false;
		init();
	}

	private static void destroyRedis() {
		if (lockRetryThread != null) {
			lockRetryThread.interrupt();
		}
		RedisPool.destroy();
		Console.logger.info("RedisLockRetryThread RedisPool destroy");
	}

	public static String getMcType() {
		if(mcType == null) {
    		String mcType1 = EnvironmentHolder.getEnvParam("mc_type");
    		if (mcType1 != null) {
    			mcType1 = mcType1.toLowerCase();
    		} else {
    			mcType1 = "unknown";
    		}
    		mcType = mcType1;
		}
		return mcType;
	}

}
