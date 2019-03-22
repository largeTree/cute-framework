package com.qiuxs.cuteframework.core.persistent.redis.seq;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;
import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDGeneraterable;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class IDGeneraterRedis implements IDGeneraterable {

	private static final String PREFIX = "seq:";

	@Resource
	private EnvironmentContext envContext;

	@Override
	public Object getNextId(String tableName) {
		JedisPool jedisPool = ApplicationContextHolder.getBean(JedisPool.class);
		if (jedisPool == null) {
			throw new RuntimeException("No RedisPool Configuration");
		}
		Jedis jedis = jedisPool.getResource();
		jedis.select(this.envContext.getSeqDbIndex());
		Long next = jedis.incrBy(getKey(tableName), 1L);
		jedis.close();
		return next;
	}

	private String getKey(String tableName) {
		return PREFIX + tableName;
	}
}
