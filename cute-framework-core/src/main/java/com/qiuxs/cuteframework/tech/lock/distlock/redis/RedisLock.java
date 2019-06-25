package com.qiuxs.cuteframework.tech.lock.distlock.redis;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.cuteframework.core.log.Console;
import com.qiuxs.cuteframework.tech.mc.redis.RedisFacade;

import redis.clients.jedis.exceptions.JedisException;

/**
 * 因为hazelcast不稳定，重新开发redis版分布式锁，无超时释放要求，但需要锁超长时间检测<br>
 * 目前想到的死锁可能性：1.web到redis的连接包括redis自身故障(通过删除重试解决)；<br>
 * 2.web服务器在持锁过程中出错导致没有解锁(通过长时间等锁报警解决)。
 * 2016-09-27 锁固定超时10分钟
 * 
 * @author JinXinhua 2016-05-24
 *
 */
public class RedisLock implements Lock {
	/** 一次睡眠毫秒数 */
	private static final long sleepMillis = 10L;

	/** 开始时间，因为多线程，所以需要线程变量 */
	private ThreadLocal<Long> startMillis = new ThreadLocal<Long>();
	/** 长时等锁map,key=锁名-线程id-线程名,value=等锁开始时间 */
	private static final Map<String, Long> mapLongWait = new ConcurrentHashMap<String, Long>();
	/** 超时阈值 */
	private static final long longThreshold = 60000L; // 60*1000毫秒=1分钟,3*60*1000毫秒=3分钟
	/** 长时等key分割符 */
	public static final String LONG_WAIT_KEY_SPLIT = "--";
	/** add by lsf myenv.xml中定义的用于分布锁的redis master，如果未定义，默认使用mymaster*/
	public static final String LOCK_REDIS_POOL_MASTER = "lock_redis_pool_master";
	public static String serverid = null;
	private String name;

	/** 为了整体区分出来，所以前面加 lock: */
	public RedisLock(String name) {
		this.name = "lock:" + name;
		if (serverid == null) {
			serverid = EnvironmentContext.getEnvContext().getServerId();
		}
	}

	/**
	 * lock用的比较多，用自己的连接池
	 * 
	 * @return
	 */
	private static RedisFacade getRedisFacade() {
		return RedisDAOHolder.REDIS_FACADE;
	}

	/**
	 * 功能描述: 用于延迟初始化RedisDAO. lazy initialization holder class模式
	 */
	private static class RedisDAOHolder {
		static RedisFacade REDIS_FACADE;
		static {
			REDIS_FACADE = new RedisFacade("lock");
		}
	}

	/**
	 * 如果锁空闲立即返回 获取失败 一直等待
	 */
	@Override
	public void lock() {
		setStartMillis();
		do {
			if (lock1()) {
				break; // 获得锁
			}
		} while (true);
	}

	private boolean lock1() {
		boolean b = false;
		if (tryLock1()) {
			b = true; // 获得锁
		} else {
			try {
				Thread.sleep(sleepMillis);
				if (getTotalMillis() > longThreshold) {
					putLongWaitKey();
				}
				if (getTotalMillis() < 3 * sleepMillis) {
					removeLongWaitKey();
				}
			} catch (InterruptedException e) {
				Console.log.warn("InterruptedException=" + e.getMessage());
			}
		}
		return b;
	}

	/**
	 * 中断也是单jvm的，所以这儿无任何实现
	 */
	@Override
	public void lockInterruptibly() throws InterruptedException {
	}

	/**
	 * 获取锁 如果锁可用 立即返回true， 否则返回false
	 */
	@Override
	public boolean tryLock() {
		setStartMillis();
		return tryLock1();
	}

	/**
	 * 2016-08-20 加异常时删除锁，一般是超时
	 *  
	 * @author JinXinhua
	 * @return
	 */
	private boolean tryLock1() {
		Long l = null;
		try {
			l = getRedisFacade().setnx(name, RedisLock.getThreadKey());
		} catch (JedisException e) {
			Console.log.error("ext = " + e.getLocalizedMessage());
			unlock();
			throw e;
		}
		if (l == 1) {
			getRedisFacade().expire(name, 600); //锁固定超时10分钟
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 锁在给定的等待时间内空闲，则获取锁成功 返回true， 否则返回false
	 */
	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		setStartMillis();

		long nano = System.nanoTime();
		do {
			if (lock1()) {
				return true;
			}
		} while ((System.nanoTime() - nano) < unit.toNanos(time));
		return false;
	}

	/**
	 * 释放锁,如果删除出错加入重试线程继续删除，因为删除出错可能造成死锁（后面线程一直拿不到锁）
	 */
	@Override
	public void unlock() {
		clearStartMillis();
		removeLongWaitKey();
		try {
			getRedisFacade().del(name);
		} catch (JedisException e) {
			// 只有在出异常情况下才认为删除有问题，因为返回值0也有可能是真不存在
			RedisLockRetryThread.offer(name);
			Console.log.error("ex=" + e.getMessage());
		} finally {
			startMillis.set(null);
		}
	}

	/**
	 * Condition 替代了 Object 监视器方法的使用,是单jvm用的，分布式无用，所以直接返回null
	 */
	@Override
	public Condition newCondition() {
		return null;
	}

	public String getName() {
		return name;
	}

	public static String getThreadKey() {
		String tKey = serverid + LONG_WAIT_KEY_SPLIT + Thread.currentThread().getId() + LONG_WAIT_KEY_SPLIT + Thread.currentThread().getName();
		return tKey;
	}

	private void setStartMillis() {
		startMillis.set(Long.valueOf(System.currentTimeMillis()));
	}

	private void clearStartMillis() {
		startMillis.set(null);
	}

	private long getTotalMillis() {
		return (System.currentTimeMillis() - startMillis.get());
	}

	private String getLongWaitKey() {
		return name + LONG_WAIT_KEY_SPLIT + getThreadKey();
	}

	private void removeLongWaitKey() {
		String longWaitKey = getLongWaitKey();
		if (mapLongWait.containsKey(longWaitKey)) {
			mapLongWait.remove(longWaitKey);
		}
	}

	private void putLongWaitKey() {
		String longWaitKey = getLongWaitKey();
		if (!mapLongWait.containsKey(longWaitKey)) {
			mapLongWait.put(longWaitKey, startMillis.get());
		}
	}

	/**
	 * 
	 * @return 长时等锁map大小，报警用
	 */
	public static int getMapLongWaitSize() {
		return mapLongWait.size();
	}

	/**
	 * 长时等锁map详情
	 * 
	 * @return
	 */
	public static String getMapLongWaitDetails() {
		Set<String> locknames = new HashSet<String>();
		StringBuffer sb = new StringBuffer();
		sb.append("lockname").append(LONG_WAIT_KEY_SPLIT);
		sb.append("threadid").append(LONG_WAIT_KEY_SPLIT).append("threadname:waittime(ms)");
		for (Map.Entry<String, Long> entry : mapLongWait.entrySet()) {
			String key = entry.getKey();
			long wait = System.currentTimeMillis() - entry.getValue();
			sb.append(',').append(key).append(':').append(wait);
			String[] terms = key.split(LONG_WAIT_KEY_SPLIT);
			String lockname = terms[0];
			locknames.add(lockname);
		}
		Iterator<String> iter = locknames.iterator();
		while (iter.hasNext()) {
			String lockname = iter.next();
			String t = getRedisFacade().get(lockname);
			sb.append(';').append(lockname).append(" hold by ").append(t);
		}
		return sb.toString();
	}

	public static void clearMapLongWait() {
		mapLongWait.clear();
	}
}
