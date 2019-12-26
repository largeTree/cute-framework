package com.qiuxs.cuteframework.tech.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import com.qiuxs.cuteframework.core.tx.local.AfterCompletionRunnable;
import com.qiuxs.cuteframework.core.tx.local.SpringTxContext;

/**
 * 异步任务执行器
 * 新增原因：方便的执行异步任务，避免需要异步执行时自行手动new Thread来执行
 * @author qiuxs
 *
 */
public class AsyncTaskExecutor {

	/**
	 * 异步任务线程池，先写10个线程，后续支持按配置制定线程数
	 */
	private static ExecutorService thread_pool = Executors.newFixedThreadPool(10);

	/**
	 * 简单异步任务
	 * @param task 需要执行的任务对象
	 * @param isTransactional  是否需要在当前事务提交后执行
	 * 		true：等到当前事务提交成功后才开始执行此任务，需要当前在Spring事务环境中才有效否则立即提交执行
	 * 		false：当前线程池还有空闲的情况下立即开始执行任务，不空闲的话等待空闲开始执行
	 */
	public static void execute(RunnableAsyncTask<?> task, boolean isTransactional) {
		if (isTransactional && SpringTxContext.isTransactional()) {
			SpringTxContext.addAfterCompletionRunnable(new AfterCompletionRunnable<RunnableAsyncTask<?>>(task) {
				@Override
				public void commited(RunnableAsyncTask<?> task) {
					thread_pool.execute(task);
				}
			});
		} else {
			thread_pool.execute(task);
		}
	}

	/**
	 * 带返回值的异步任务
	 * @param task 需要执行的异步任务对象
	 * @param isTransactional 是否需要在当前十五提交后执行
	 * 		true：等到当前事物提交成功后才开始执行此任务，需要当前在Spring事务环境中才有效否则立即提交执行
	 * 			若事务被回滚则取消任务，获取返回值抛出{@link java.util.concurrent.CancellationException}
	 * 		false：当前线程池还有空闲的情况下立即开始执行任务，不空闲时等待空闲
	 * @return
	 */
	public static <P, V> Future<V> submit(CallableAsyncTask<P, V> task, boolean isTransactional) {
		if (isTransactional && SpringTxContext.isTransactional()) {
			FutureTask<V> futureTask = new FutureTask<>(task);
			SpringTxContext.addAfterCompletionRunnable(new AfterCompletionRunnable<FutureTask<V>>(futureTask) {
				@Override
				public void commited(FutureTask<V> task) {
					thread_pool.execute(futureTask);
				}

				@Override
				public void rolledBack(FutureTask<V> task) {
					task.cancel(true);
				}
			});
			return futureTask;
		} else {
			return thread_pool.submit(task);
		}
	}

}
