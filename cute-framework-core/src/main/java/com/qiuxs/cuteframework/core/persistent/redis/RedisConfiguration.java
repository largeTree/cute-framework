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
	private Integer port = Protocol.DEFAULT_PORT;

	/** 超时时间 */
	private Integer timeout = Protocol.DEFAULT_TIMEOUT;

	/** 连接池配置信息 */
	private RedisPoolConfig pool;

	/** 密码 */
	private String password;

	/** 默认数据索引 */
	private Integer defaultIndex = Protocol.DEFAULT_DATABASE;

	/** 配置持有对象 */
	private static RedisConfiguration redisConfiguration;

	/** 已创建的连接池缓存 */
	private static Map<String, Pool<Jedis>> jedisPoolMap = new ConcurrentHashMap<>();

	/**
	 * 获取jedis连接池
	 * 
	 * 2019年6月15日 下午10:23:53
	 * @auther qiuxs
	 * @return
	 */
	public static Pool<Jedis> getJedisPool() {
		return getJedisPool(DEFAUL_POOL, redisConfiguration.defaultIndex);
	}

	/**
	 * 获取jedis连接池
	 * 
	 * 2019年6月15日 下午10:23:53
	 * @auther qiuxs
	 * @return
	 */
	public static Pool<Jedis> getJedisPool(String poolName) {
		return getJedisPool(poolName, redisConfiguration.defaultIndex);
	}

	/**
	 * 获取指定的链接池
	 * 
	 * 2019年6月15日 下午10:29:04
	 * @auther qiuxs
	 * @param poolName
	 * @return
	 */
	public static Pool<Jedis> getJedisPool(String poolName, int dbIdx) {
		if (jedisPoolMap.size() == 0) {
			ExceptionUtils.throwRuntimeException("not inited ...");
		}
		String poolKey = poolName + SymbolConstants.SEPARATOR_HYPHEN + dbIdx;
		Pool<Jedis> jedisPool = jedisPoolMap.get(poolKey);
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
	 * @auther qiuxs
	 */
	@PostConstruct
	public void initDefaultPoll() {
		if (StringUtils.isBlank(this.getHost())) {
			return;
		}

		if (log.isDebugEnabled()) {
			log.debug("JedisPoolConfig[host=" + this.getHost() + ",port=" + this.getPort() + ",password=" + this.getPassword() + ",timeout=" + this.getTimeout() + ",max-idle=" + this.getPool().getMaxIdle() + ",max-wait=" + this.getPool().getMaxWaitMillis() + "]");
		}

		Pool<Jedis> jedisPool = this.initPool(this.defaultIndex);
		RedisConfiguration.jedisPoolMap.put(DEFAUL_POOL, jedisPool);

		// 缓存一个自身对象
		redisConfiguration = this;
		log.info("inited default jedisPool....");
	}

	/**
	 * 初始化一个连接池
	 * 
	 * 2019年6月15日 下午11:13:23
	 * @auther qiuxs
	 * @param dbIdx
	 * @return
	 */
	private Pool<Jedis> initPool(int dbIdx) {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(this.getPool().getMaxIdle());
		jedisPoolConfig.setMaxWaitMillis(this.getPool().getMaxWaitMillis());
		jedisPoolConfig.setMaxTotal(this.getPool().getMaxTotal());
		JedisPool jedisPool = new JedisPool(jedisPoolConfig, this.getHost(), this.getPort(), this.getTimeout(), this.getPassword(), dbIdx);
		return jedisPool;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getDefaultIndex() {
		return defaultIndex;
	}

	public void setDefaultIndex(Integer defaultIndex) {
		this.defaultIndex = defaultIndex;
	}

	public RedisPoolConfig getPool() {
		this.pool = this.pool == null ? new RedisPoolConfig() : this.pool;
		return pool;
	}

	public void setPool(RedisPoolConfig pool) {
		this.pool = pool;
	}
}
