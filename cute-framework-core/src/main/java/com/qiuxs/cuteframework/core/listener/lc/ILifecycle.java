package com.qiuxs.cuteframework.core.listener.lc;


public interface ILifecycle {

	/**
	 * 顺序号
	 * @return
	 */
	public int order();

	/**
	 * 初始化完成后执行
	 */
	public default void started() {
	}

	/**
	 * 销毁时执行
	 */
	public default void destroyed() {
	}

}
