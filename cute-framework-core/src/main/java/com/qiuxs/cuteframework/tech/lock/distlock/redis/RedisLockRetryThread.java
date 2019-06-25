package com.qiuxs.cuteframework.tech.lock.distlock.redis;

import java.util.LinkedList;
import java.util.Queue;

import com.qiuxs.cuteframework.core.log.Console;
import com.qiuxs.cuteframework.tech.mc.redis.RedisFacade;

/**
 * 每个web开一个重试线程，为了清除因网络原因锁删除失败<br>
 * 
 * @author JinXinhua 2016-05-25
 *
 */
public class RedisLockRetryThread extends Thread {

	private static Queue<String> queue = new LinkedList<String>();

	public RedisLockRetryThread() {
		super("RedisLockRetryThread");
	}

	/**
	 * 这个比较重要，用自己的连接池
	 * 
	 * @return
	 */
	private static RedisFacade getRedisFacade() {
		return RedisFacadeHolder.REDIS_DAO;
	}

	/**
	 * 功能描述: 用于延迟初始化RedisFacade. lazy initialization holder class模式
	 */
	private static class RedisFacadeHolder {
		static final RedisFacade REDIS_DAO = new RedisFacade("lock_retry");
	}

	/**
	 * 加入元素,可能多线程加，加同步
	 * 
	 * @param e
	 */
	public synchronized static void offer(String e) {
		Console.log.info("key=" + e + " from " + RedisLock.getThreadKey());
		queue.offer(e);
	}

	public void myWork() {
		try {
			while (true) {
				delLock();
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			Console.log.info(RedisLock.getThreadKey() + " exit," + e.getMessage());
		}
	}

	/**
	 * 因为是单线程的，所以不需要同步
	 */
	private void delLock() {
		String key = queue.peek();
		if (key == null) {
			return;
		}
		Long l = 0L;
		try {
			l = getRedisFacade().del(key);
			// 只要不出异常，都要从队列移除
			queue.poll();
			Console.log.info("del key=" + key + ",l=" + l + ",poll");
		} catch (Exception e) {
			Console.log.error("key=" + key + ",ex=" + e.getMessage());
		}
	}

	public static int getQueueSize() {
		return queue.size();
	}

}
