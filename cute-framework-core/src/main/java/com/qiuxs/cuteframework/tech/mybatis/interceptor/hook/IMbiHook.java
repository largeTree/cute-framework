package com.qiuxs.cuteframework.tech.mybatis.interceptor.hook;

import org.apache.ibatis.plugin.Invocation;
import org.springframework.core.Ordered;

/**
 * MyBatis钩子
 * @author qiuxs
 * 
 * 创建时间 ： 2018年8月17日 下午8:22:59
 *
 */
public interface IMbiHook extends Ordered {
	
	/**
	 * 在ExecutorMbi执行真正方法前执行
	 * @author qiuxs
	 *
	 * @param invocation
	 * @return
	 *
	 * 创建时间：2018年8月17日 下午8:24:55
	 */
	public default void beforeExecutor(Invocation invocation) {};
	
	/**
	 * 在ExecutorMbi执行真正方法之后的finally块中执行
	 * @author qiuxs
	 *
	 * @param invocation
	 * @return
	 *
	 * 创建时间：2018年8月17日 下午8:25:56
	 */
	public default void finallyExecutor(Invocation invocation) {};
	
	/**
	 * 
	 * @author qiuxs
	 *
	 * @param invocation
	 * @return
	 *
	 * 创建时间：2018年8月17日 下午8:26:49
	 */
	public default void afterExecutor(Invocation invocation) {};
	
	/**
	 * 在StatementHandlerMbi执行真正方法前执行
	 * @author qiuxs
	 *
	 * @param invocation
	 *
	 * 创建时间：2018年8月17日 下午8:27:20
	 */
	public default void beforeStatement(Invocation invocation) {};
	
	/**
	 * 在StatementHandlerMbi执行真正方法后的finally块执行
	 * @author qiuxs
	 *
	 * @param invocation
	 *
	 * 创建时间：2018年8月17日 下午8:27:20
	 */
	public default void finallyStatement(Invocation invocation) {};
	
	/**
	 * 在StatementHandlerMbi执行真正方法后执行
	 * @author qiuxs
	 *
	 * @param invocation
	 *
	 * 创建时间：2018年8月17日 下午8:27:20
	 */
	public default void afterStatement(Invocation invocation) {};
	
}
