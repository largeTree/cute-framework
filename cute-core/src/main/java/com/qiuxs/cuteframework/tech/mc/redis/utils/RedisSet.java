package com.qiuxs.cuteframework.tech.mc.redis.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.qiuxs.cuteframework.core.basic.utils.io.SerializeUtil;
import com.qiuxs.cuteframework.core.persistent.redis.RedisConfiguration;
import com.qiuxs.cuteframework.tech.mc.redis.RedisFacade;

/**
 * redis命名hashSet
 * 
 * @author JinXinhua 2016-05-26
 *
 */
public class RedisSet<E> implements Set<E> {
	private RedisFacade redisFacade;

	private String nameKey;
	private byte[] namekeyBytes;

	public RedisSet(String name) {
		this.nameKey = "set:" + name;
		this.namekeyBytes = this.nameKey.getBytes();
	}

	public RedisSet(String name, RedisFacade redisFacade) {
		this.nameKey = "set:" + name;
		this.namekeyBytes = this.nameKey.getBytes();
		this.redisFacade = redisFacade;
	}

	public RedisSet(String name, int dbIndex) {
		this(name);
		this.redisFacade = new RedisFacade(RedisConfiguration.DEFAUL_POOL, dbIndex);
	}

	/**
	 * 功能描述: 用于延迟初始化RedisFacade. lazy initialization holder class模式
	 */
	private static class DefaultRedisFacadeHolder {
		/** set用的比较少，用默认连接池 */
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
		byte[] oBytes = SerializeUtil.serialize(o);
		Boolean b = getRedisFacade().sismember(namekeyBytes, oBytes);
		return b.booleanValue();
	}

	/**
	 * 开销很大，没有实现
	 */
	@Override
	public Iterator<E> iterator() {
		return getSet().iterator();
	}

	@Override
	public Object[] toArray() {
		Set<byte[]> set = getRedisFacade().smembers(namekeyBytes);
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

	private Set<E> getSet() {
		Set<E> setRet = new HashSet<E>();
		Set<byte[]> set = getRedisFacade().smembers(namekeyBytes);
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
		Long l = getRedisFacade().sadd(namekeyBytes, eBytes);
		boolean b = false;
		if (l > 0) {
			b = true;
		}
		return b;
	}

	@Override
	public boolean remove(Object o) {
		byte[] eBytes = SerializeUtil.serialize(o);
		Long l = getRedisFacade().srem(namekeyBytes, eBytes);
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
		c.forEach(item -> remove(item));
		return true;
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
