package com.qiuxs.cuteframework.tech.mc.redis.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.qiuxs.cuteframework.tech.mc.redis.RedisFacade;

public class RedisStringSet implements Set<String> {
	private RedisFacade redisFacade;
	
	private String nameKey;

	public RedisStringSet(String name) {
		this.nameKey = "set:" + name;
	}
	
	public RedisStringSet(String name, RedisFacade RedisFacade) {
		this.nameKey = "set:" + name;
		this.redisFacade = RedisFacade;
	}
	
	/**
	 * 功能描述: 用于延迟初始化RedisFacade. lazy initialization holder class模式
	 */
	private static class DefaultRedisFacadeHolder {
		static final RedisFacade REDIS_FACADE = new RedisFacade();
	}
	
	/**
	 * 初始化时未指定dao，则使用默认的dao
	 * @return
	 */
	public RedisFacade getRedisFacade() {
		if (redisFacade == null) {
			redisFacade = DefaultRedisFacadeHolder.REDIS_FACADE;
		}
		return redisFacade;
	}

	@Override
	public int size() {
		Long l = getRedisFacade().scard(nameKey);
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
		Boolean b = getRedisFacade().sismember(nameKey, o.toString());
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
		Set<String> set = getRedisFacade().smembers(nameKey);
		return set.toArray();
	}
	
	private Set<String> getSet(){
		return getRedisFacade().smembers(nameKey);
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
		Long l = getRedisFacade().sadd(nameKey, e);
		if (l > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean remove(Object o) {
		Long l = getRedisFacade().srem(nameKey, o.toString());
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
		Long l = getRedisFacade().sadd(nameKey, cArray);
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
		getRedisFacade().del(nameKey);
	}

	@Override
	public String toString() {
		return getSet().toString();
	}
}
