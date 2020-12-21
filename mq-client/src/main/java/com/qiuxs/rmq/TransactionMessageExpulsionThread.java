package com.qiuxs.rmq;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;
import com.qiuxs.cuteframework.core.tx.IMQTxService;

/**
 * 超时事务消息逐出线程
 * 
 * @author qiuxs
 *
 */
public class TransactionMessageExpulsionThread implements Runnable {

	private static ScheduledExecutorService pool = new ScheduledThreadPoolExecutor(1);

	private IMQTxService mqTxService;

	public TransactionMessageExpulsionThread(IMQTxService mqTxservice) {
		this.mqTxService = mqTxservice;
	}

	@Override
	public void run() {
		this.mqTxService.expulsionTimeoutedTransactions();
	}

	/**
	 * 启动
	 */
	public static void startWorker() {
		IMQTxService mqTxService = ApplicationContextHolder.getBean(IMQTxService.class);
		TransactionMessageExpulsionThread worker = new TransactionMessageExpulsionThread(mqTxService);
		pool.scheduleAtFixedRate(worker, 0, 1, TimeUnit.MINUTES);
	}

	public static void shutdownWorker() {
		if (pool != null) {
			pool.shutdown();
		}
	}

}
