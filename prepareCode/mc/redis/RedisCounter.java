package com.hzecool.core.cache.mc.redis;

import com.hzecool.tech.redis.RedisDAO;
import com.hzecool.tech.redis.RedisPool;

/**
 * Redis命名计数器
 * 
 * @author JinXinhua 2016-05-26
 *
 */
public class RedisCounter {
	private RedisDAO redisDAO;

	private String nameKey;

	public RedisCounter(String name) {
		this.nameKey = "counter:" + name;
	}

	public RedisCounter(String name, RedisDAO redisDao) {
		this.nameKey = "counter:" + name;
		this.redisDAO = redisDao;
	}

	/**
	 * 功能描述: 用于延迟初始化RedisDAO. lazy initialization holder class模式
	 */
	private static class DefaultRedisDAOHolder {
		/** 计数器用的比较多，用默认连接池 */
		static final RedisDAO REDIS_DAO = new RedisDAO(RedisPool.POOL_NAME_DEFAULT);
	}

	/**
	 * 计数器用的比较多，用默认连接池
	 * 
	 * @return
	 */
	public RedisDAO getRedisDAO() {
		if (redisDAO == null) {
			redisDAO = DefaultRedisDAOHolder.REDIS_DAO;
		}
		return redisDAO;
	}

	/**
	 * 
	 * @return 加1后的值
	 */
	public long incr() {
		return getRedisDAO().incr(nameKey).longValue();
	}

	public long decr() {
		return getRedisDAO().incr(nameKey).longValue();
	}

	public long incrBy(long l) {
		return getRedisDAO().incrBy(nameKey, l).longValue();
	}

	public long decrBy(long l) {
		return getRedisDAO().incrBy(nameKey, l).longValue();
	}

	public void clear() {
		getRedisDAO().del(nameKey);
	}
}
