package com.qiuxs.cuteframework.core.basic.bean;

import java.io.Serializable;

public abstract class AbstractExpiresBean implements Serializable {
	
	private static final long serialVersionUID = 6470453140793929866L;

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
