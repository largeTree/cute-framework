package com.qiuxs.cuteframework.tech.mc.redis.comn;

import java.io.Serializable;

import com.qiuxs.cuteframework.core.basic.bean.AbstractExpiresBean;

/**
 * 通用缓存包装类
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年9月28日 下午12:05:04 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class DataWrapper extends AbstractExpiresBean {

	private static final long serialVersionUID = 6647217919529882251L;
	
	private final long bornTime;
	private final long expiresIn;
	private Serializable data;

	/**
	 * Creates a new instance of DataWrapper.  
	 *  
	 * @param expiresIn
	 * 		有效时间，毫秒
	 * 
	 */
	public DataWrapper(long expiresIn) {
		bornTime = System.currentTimeMillis();
		this.expiresIn = expiresIn;
	}

	@Override
	public long getBornTime() {
		return this.bornTime;
	}

	@Override
	public long getExpiresIn() {
		return this.expiresIn;
	}

	public Serializable getData() {
		return data;
	}

	public void setData(Serializable data) {
		this.data = data;
	}

}
