package com.qiuxs.cuteframework.tech.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qiuxs.cuteframework.core.context.TLVariableHolder;

/**
 * 简单异步任务
 * @author qiuxs
 *
 * @param <P> 前置参数类型
 */
public abstract class RunnableAsyncTask<P> extends AbstractAsyncTask<P> implements Runnable {

	private static final Logger log = LogManager.getLogger(RunnableAsyncTask.class);

	/**
	 * 构造一个异步任务
	 * @param preparParam
	 */
	public RunnableAsyncTask(P preparParam) {
		super(preparParam);
	}

	@Override
	public final void run() {
		super.init();
		try {
			this.execute(super.getPreparParam());
		} catch (Throwable e) {
			log.error("Async Task exec ext = " + e.getLocalizedMessage(), e);
		} finally {
			TLVariableHolder.clear();
		}
	}

	/**
	 * 任务方法
	 * @param preparParam 前置参数
	 */
	public abstract void execute(P preparParam);

}
