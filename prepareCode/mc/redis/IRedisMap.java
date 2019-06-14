package com.hzecool.core.cache.mc.redis;

import java.util.Map;

import com.hzecool.tech.redis.RedisDAO;

public interface IRedisMap<K, V> extends Map<K, V> {
	public String getNameKey();
	public RedisDAO getRedisDAO();
}
