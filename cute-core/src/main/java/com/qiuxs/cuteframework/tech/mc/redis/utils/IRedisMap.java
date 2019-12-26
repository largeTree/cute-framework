package com.qiuxs.cuteframework.tech.mc.redis.utils;

import java.util.Map;

import com.qiuxs.cuteframework.tech.mc.redis.RedisFacade;

public interface IRedisMap<K, V> extends Map<K, V> {
	
	public String getNameKey();

	public RedisFacade getRedisFacade();
}
