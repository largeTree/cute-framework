package com.qiuxs.cuteframework.core.persistent.redis.config;

public class JedisConfig {
	/** 连接池配置信息 */
	private RedisPoolConfig pool;

	public RedisPoolConfig getPool() {
		return pool;
	}

	public void setPool(RedisPoolConfig pool) {
		this.pool = pool;
	}

}
