package com.qiuxs.cuteframework.core.persistent.redis;

/**
 * Redis配置对象
 * @author qiuxs
 * 2019年6月15日 下午10:20:21
 */
public class RedisPoolConfig {

	private Integer maxIdle;
	private Integer maxTotal;
	private Long maxWaitMillis;

	public Integer getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(Integer maxIdle) {
		this.maxIdle = maxIdle;
	}

	public Integer getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(Integer maxTotal) {
		this.maxTotal = maxTotal;
	}

	public Long getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(Long maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public void initDefault() {
		this.maxIdle = 10;
		this.maxTotal = 20;
		this.maxWaitMillis = 2000L;
	}

}
