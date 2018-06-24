package com.qiuxs.cuteframework.core.persistent.redis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfiguration {

	private static Logger log = LogManager.getLogger(RedisConfiguration.class);

	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private int port;

	@Value("${spring.redis.timeout}")
	private int timeout;

	@Value("${spring.redis.jedis.pool.max-idle}")
	private int maxIdle;

	@Value("${spring.redis.jedis.pool.max-wait}")
	private long maxWaitMillis;

	@Value("${spring.redis.password}")
	private String password;

	@Bean
	public JedisPool redisPoolFactory() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
		JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);
		log.info("inited jedisPool....");
		if (log.isDebugEnabled()) {
			log.debug("JedisPoolConfig[host=" + this.host + ",port=" + this.port + ",password=" + this.password
					+ ",timeout=" + this.timeout + ",max-idle=" + this.maxIdle + ",max-wait=" + this.maxWaitMillis
					+ "]");
		}
		return jedisPool;
	}

}
