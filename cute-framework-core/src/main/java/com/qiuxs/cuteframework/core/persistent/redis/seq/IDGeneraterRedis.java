package com.qiuxs.cuteframework.core.persistent.redis.seq;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDGeneraterable;
import com.qiuxs.cuteframework.core.persistent.redis.RedisConfiguration;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class IDGeneraterRedis implements IDGeneraterable {
	
	private static Logger log = LogManager.getLogger(IDGeneraterRedis.class);

	private static final String POOL_NAME = "seq_pool";

	private static final String PREFIX = "seq:";

	@Override
	public Object getNextId(String tableName) {
		JedisPool jedisPool = RedisConfiguration.getJedisPool(POOL_NAME, EnvironmentContext.getSeqDbIndex());
		if (jedisPool == null) {
			throw new RuntimeException("No RedisPool Configuration");
		}
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(EnvironmentContext.getSeqDbIndex());
			Long next = jedis.incrBy(getKey(tableName), 1L);
			return next;
		} catch (Exception e) {
			log.error("get next id for table[" + tableName + "] failed ext = " + e.getLocalizedMessage(), e);
			throw e;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	private String getKey(String tableName) {
		return PREFIX + tableName;
	}
}
