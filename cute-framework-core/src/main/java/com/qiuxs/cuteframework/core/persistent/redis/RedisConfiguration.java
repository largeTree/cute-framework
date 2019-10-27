package com.qiuxs.cuteframework.core.persistent.redis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.qiuxs.cuteframework.core.basic.constants.SymbolConstants;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.persistent.redis.config.JedisConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;
import redis.clients.util.Pool;

@Configuration
@ConfigurationProperties(prefix = RedisConfiguration.PREFIX)
public class RedisConfiguration {

	private static Logger log = LogManager.getLogger(RedisConfiguration.class);

	protected static final String PREFIX = "spring.redis";

	public static final String DEFAUL_POOL = "redisDefaultPool";

	/** 地址 */
	private String host;

	/** 端口 */
	private int port = Protocol.DEFAULT_PORT;

	/** 超时时间 */
	private int timeout = Protocol.DEFAULT_TIMEOUT;

	/** jedis配置 */
	private JedisConfig jedis;

	/** 密码 */
	private String password;

	/** 默认数据索引 */
	private int defaultIndex = Protocol.DEFAULT_DATABASE;

	/** 配置持有对象 */
	private static RedisConfiguration redisConfiguration;

	/** 已创建的连接池缓存 */
	private static Map<String, JedisPool> jedisPoolMap = new ConcurrentHashMap<>();

	/**
	 * 获取jedis连接池
	 * 
	 * 2019年6月15日 下午10:23:53
	 * 
	 * @auther qiuxs
	 * @return
	 */
	public static JedisPool getJedisPool() {
		return getJedisPool(DEFAUL_POOL, redisConfiguration.defaultIndex);
	}

	/**
	 * 获取jedis连接池
	 * 
	 * 2019年6月15日 下午10:23:53
	 * 
	 * @auther qiuxs
	 * @return
	 */
	public static JedisPool getJedisPool(String poolName) {
		return getJedisPool(poolName, redisConfiguration.defaultIndex);
	}

	/**
	 * 获取指定的链接池
	 * 
	 * 2019年6月15日 下午10:29:04
	 * 
	 * @auther qiuxs
	 * @param poolName
	 * @return
	 */
	public static JedisPool getJedisPool(String poolName, int dbIdx) {
		if (jedisPoolMap.size() == 0) {
			ExceptionUtils.throwRuntimeException("not inited ...");
		}
		String poolKey = poolName + SymbolConstants.SEPARATOR_HYPHEN + dbIdx;
		JedisPool jedisPool = jedisPoolMap.get(poolKey);
		if (jedisPool == null) {
			synchronized (jedisPoolMap) {
				jedisPool = jedisPoolMap.get(poolKey);
				if (jedisPool == null) {
					jedisPool = redisConfiguration.initPool(dbIdx);
					jedisPoolMap.put(poolKey, jedisPool);
				}
			}
		}
		return jedisPool;
	}

	/**
	 * 初始化默认连接池
	 * 
	 * 2019年6月15日 下午11:08:19
	 * 
	 * @auther qiuxs
	 */
	@PostConstruct
	public void initDefaultPoll() {
		if (StringUtils.isBlank(this.getHost())) {
			return;
		}

		if (log.isDebugEnabled()) {
			log.debug("JedisPoolConfig[host=" + this.getHost() + ",port=" + this.getPort() + ",password="
					+ this.getPassword() + ",timeout=" + this.getTimeout() + ",max-idle=" + this.getJedis().getPool().getMaxIdle()
					+ ",max-wait=" + this.getJedis().getPool().getMaxWaitMillis() + "]");
		}

		JedisPool jedisPool = this.initPool(this.getDefaultIndex());
		RedisConfiguration.jedisPoolMap.put(DEFAUL_POOL, jedisPool);

		// 缓存一个自身对象
		redisConfiguration = this;
		log.info("inited default jedisPool....");
	}

	/**
	 * 初始化一个连接池
	 * 
	 * 2019年6月15日 下午11:13:23
	 * 
	 * @auther qiuxs
	 * @param dbIdx
	 * @return
	 */
	private JedisPool initPool(int dbIdx) {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(this.getJedis().getPool().getMaxIdle());
		jedisPoolConfig.setMaxWaitMillis(this.getJedis().getPool().getMaxWaitMillis());
		jedisPoolConfig.setMaxTotal(this.getJedis().getPool().getMaxTotal());
		JedisPool jedisPool = new JedisPool(jedisPoolConfig, this.getHost(), this.getPort(), this.getTimeout(),
				this.getPassword(), dbIdx);
		return jedisPool;
	}

	private String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	private int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	private int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	private String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private int getDefaultIndex() {
		return defaultIndex;
	}

	public void setDefaultIndex(int defaultIndex) {
		this.defaultIndex = defaultIndex;
	}

	private JedisConfig getJedis() {
		return jedis;
	}

	public void setJedis(JedisConfig jedis) {
		this.jedis = jedis;
	}

}
