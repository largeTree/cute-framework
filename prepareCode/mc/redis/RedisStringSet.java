package com.hzecool.core.cache.mc.redis;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.hzecool.tech.redis.RedisDAO;
import com.hzecool.tech.redis.RedisPool;

public class RedisStringSet implements Set<String> {
	private RedisDAO redisDAO;
	
	private String nameKey;

	public RedisStringSet(String name) {
		this.nameKey = "set:" + name;
	}
	
	public RedisStringSet(String name, RedisDAO redisDao) {
		this.nameKey = "set:" + name;
		this.redisDAO = redisDao;
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
		Boolean b = getRedisDAO().sismember(nameKey, o.toString());
		return b.booleanValue();
	}

	/**
	 * 开销很大，没有实现
	 */
	@Override
	public Iterator<String> iterator() {
		throw new UnsupportedOperationException("成本高，未实现");
	}

	@Override
	public Object[] toArray() {
		Set<String> set = getRedisDAO().smembers(nameKey);
		return set.toArray();
	}
	
	private Set<String> getSet(){
		return getRedisDAO().smembers(nameKey);
	}

	/**
	 * 这个方法不理解要做什么，未实现
	 */
	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException("成本高，未实现");
	}

	@Override
	public boolean add(String e) {
		Long l = getRedisDAO().sadd(nameKey, e);
		if (l > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean remove(Object o) {
		Long l = getRedisDAO().srem(nameKey, o.toString());
		if (l > 0) {
			return true;
		}
		return false;
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
	
	/**
	 * 添加c中所有元素；只要一个添加成功，就返回true
	 * @see java.util.Set#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends String> c) {
		String[] cArray = c.toArray(new String[0]);
		Long l = getRedisDAO().sadd(nameKey, cArray);
		if (l >= 0) {
			return true;
		}
		return false;
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
