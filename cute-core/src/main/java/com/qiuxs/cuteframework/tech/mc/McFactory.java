package com.qiuxs.cuteframework.tech.mc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.qiuxs.cuteframework.core.log.Console;
import com.qiuxs.cuteframework.tech.lock.distlock.redis.RedisLock;
import com.qiuxs.cuteframework.tech.mc.redis.utils.RedisList;
import com.qiuxs.cuteframework.tech.mc.redis.utils.RedisMap;
import com.qiuxs.cuteframework.tech.mc.redis.utils.RedisQueue;
import com.qiuxs.cuteframework.tech.mc.redis.utils.RedisSet;

/**
 * 根据配置创建需要的集合，可能是本地的或远程的。
 * 
 * @author zhangyz created on 2013-10-19
 */
public class McFactory {
	/** 默认本地,防止出空指针 */
	private static ServerType serverType = ServerType.local;

	/** 锁缓存 */
	private Map<String, Lock> mapLock = new ConcurrentHashMap<String, Lock>();

	/** 缓存对象名set,防止单服务器内部重名 */
	private Set<String> mcName = new HashSet<String>();

	private int dbIndex = 0;

	/**
	 * 分布式集合类型枚举<br>
	 * 2016-05-26 JinXinhua seq(序列)只实现了redis
	 * 
	 * @author zhangyz created on 2013-5-9
	 * @since Framework 1.0
	 */
	public enum McType {
		map, queue, list, set, lock, seq
	}

	/**
	 * 内存服务器提供商枚举类型 <br>
	 * 2016-05-25 JinXinhua redis用redis sentinel重新实现(包在redis2)<br>
	 * 2016-06-12 JinXinhua islocal配置统一成mc_type<br>
	 * 
	 * @author zhangyz created on 2013-12-19
	 */
	public enum ServerType {
		redis, local
	}

	/**
	 * 检查有无重名；但分布式环境下多台服务器检查名字有无重复还无法做到！
	 * 
	 * @throws RuntimeException
	 * @author zhangyz created on 2013-5-9
	 */
	private synchronized void checkNameExist(McType mcType, String name) throws RuntimeException {
		String fullName = mcType.toString() + "/" + name;
		if (mcName.contains(fullName))
			throw new RuntimeException("指定的名字 " + fullName + "已经存在!");
		else
			mcName.add(fullName);
	}

	/**
	 * 使用分布式内存情况下，需要在调用之前设置一下参数，只需在系统启动时设置一次即可。
	 * 
	 * @param srvType
	 *            使用的服务类型
	 * @param className
	 *            相应的配置文件(类路径)，一般每种服务都有自己的配置文件,若为null的话使用默认配置
	 * @author zhangyz created on 2013-12-19
	 */
	public static void init(ServerType srvType, String className) {
		serverType = srvType;
	}

	/**
	 * 单例模式，获取map工厂。初始化配置
	 * 
	 * @return
	 * @author zhangyz created on 2013-3-9
	 */
	public static McFactory getFactory() {
		return McFactoryHolder.FACTORY;
	}

	/**
	 * 
	 * 功能描述: 用于延迟初始化factory. lazy initialization holder class模式
	 * <p>新增原因: 性能优于静态域的DCL
	 * <p>新增日期: 2016年8月26日 下午5:41:29
	 *  
	 * @author fengdg   
	 * @version 1.0.0
	 */
	private static class McFactoryHolder {
		static final McFactory FACTORY = new McFactory();
	}

	/**
	 * 创建一个map,采用指定的名字
	 * 
	 * @param mapId
	 *            指定的名字
	 * @param keyClass
	 *            key的类名
	 * @param valueClass
	 *            value的类名
	 * 
	 * @return
	 */
	public <K, V> Map<K, V> createMap(Class<K> keyClass, Class<V> valueClass,
	        int initSize, String mapId) {
		return createMap(keyClass, valueClass, initSize, mapId, true);
	}

	public <K, V> Map<K, V> createMap(Class<K> keyClass, Class<V> valueClass,
	        int initSize, String mapId, boolean checkName) {
		if (checkName) {
			checkNameExist(McType.map, mapId);
		}
		Map<K, V> map = null;
		switch (serverType) {
		case redis:
			map = new RedisMap<K, V>(mapId, dbIndex);
			break;
		case local:
			if (initSize <= 0)
				map = new HashMap<K, V>();
			else
				map = new HashMap<K, V>(initSize);
			break;
		default:
			Console.log.info("未支持的serverType=" + serverType);
		}
		return map;
	}

	public <V> Set<V> createSet(Class<V> valueClass, String setId) {
		return createSet(valueClass, setId, true);
	}

	public <V> Set<V> createSet(Class<V> valueClass, String setId, boolean checkName) {
		if (checkName) {
			checkNameExist(McType.set, setId);
		}
		Set<V> ret = null;
		switch (serverType) {
		case redis:
			ret = new RedisSet<V>(setId, dbIndex);
			break;
		case local:
			ret = new HashSet<V>();
			break;
		default:
			Console.log.info("未支持的serverType=" + serverType);
		}
		return ret;
	}

	/**
	 * 创建一个固定的List
	 * 
	 * @param listId
	 *            列表标识，该列表会被永久启用,用于分布式下场景
	 * @return
	 */
	public <V> List<V> createList(Class<V> valueClass, String listId) {
		return createList(valueClass, listId, true);
	}

	/**
	 * 创建一个固定的List
	 * 
	 * @param listId
	 *            列表标识，该列表会被永久启用,用于分布式下场景
	 * @return
	 * @author zhangyz created on 2013-3-10
	 */
	public <V> List<V> createList(Class<V> valueClass, String listId, boolean checkName) {
		if (checkName) {
			checkNameExist(McType.list, listId);
		}

		List<V> ret = null;
		switch (serverType) {
		case redis:
			ret = new RedisList<V>(listId);
			break;
		case local:
			ret = new ArrayList<V>();
			break;
		default:
			Console.log.info("未支持的serverType=" + serverType);
		}
		return ret;
	}

	/**
	 * 创建一个固定的queue
	 * 
	 * @param valueClass
	 *            list中元素类名
	 * @param listId
	 *            列表标识，该列表会被永久启用,用于分布式下场景
	 * @return
	 * @author zhangyz created on 2013-3-10
	 */
	public <V> BlockingQueue<V> createQueue(Class<V> valueClass, String queueId) {
		checkNameExist(McType.queue, queueId);

		BlockingQueue<V> ret = null;
		switch (serverType) {
		case redis:
			ret = new RedisQueue<V>(queueId);
			break;
		case local:
			ret = new LinkedBlockingQueue<V>();
			break;
		default:
			Console.log.info("未支持的serverType=" + serverType);
		}
		return ret;
	}

	/**
	 * 为了避免客户端锁对象的创建资源浪费
	 * 
	 * @param lockName
	 * @return
	 * @author zhangyz created on 2013-4-26
	 */
	public Lock getLock(String lockName) {
		Lock lock = mapLock.get(lockName);
		if (lock == null) {
			lock = createLock(lockName);
			mapLock.put(lockName, lock);
		}
		return lock;
	}

	private Lock createLock(String lockName) {
		Lock ret = null;
		switch (serverType) {
		case redis:
			ret = new RedisLock(lockName);
			break;
		case local:
			ret = new ReentrantLock();
			break;
		default:
			Console.log.info("未支持的serverType=" + serverType);
		}
		return ret;
	}

	/**
	 * 出现异常清除缓存名称重新加载
	 */
	public void clearMcName() {
		mcName.clear();
	}

	public Set<String> getMcName() {
		return mcName;
	}

}
