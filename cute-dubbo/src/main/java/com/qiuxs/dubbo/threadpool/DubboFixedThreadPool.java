package com.qiuxs.dubbo.threadpool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.threadpool.ThreadPool;
import org.apache.dubbo.common.utils.NamedThreadFactory;




public class DubboFixedThreadPool implements ThreadPool {

	@Override
	public Executor getExecutor(URL url) {
		String name = url.getParameter(CommonConstants.THREAD_NAME_KEY, CommonConstants.DEFAULT_THREAD_NAME);
        int threads = url.getParameter(CommonConstants.THREADS_KEY, CommonConstants.DEFAULT_THREADS);
        int queues = url.getParameter(CommonConstants.QUEUES_KEY, CommonConstants.DEFAULT_QUEUES);
        BlockingQueue<Runnable> queue = queues == 0 ? new SynchronousQueue<Runnable>() : (queues < 0 ? new LinkedBlockingQueue<Runnable>() : new LinkedBlockingQueue<Runnable>(queues));
        return new ThreadPoolExecutor(threads, threads, 0, TimeUnit.MILLISECONDS, queue, new NamedThreadFactory(name, true), new DubboAbortPolicyWithReport(name, url));//替换拒绝策略
	}

}
