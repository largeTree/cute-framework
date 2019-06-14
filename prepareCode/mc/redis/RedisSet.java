package com.hzecool.core.cache.mc.redis;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.hzecool.fdn.utils.StringUtils;
import com.hzecool.fdn.utils.io.SerializeUtil;
import com.hzecool.tech.redis.RedisDAO;
import com.hzecool.tech.redis.RedisPool;

/**
 * redis命名hashSet
 * 
 * @author JinXinhua 2016-05-26
 *
 */
public class RedisSet<E> implements Set<E> {
	private RedisDAO redisDAO;
	
	private String nameKey;
	private byte[] namekeyBytes;

	public RedisSet(String name) {
		this.nameKey = "set:" + name;
		this.namekeyBytes = this.nameKey.getBytes();
	}
	
	public RedisSet(String name, RedisDAO redisDao) {
		this.nameKey = "set:" + name;
		this.namekeyBytes = this.nameKey.getBytes();
		this.redisDAO = redisDao;
	}
	
	public RedisSet(String name, String dbIndex, String masterName) {
		this(name);
		//dbIndex和masterName同时为null时，就用mc_redis_pool_db和mc_redis_master_name配置的dbIndex和masterName
		if(StringUtils.isNotEmpty(dbIndex) || StringUtils.isNotEmpty(masterName)){
			this.redisDAO = new RedisDAO(RedisPool.POOL_NAME_DEFAULT, dbIndex, masterName);
		}
	}
	/**
	 * 功能描述: 用于延迟初始化RedisDAO. lazy initialization holder class模式
	 */
	private static class DefaultRedisDAOHolder {
		/** set用的比较少，用默认连接池 */
		static final RedisDAO REDIS_DAO = new RedisDAO(RedisPool.POOL_NAME_DEFAULT);
	}
	
	/**
	 * 初始化时未指定dao，则使用默认的dao
	 * @return
	 */
	public RedisDAO getRedisDAO() {
		if (redisDAO == null) {
			redisDAO = DefaultRedisDAOHolder.REDIS_DAO;
		}
		return redisDAO;
	}

	@Override
	public int size() {
		Long l = getRedisDAO().scard(nameKey);
		return l.intValue();
	}

	@Override
	public boolean isEmpty() {
		boolean b = false;
		if (size() == 0) {
			b = true;
		}
		return b;
	}

	@Override
	public boolean contains(Object o) {
		byte[] oBytes = SerializeUtil.serialize(o);
		Boolean b = getRedisDAO().sismember(namekeyBytes, oBytes);
		return b.booleanValue();
	}

	/**
	 * 开销很大，没有实现
	 */
	@Override
	public Iterator<E> iterator() {
		return getSet().iterator();
//		throw new UnsupportedOperationException("成本高，未实现");
	}

	@Override
	public Object[] toArray() {
		Set<byte[]> set = getRedisDAO().smembers(namekeyBytes);
		Object[] a = null;
		if (set != null && set.size() > 0) {
			a = new Object[set.size()];
			int i = 0;
			Iterator<byte[]> iter = set.iterator();
			while (iter.hasNext()) {
				Object o = SerializeUtil.unserialize(iter.next());
				a[i] = o;
				i++;
			}
		}
		return a;
	}
	
	private Set<E> getSet(){
		Set<E> setRet = new HashSet<E>();
		Set<byte[]> set = getRedisDAO().smembers(namekeyBytes);
		if (set != null && set.size() > 0) {
			Iterator<byte[]> iter = set.iterator();
			while (iter.hasNext()) {
				E e = SerializeUtil.unserialize(iter.next());
				setRet.add(e);
			}
		}
		return setRet;
	}

	/**
	 * 这个方法不理解要做什么，未实现
	 */
	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException("成本高，未实现");
	}

	@Override
	public boolean add(E e) {
		byte[] eBytes = SerializeUtil.serialize(e);
		Long l = getRedisDAO().sadd(namekeyBytes, eBytes);
		boolean b = false;
		if (l > 0) {
			b = true;
		}
		return b;
	}

	@Override
	public boolean remove(Object o) {
		byte[] eBytes = SerializeUtil.serialize(o);
		Long l = getRedisDAO().srem(namekeyBytes, eBytes);
		boolean b = false;
		if (l > 0) {
			b = true;
		}
		return b;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		boolean b = true;
		if (c != null && !c.isEmpty()) {
			Iterator<?> iter = c.iterator();
			while (iter.hasNext()) {
				if (!contains(iter.next())) {
					b = false; // 任一不包含
					break;
				}
			}
		}
		return b;
	}

	@Override
	/**
	 * 如果此 set 由于调用而发生更改，则返回 true
	 */
	public boolean addAll(Collection<? extends E> c) {
		boolean b = false;
		if (c != null && !c.isEmpty()) {
			Iterator<? extends E> iter = c.iterator();
			while (iter.hasNext()) {
				E e = (E) iter.next();
				if (add(e)) {
					b = true;
				}
			}
		}
		return b;
	}

	@Override
	/**
	 * 仅保留 set 中那些包含在指定 collection 中的元素（可选操作）,未实现
	 */
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("成本高，未实现");
	}

	@Override
	/**
	 * 移除 set 中那些包含在指定 collection 中的元素（可选操作）,未实现
	 */
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("成本高，未实现");
	}

	@Override
	public void clear() {
		getRedisDAO().del(nameKey);
	}

	@Override
	public String toString() {
		return getSet().toString();
	}
}
