package com.qiuxs.cuteframework.core.persistent.redis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.core.basic.config.IConfiguration;
import com.qiuxs.cuteframework.core.basic.config.UConfigUtils;
import com.qiuxs.cuteframework.core.basic.constants.SymbolConstants;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

// TODO: Auto-generated Javadoc
/**
 * 
 * 功能描述: Redis配置工厂<br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年1月30日 下午5:22:59 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
@Component
public class RedisConfiguration {

	/** The log. */
	private static Logger log = LogManager.getLogger(RedisConfiguration.class);

	/**  配置域. */
	protected static final String CONFIG_DOMAIN = "rds";
	
	/**  默认连接池. */
	public static final String DEFAUL_POOL = "redisDefaultPool";

	/**  地址. */
	private String host;

	/**  端口. */
	private int port = Protocol.DEFAULT_PORT;

	/**  超时时间. */
	private int timeout = Protocol.DEFAULT_TIMEOUT;

	/**  密码. */
	private String password;

	/**  默认数据索引. */
	private int defaultIndex = Protocol.DEFAULT_DATABASE;

	/**  最大空闲. */
	private int maxIdle;
	
	/**  最小空闲. */
	private int minIdle;
	
	/**  最大连接. */
	private int maxTotal;
	
	/**  最大等待时常. */
	private long maxWaitMillis;

	/**  配置持有对象. */
	private static RedisConfiguration redisConfiguration;

	/**  已创建的连接池缓存. */
	private static Map<String, JedisPool> jedisPoolMap = new ConcurrentHashMap<>();

	/**
	 * 获取jedis连接池
	 * 
	 * 2019年6月15日 下午10:23:53.
	 *
	 * @return the jedis pool
	 * @auther qiuxs
	 */
	public static JedisPool getJedisPool() {
		return getJedisPool(DEFAUL_POOL, redisConfiguration.getDefaultIndex());
	}

	/**
	 * 获取jedis连接池
	 * 
	 * 2019年6月15日 下午10:23:53.
	 *
	 * @param poolName the pool name
	 * @return the jedis pool
	 * @auther qiuxs
	 */
	public static JedisPool getJedisPool(String poolName) {
		return getJedisPool(poolName, redisConfiguration.getDefaultIndex());
	}

	/**
	 * 获取指定的链接池
	 * 
	 * 2019年6月15日 下午10:29:04.
	 *
	 * @param poolName the pool name
	 * @param dbIdx the db idx
	 * @return the jedis pool
	 * @auther qiuxs
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
	 * 2019年6月15日 下午11:08:19.
	 *
	 * @auther qiuxs
	 */
	@PostConstruct
	public void initDefaultPoll() {
		
		IConfiguration configuration = UConfigUtils.getDomain(CONFIG_DOMAIN);
		if (configuration == null ) {
			log.info("NoRedis Config...");
			return;
		}
		this.timeout = configuration.getInt("timeout", Protocol.DEFAULT_TIMEOUT);
		this.maxIdle = configuration.getInt("max-idle", 8);
		this.minIdle = configuration.getInt("min-idle", 0);
		this.maxWaitMillis = configuration.getInt("max-wait-millis", -1);
		this.maxTotal = configuration.getInt("max-total", 20);
		
		this.host = configuration.getString("host");
		this.port = configuration.getInt("port", Protocol.DEFAULT_PORT);
		this.password = configuration.getString("password");
		this.defaultIndex = configuration.getInt("database", Protocol.DEFAULT_DATABASE);
		
		if (StringUtils.isBlank(this.getHost())) {
			return;
		}

		if (log.isDebugEnabled()) {
			log.debug("JedisPoolConfig[host = " + this.getHost() + ", port = " + this.getPort() + ", password = "+ this.getPassword() 
					+ ", timeout = " + this.getTimeout() + ", max-idle = " + this.getMaxIdle() + ", defaultIndex = " + this.getDefaultIndex()
					+ ", min-idle = " + this.getMinIdle() + ", max-total = " + this.getMaxTotal() + ", max-wait = " + this.getMaxWaitMillis() + "]");
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
	 * 2019年6月15日 下午11:13:23.
	 *
	 * @param dbIdx the db idx
	 * @return the jedis pool
	 * @auther qiuxs
	 */
	private JedisPool initPool(int dbIdx) {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(this.getMaxIdle());
		jedisPoolConfig.setMaxWaitMillis(this.getMaxWaitMillis());
		jedisPoolConfig.setMaxTotal(this.getMaxTotal());
		JedisPool jedisPool = new JedisPool(jedisPoolConfig, this.getHost(), this.getPort(), this.getTimeout(),
				this.getPassword(), dbIdx);
		return jedisPool;
	}

	/**
	 * Gets the 地址.
	 *
	 * @return the 地址
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Gets the 端口.
	 *
	 * @return the 端口
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Gets the 超时时间.
	 *
	 * @return the 超时时间
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * Gets the 密码.
	 *
	 * @return the 密码
	 */
	private String getPassword() {
		return password;
	}

	/**
	 * Gets the 默认数据索引.
	 *
	 * @return the 默认数据索引
	 */
	public int getDefaultIndex() {
		return defaultIndex;
	}

	/**
	 * Gets the 最大空闲.
	 *
	 * @return the 最大空闲
	 */
	public int getMaxIdle() {
		return maxIdle;
	}

	/**
	 * Gets the 最小空闲.
	 *
	 * @return the 最小空闲
	 */
	public int getMinIdle() {
		return minIdle;
	}

	/**
	 * Gets the 最大连接.
	 *
	 * @return the 最大连接
	 */
	public int getMaxTotal() {
		return maxTotal;
	}

	/**
	 * Gets the 最大等待时常.
	 *
	 * @return the 最大等待时常
	 */
	public long getMaxWaitMillis() {
		return maxWaitMillis;
	}

	/**
	 * Gets the 默认数据索引.
	 *
	 * @return the 默认数据索引
	 */
	public static int getDefaultDbIndex() {
		return redisConfiguration.getDefaultIndex();
	}
	
}
