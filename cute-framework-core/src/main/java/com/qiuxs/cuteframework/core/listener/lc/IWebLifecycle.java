package com.qiuxs.cuteframework.core.listener.lc;

public interface IWebLifecycle {

	/**
	 * 顺序号
	 * @return
	 */
	public int order();

	/**
	 * Spring初始化之前执行
	 *  
	 * @author qiuxs
	 */
	public default void firstInit() {
	}
	
	/**
	 * Spring销毁后执行
	 *  
	 * @author qiuxs
	 */
	public default void firstDestoryed() {
	}

	/**
	 * Spring初始化之后执行
	 *  
	 * @author qiuxs
	 */
	public default void lastInit() {
	}
	
	/**
	 * Spring销毁前执行
	 *  
	 * @author qiuxs
	 */
	public default void lastDestory() {
	}
	
}
