package com.qiuxs.cuteframework.core.persistent.redis.config;

/**
 * Redis配置对象
 * 
 * @author qiuxs 2019年6月15日 下午10:20:21
 */
public class RedisPoolConfig {

	private int maxIdle;
	private int minIdle;
	private int maxTotal;
	private long maxWaitMillis;

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public long getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(long maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public void initDefault() {
		this.maxIdle = 10;
		this.maxTotal = 20;
		this.maxWaitMillis = 2000L;
	}

}
