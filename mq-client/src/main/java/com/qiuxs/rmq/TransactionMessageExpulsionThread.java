package com.qiuxs.rmq;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;
import com.qiuxs.cuteframework.core.tx.IMQTxService;

/**
 * 超时事务消息逐出线程
 * 
 * @author qiuxs
 *
 */
public class TransactionMessageExpulsionThread implements Runnable {
	
	private static Logger log = LoggerFactory.getLogger(TransactionMessageExpulsionThread.class);

	private static ScheduledExecutorService pool = new ScheduledThreadPoolExecutor(1);

	private IMQTxService mqTxService;

	public TransactionMessageExpulsionThread(IMQTxService mqTxservice) {
		this.mqTxService = mqTxservice;
	}

	@Override
	public void run() {
		try {
			this.mqTxService.expulsionTimeoutedTransactions();
		} catch (Throwable e) {
			log.error("Expulsion TransactionMessage ext = " + e.getLocalizedMessage(), e);
		}
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
