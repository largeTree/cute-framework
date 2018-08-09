package com.qiuxs.cuteframework.core.persistent.redis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.qiuxs.cuteframework.core.basic.utils.StringUtils;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfiguration implements EnvironmentAware {

	private static Logger log = LogManager.getLogger(RedisConfiguration.class);

	//	@Value("${spring.redis.host}")
	private String host;

	//	@Value("${spring.redis.port}")
	private Integer port;

	//	@Value("${spring.redis.timeout}")
	private Integer timeout;

	//	@Value("${spring.redis.jedis.pool.max-idle}")
	private Integer maxIdle;
	
	private Integer maxTotal;

	//	@Value("${spring.redis.jedis.pool.max-wait}")
	private Long maxWaitMillis;

	//	@Value("${spring.redis.password}")
	private String password;

	private boolean hasConfig = false;

	@Override
	public void setEnvironment(Environment environment) {
		this.host = environment.getProperty("spring.redis.host");
		this.port = environment.getProperty("spring.redis.port", int.class);
		this.timeout = environment.getProperty("spring.redis.timeout", int.class);
		this.maxIdle = environment.getProperty("spring.redis.jedis.pool.max-idle", int.class);
		this.maxTotal = environment.getProperty("spring.redis.jedis.pool.max-total", int.class);
		this.maxWaitMillis = environment.getProperty("spring.redis.jedis.pool.max-wait", long.class);
		this.password = environment.getProperty("spring.redis.password");
		hasConfig = StringUtils.isNotBlank(host);
	}

	@Bean
	public JedisPool redisPoolFactory() {
		if (!hasConfig) {
			return null;
		}
		
		// 设置默认配置
		this.initDefaultConfig();
		
		if (log.isDebugEnabled()) {
			log.debug("JedisPoolConfig[host=" + this.host + ",port=" + this.port + ",password=" + this.password
					+ ",timeout=" + this.timeout + ",max-idle=" + this.maxIdle + ",max-wait=" + this.maxWaitMillis
					+ "]");
		}
		
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
		jedisPoolConfig.setMaxTotal(maxTotal);
		JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);
		log.info("inited jedisPool....");
		return jedisPool;
	}

	private void initDefaultConfig() {
		this.port = this.port == null ? 6379 : this.port;
		this.timeout = this.timeout == null ? 0 : this.timeout;
		this.maxIdle = this.maxIdle == null ? 8 : this.maxIdle;
		this.maxWaitMillis = this.maxWaitMillis == null ? -1 : this.maxWaitMillis;
		this.maxTotal = this.maxTotal == null ? 8 : this.maxTotal;
	}
	
}
