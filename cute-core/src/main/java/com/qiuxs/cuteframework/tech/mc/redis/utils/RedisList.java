package com.qiuxs.cuteframework.tech.mc.redis.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.qiuxs.cuteframework.core.basic.utils.io.SerializeUtil;
import com.qiuxs.cuteframework.tech.mc.redis.RedisFacade;

/**
 * Redis命名List
 * 
 * @author JinXinhua 2016-05-26
 *
 * @param <E>
 */
public class RedisList<E> implements List<E> {

	private RedisFacade redisFacade;

	private String nameKey;
	private byte[] namekeyBytes;

	public RedisList(String name) {
		this.nameKey = "list:" + name;
		this.namekeyBytes = this.nameKey.getBytes();
	}

	public RedisList(String name, RedisFacade redisDao) {
		this.nameKey = "list:" + name;
		this.namekeyBytes = this.nameKey.getBytes();
		this.redisFacade = redisDao;
	}

	/**
	 * 功能描述: 用于延迟初始化RedisDAO. lazy initialization holder class模式
	 */
	private static class DefaultRedisFadadeHolder {
		/** list用的比较少，用默认连接池 */
		static final RedisFacade REDIS_FACADE = new RedisFacade();
	}

	/**
	 * list用的比较少，用默认连接池
	 * 
	 * @return
	 */
	public RedisFacade getRedisFacade() {
		if (redisFacade == null) {
			redisFacade = DefaultRedisFadadeHolder.REDIS_FACADE;
		}
		return redisFacade;
	}

	//	public static void setRedisDAO(RedisDAO redisDAO) {
	//		RedisList.redisDAO = redisDAO;
	//	}

	@Override
	public int size() {
		Long l = getRedisFacade().llen(nameKey);
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
	/** 会在本地做个拷贝 */
	public boolean contains(Object o) {
		return getList().contains(o);
	}

	@Override
	/**
	 * 获取迭代器时，会在本地做个拷贝
	 */
	public Iterator<E> iterator() {
		return getList().iterator();
	}

	/** 在本地做个全拷贝 */
	private List<E> getList() {
		List<E> listRet = new ArrayList<E>();
		int size = size();
		if (size > 0) {
			List<byte[]> list = getRedisFacade().lrange(namekeyBytes, 0, size - 1);
			Iterator<byte[]> iter = list.iterator();
			while (iter.hasNext()) {
				E e = SerializeUtil.unserialize(iter.next());
				listRet.add(e);
			}
		}
		return listRet;
	}

	@Override
	public Object[] toArray() {
		Object[] a = null;
		List<E> list = getList();

		int size = list.size();
		if (list.size() > 0) {
			int i = 0;
			a = new Object[size];
			Iterator<E> iter = list.iterator();
			while (iter.hasNext()) {
				a[i] = iter.next();
				i++;
			}
		}
		return a;
	}

	@Override
	/**
	 * 这个方法不理解要做什么，未实现
	 */
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException("成本高，未实现");
	}

	@Override
	/**
	 * 向列表的尾部添加指定的元素
	 * @return 由于调用而发生更改，则返回 true
	 */
	public boolean add(E e) {
		byte[] eBytes = SerializeUtil.serialize(e);
		int size = size();
		Long l = getRedisFacade().rpush(namekeyBytes, eBytes);
		boolean b = false;
		if (l.intValue() > size) {
			b = true;
		}
		return b;
	}

	@Override
	/**
	 * 从此列表中移除第一次出现的指定元素（如果存在）,如果列表包含指定的元素，则返回 true
	 */
	public boolean remove(Object o) {
		byte[] oBytes = SerializeUtil.serialize(o);
		Long l = getRedisFacade().lrem(namekeyBytes, 1, oBytes);
		boolean b = false;
		if (l > 0) {
			b = true;
		}
		return b;
	}

	@Override
	/** 会在本地做个全拷贝 */
	public boolean containsAll(Collection<?> c) {
		return getList().containsAll(c);
	}

	@Override
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
	 * 将指定 collection 中的所有元素都插入到列表中的指定位置（可选操作）。代价高，未实现
	 */
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException("成本高，未实现");
	}

	@Override
	/**
	 * 从列表中移除指定 collection 中包含的其所有元素（可选操作）。代价高，未实现
	 */
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("成本高，未实现");
	}

	@Override
	/**
	 * 仅在列表中保留指定 collection 中所包含的元素（可选操作）。代价高，未实现
	 */
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("成本高，未实现");
	}

	@Override
	public void clear() {
		getRedisFacade().del(nameKey);
	}

	@Override
	public E get(int index) {
		byte[] eBytes = getRedisFacade().lindex(namekeyBytes, index);
		return SerializeUtil.unserialize(eBytes);
	}

	@Override
	/**
	 * 返回以前在指定位置的元素
	 */
	public E set(int index, E element) {
		E old = get(index);
		byte[] eBytes = SerializeUtil.serialize(element);
		getRedisFacade().lset(namekeyBytes, (long) index, eBytes);
		return old;
	}

	@Override
	/**
	 * 在列表的指定位置插入指定元素（可选操作）。将当前处于该位置的元素（如果有的话）和所有后续元素向右移动（在其索引中加 1）。
	 * <p>未实现
	 */
	public void add(int index, E element) {
		throw new UnsupportedOperationException("成本高，未实现");
	}

	@Override
	/**
	 * 移除列表中指定位置的元素（可选操作）。将所有的后续元素向左移动（将其索引减 1）。返回从列表中移除的元素。
	 * <p>成本高，未实现
	 */
	public E remove(int index) {
		throw new UnsupportedOperationException("成本高，未实现");
	}

	@Override
	/**
	 * 返回此列表中第一次出现的指定元素的索引；如果此列表不包含该元素，则返回 -1
	 * 
	 */
	public int indexOf(Object o) {
		throw new UnsupportedOperationException("成本高，未实现");
	}

	@Override
	/**
	 * 返回此列表中最后出现的指定元素的索引；如果列表不包含此元素，则返回 -1。
	 */
	public int lastIndexOf(Object o) {
		throw new UnsupportedOperationException("成本高，未实现");
	}

	@Override
	/**
	 * 返回此列表元素的列表迭代器（按适当顺序）
	 */
	public ListIterator<E> listIterator() {
		throw new UnsupportedOperationException("成本高，未实现");
	}

	@Override
	/**
	 * 返回列表中元素的列表迭代器（按适当顺序），从列表的指定位置开始
	 */
	public ListIterator<E> listIterator(int index) {
		throw new UnsupportedOperationException("成本高，未实现");
	}

	@Override
	/**
	 * 返回列表中指定的 fromIndex（包括 ）和 toIndex（不包括）之间的部分视图
	 */
	public List<E> subList(int fromIndex, int toIndex) {
		List<byte[]> list = getRedisFacade().lrange(namekeyBytes, fromIndex, toIndex - 1);
		List<E> listRet = null;
		if (list != null && list.size() > 0) {
			listRet = new ArrayList<E>();
			Iterator<byte[]> iter = list.iterator();
			while (iter.hasNext()) {
				E e = SerializeUtil.unserialize(iter.next());
				listRet.add(e);
			}
		}
		return listRet;
	}

	@Override
	public String toString() {
		return getList().toString();
	}

}
