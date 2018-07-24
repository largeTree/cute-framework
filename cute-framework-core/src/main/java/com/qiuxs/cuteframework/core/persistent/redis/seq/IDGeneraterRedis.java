package com.qiuxs.cuteframework.core.persistent.redis.seq;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDGeneraterable;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class IDGeneraterRedis implements IDGeneraterable {

	private static final String PREFIX = "seq:";

	@Resource
	private EnvironmentContext envContext;

	@Resource
	private JedisPool jedisPool;

	@Override
	public Object getNextId(String tableName) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(envContext.getSeqDbIndex());
		Long next = jedis.incrBy(getKey(tableName), 1L);
		return next;
	}

	private String getKey(String tableName) {
		return PREFIX + tableName;
	}
}
