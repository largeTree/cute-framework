package com.qiuxs.dubbo.threadpool;

import java.util.Calendar;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.remoting.exchange.Request;
import org.apache.dubbo.remoting.transport.dispatcher.ChannelEventRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qiuxs.cuteframework.core.basic.utils.reflect.FieldUtils;
import com.qiuxs.cuteframework.core.utils.notice.NoticeLogger;
import com.qiuxs.dubbo.DubboContextHolder;

public class DubboAbortPolicyWithReport extends ThreadPoolExecutor.AbortPolicy {

	protected static final Logger log = LoggerFactory.getLogger(DubboAbortPolicyWithReport.class);

	private final String threadName;

	private final URL url;

	public DubboAbortPolicyWithReport(String threadName, URL url) {
	        this.threadName = threadName;
	        this.url = url;
	    }

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
		String msg = String.format("Thread pool is EXHAUSTED!" + " Thread Name: %s, Pool Size: %d (active: %d, core: %d, max: %d, largest: %d), Task: %d (completed: %d)," + " Executor status:(isShutdown:%s, isTerminated:%s, isTerminating:%s), in %s://%s:%d!", threadName, e.getPoolSize(), e.getActiveCount(), e.getCorePoolSize(), e.getMaximumPoolSize(), e.getLargestPoolSize(), e.getTaskCount(), e.getCompletedTaskCount(), e.isShutdown(), e.isTerminated(), e.isTerminating(), url.getProtocol(), url.getIp(), url.getPort());
		log.warn(msg);
		try {
			long startMs = Calendar.getInstance().getTimeInMillis();
			StringBuilder logSb = new StringBuilder("Dump threadpool: ");
			if (r instanceof ChannelEventRunnable) {
				ChannelEventRunnable cer = (ChannelEventRunnable) r;
				Request request = (Request) FieldUtils.getFieldValue(cer, "message");
				if (request != null) {
					Object mData = request.getData();
					if (mData != null) {
						logSb.append("invocation=").append(mData.toString()).append("; ");
					}
				}
			}

			String tpStr = DubboContextHolder.getThreadPoolInfo(null, true);//State.RUNNABLE；拒绝时就看所有的
			logSb.append(" elapsed ").append((Calendar.getInstance().getTimeInMillis() - startMs)).append(" ms; ").append(tpStr);
		} catch (Throwable t) {
			log.error("ext = " + t.getMessage(), t);
			NoticeLogger.error("ext = " + t.getLocalizedMessage(), t);
		}
		throw new RejectedExecutionException(msg);
	}

}
