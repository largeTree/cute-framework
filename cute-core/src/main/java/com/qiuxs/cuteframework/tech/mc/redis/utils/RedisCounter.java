package com.qiuxs.cuteframework.tech.mc.redis.utils;

import com.qiuxs.cuteframework.tech.mc.redis.RedisFacade;

/**
 * Redis命名计数器
 * 
 * @author JinXinhua 2016-05-26
 *
 */
public class RedisCounter {
	private RedisFacade redisFacade;
	private String nameKey;

	public RedisCounter(String name) {
		this.nameKey = "counter:" + name;
	}

	public RedisCounter(String name, RedisFacade redisFacade) {
		this.nameKey = "counter:" + name;
		this.redisFacade = redisFacade;
	}

	/**
	 * 功能描述: 用于延迟初始化RedisDAO. lazy initialization holder class模式
	 */
	private static class DefaultRedisDAOHolder {
		/** 计数器用的比较多，用默认连接池 */
		static final RedisFacade REDIS_DAO = new RedisFacade();
	}

	/**
	 * 计数器用的比较多，用默认连接池
	 * 
	 * @return
	 */
	public RedisFacade getRedisFacade() {
		if (redisFacade == null) {
			redisFacade = DefaultRedisDAOHolder.REDIS_DAO;
		}
		return redisFacade;
	}

	/**
	 * 
	 * @return 加1后的值
	 */
	public long incr() {
		return getRedisFacade().incr(nameKey).longValue();
	}

	public long decr() {
		return getRedisFacade().incr(nameKey).longValue();
	}

	public long incrBy(long l) {
		return getRedisFacade().incrBy(nameKey, l).longValue();
	}

	public long decrBy(long l) {
		return getRedisFacade().incrBy(nameKey, l).longValue();
	}

	public void clear() {
		getRedisFacade().del(nameKey);
	}
}
