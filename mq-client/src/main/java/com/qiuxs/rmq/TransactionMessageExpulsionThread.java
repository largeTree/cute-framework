package com.qiuxs.rmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.qiuxs.cuteframework.core.basic.utils.TimeUtils;
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

//	private static ScheduledExecutorService pool = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
//		private AtomicInteger counter = new AtomicInteger(1);
//		@Override
//		public Thread newThread(Runnable r) {
//			return new Thread(r, "transactionMessageExpThread-" + counter.getAndIncrement());
//		}
//	});

	private IMQTxService mqTxService;

	public TransactionMessageExpulsionThread(IMQTxService mqTxservice) {
		this.mqTxService = mqTxservice;
	}

	@Override
	public void run() {
		try {
			if (log.isDebugEnabled()) {
				log.debug("TransactionMessageExpulsionThread is running ");
			}
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
		ThreadPoolTaskScheduler scheduler = ApplicationContextHolder.getBean("myScheduler");
		TransactionMessageExpulsionThread worker = new TransactionMessageExpulsionThread(mqTxService);
		scheduler.scheduleAtFixedRate(worker, TimeUtils.MINUTE);
	}

//	public static void shutdownWorker() {
//		if (pool != null) {
//			pool.shutdown();
//		}
//	}

}
