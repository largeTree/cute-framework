package com.qiuxs.cuteframework.tech.task;

/**
 * 简单异步任务
 * @author qiuxs
 *
 * @param <P> 前置参数类型
 */
public abstract class RunnableAsyncTask<P> extends AbstractAsyncTask<P> implements Runnable {

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
		this.execute(super.getPreparParam());
	}

	/**
	 * 任务方法
	 * @param preparParam 前置参数
	 */
	public abstract void execute(P preparParam);

}
