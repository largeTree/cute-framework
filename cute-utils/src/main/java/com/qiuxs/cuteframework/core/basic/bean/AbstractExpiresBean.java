package com.qiuxs.cuteframework.core.basic.bean;

public abstract class AbstractExpiresBean {

	public abstract long getBornTime();


	public abstract long getExpiresIn();
	
	public long buffTimes() {
		return 200;
	}

	/**
	 * 判断是否过期
	 *
	 * @return true, if expired
	 */
	public boolean expired() {
		// +200s是给一个缓冲时间、认为有效期仅有7000秒
		return (System.currentTimeMillis() - this.getBornTime() + this.buffTimes()) > this.getExpiresIn();
	}

}
