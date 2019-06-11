package com.qiuxs.cuteframework.tech.task;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;

/**
 * 带返回值的异步任务
 * @author qiuxs
 *
 * @param <P> 构造任务的前置参数类型
 * @param <V> 返回值类型
 */
public abstract class CallableAsyncTask<P, V> extends AbstractAsyncTask<P> implements Callable<V> {

	private static final Logger log = LogManager.getLogger(CallableAsyncTask.class);

	/**
	 * 构造异步任务对象
	 * @param preparParam 前置参数
	 */
	public CallableAsyncTask(P preparParam) {
		super(preparParam);
	}

	@Override
	public V call() throws Exception {
		super.init();
		try {
			return this.call(super.getPreparParam());
		} catch (Throwable e) {
			log.error("Callable Async Task exec ext = " + e.getLocalizedMessage(), e);
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * 异步任务
	 * @param preparParams 前置参数
	 * @return 执行结果
	 * @throws CancellationException 任务被取消时抛出，如提交任务需要在事务提交成功后执行但事务被回滚时
	 */
	public abstract V call(P preparParams) throws CancellationException;

}
