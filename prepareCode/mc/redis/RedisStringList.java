package com.hzecool.core.cache.mc.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.hzecool.tech.redis.RedisDAO;
import com.hzecool.tech.redis.RedisPool;

public class RedisStringList implements List<String> {

	private RedisDAO redisDAO;

	private String nameKey;

	public RedisStringList(String name) {
		this.nameKey = "list:" + name;
	}
	
	public RedisStringList(String name, RedisDAO redisDao) {
		this.nameKey = "list:" + name;
		this.redisDAO = redisDao;
	}

	/**
	 * 功能描述: 用于延迟初始化RedisDAO. lazy initialization holder class模式
	 */
	private static class DefaultRedisDAOHolder {
		/** list用的比较少，用默认连接池 */
		static final RedisDAO REDIS_DAO = new RedisDAO(RedisPool.POOL_NAME_DEFAULT);
	}
	
	/**
	 * list用的比较少，用默认连接池
	 * 
	 * @return
	 */
	public RedisDAO getRedisDAO() {
		if (redisDAO == null) {
			redisDAO = DefaultRedisDAOHolder.REDIS_DAO;
		}
		return redisDAO;
	}

//	public static void setRedisDAO(RedisDAO redisDAO) {
//		RedisList.redisDAO = redisDAO;
//	}

	@Override
	public int size() {
		Long l = getRedisDAO().llen(nameKey);
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
	public Iterator<String> iterator() {		
		return getList().iterator();
	}
	
	private List<String> getList() {
		int size = size();
		return getRedisDAO().lrange(nameKey, 0, size - 1);
	}

	@Override
	public Object[] toArray() {
		List<String> list = getList();
		return list.toArray();
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
	public boolean add(String e) {
		int size = size();
		Long l = getRedisDAO().rpush(nameKey, e);
		if (l.intValue() > size) {
			return true;
		}
		return false;
	}

	@Override
	/**
	 * 从此列表中移除第一次出现的指定元素（如果存在）,如果列表包含指定的元素，则返回 true
	 */
	public boolean remove(Object o) {
		Long l = getRedisDAO().lrem(nameKey, 1, o.toString());
		if (l > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return getList().containsAll(c);
	}

	/**
	 * 添加c中所有元素；只要一个添加失败，就返回false
	 * @see java.util.List#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends String> c) {
		String[] cArray = c.toArray(new String[0]);
		int size = size();
		Long l = getRedisDAO().rpush(nameKey, cArray);
		if (l.intValue() > size) {
			return true;
		}
		return false;
	}

	@Override
	/**
	 * 将指定 collection 中的所有元素都插入到列表中的指定位置（可选操作）。代价高，未实现
	 */
	public boolean addAll(int index, Collection<? extends String> c) {
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
		getRedisDAO().del(nameKey);
	}

	@Override
	public String get(int index) {
		return getRedisDAO().lindex(nameKey, index);
	}

	/**
	 * 返回以前在指定位置的元素
	 */
	@Override
	public String set(int index, String element) {
		String old = get(index);
		getRedisDAO().lset(nameKey, (long) index, element);
		return old;
	}

	@Override
	/**
	 * 在列表的指定位置插入指定元素（可选操作）。将当前处于该位置的元素（如果有的话）和所有后续元素向右移动（在其索引中加 1）。
	 * <p>未实现
	 */
	public void add(int index, String element) {
		throw new UnsupportedOperationException("成本高，未实现");
	}

	@Override
	/**
	 * 移除列表中指定位置的元素（可选操作）。将所有的后续元素向左移动（将其索引减 1）。返回从列表中移除的元素。
	 * <p>成本高，未实现
	 */
	public String remove(int index) {
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
	public ListIterator<String> listIterator() {
		throw new UnsupportedOperationException("成本高，未实现");
	}

	@Override
	/**
	 * 返回列表中元素的列表迭代器（按适当顺序），从列表的指定位置开始
	 */
	public ListIterator<String> listIterator(int index) {
		throw new UnsupportedOperationException("成本高，未实现");
	}

	@Override
	/**
	 * 返回列表中指定的 fromIndex（包括 ）和 toIndex（不包括）之间的部分视图
	 */
	public List<String> subList(int fromIndex, int toIndex) {
		return getRedisDAO().lrange(nameKey, fromIndex, toIndex - 1);
	}

	@Override
	public String toString() {
		return getList().toString();
	}

	public static void main(String[] args) {
		RedisStringList list = new RedisStringList("tlist1");
		List<String> arrayList = new ArrayList<String>();
		arrayList.add("1");
		arrayList.add("2");
		System.out.println(list.addAll(arrayList));
	}
}
