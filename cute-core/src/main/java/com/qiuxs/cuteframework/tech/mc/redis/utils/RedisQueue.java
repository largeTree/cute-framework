package com.qiuxs.cuteframework.tech.mc.redis.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.qiuxs.cuteframework.core.basic.utils.io.SerializeUtil;
import com.qiuxs.cuteframework.tech.mc.redis.RedisFacade;

/**
 * Redis命名队列，FIFO（先进先出）,不接受 null 元素
 * 
 * @author JinXinhua 2016-05-28
 *
 */
public class RedisQueue<E> implements BlockingQueue<E> {
	private RedisFacade redisFacade;

	private String nameKey;
	private byte[] namekeyBytes;

	public RedisQueue(String name) {
		this.nameKey = "queue:" + name;
		this.namekeyBytes = this.nameKey.getBytes();
	}

	/**
	 * 功能描述: 用于延迟初始化RedisFacade. lazy initialization holder class模式
	 */
	private static class DefaultRedisFacadeHolder {
		/** queue用的比较少，用默认连接池 */
		static final RedisFacade REDIS_DAO = new RedisFacade();
	}

	/**
	 * queue用的比较少，用默认连接池
	 * 
	 * @return
	 */
	public RedisFacade getRedisFacade() {
		if (redisFacade == null) {
			redisFacade = DefaultRedisFacadeHolder.REDIS_DAO;
		}
		return redisFacade;
	}

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
	public boolean contains(Object o) {
		throw new UnsupportedOperationException("成本高，未实现");
	}

	@Override
	public Iterator<E> iterator() {
		throw new UnsupportedOperationException("成本高，未实现");
	}

	@Override
	public Object[] toArray() {
		Object[] a = null;
		int size = size();
		if (size > 0) {
			a = new Object[size];
			List<byte[]> list = getRedisFacade().lrange(namekeyBytes, 0, size - 1);
			int i = 0;
			Iterator<byte[]> iter = list.iterator();
			while (iter.hasNext()) {
				Object o = SerializeUtil.unserialize(iter.next());
				a[i] = o;
				i++;
			}
		}
		return a;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException("成本高，未实现");
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
	public boolean containsAll(Collection<?> c) {
		throw new UnsupportedOperationException("成本高，未实现");
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
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("成本高，未实现");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("成本高，未实现");
	}

	@Override
	public void clear() {
		getRedisFacade().del(nameKey);
	}

	@Override
	/** 将指定元素插入此队列最左边 */
	public boolean add(E e) {
		byte[] eBytes = SerializeUtil.serialize(e);
		int size = size();
		Long l = getRedisFacade().lpush(namekeyBytes, eBytes);
		boolean b = false;
		if (l.intValue() > size) {
			b = true;
		}
		return b;
	}

	@Override
	/** 同add */
	public boolean offer(E e) {
		return add(e);
	}

	@Override
	/**
	 * 同poll
	 */
	public E remove() {
		return poll();
	}

	/**
	 * 获取并移除此队列的最右边元素，如果此队列为空，则返回 null。
	 */
	@Override
	public E poll() {
		E e = null;
		byte[] eBytes = getRedisFacade().rpop(namekeyBytes);
		if (eBytes != null) {
			e = SerializeUtil.unserialize(eBytes);
		}
		return e;
	}

	@Override
	/**
	 * 获取但不移除此队列的最右边元素,队列为空时将抛出一个异常。未实现
	 */
	public E element() {
		E e = peek();
		if (e == null) {
			throw new NoSuchElementException();
		}
		return e;
	}

	@Override
	/**
	 * 获取但不移除此队列的最右边元素,队列为空时返回null。未实现
	 */
	public E peek() {
		E e = null;
		byte[] eBytes = getRedisFacade().lindex(namekeyBytes, -1);
		if (eBytes != null) {
			e = SerializeUtil.unserialize(eBytes);
		}
		return e;
	}

	@Override
	/** 同add */
	public void put(E e) throws InterruptedException {
		add(e);
	}

	@Override
	public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
		throw new UnsupportedOperationException("成本高，未实现");
	}

	@Override
	/**
	 * 获取并移除此队列的头部，在元素变得可用之前一直等待（如果有必要）.未实现
	 */
	public E take() throws InterruptedException {
		throw new UnsupportedOperationException("成本高，未实现");
	}

	@Override
	public E poll(long timeout, TimeUnit unit) throws InterruptedException {
		throw new UnsupportedOperationException("成本高，未实现");
	}

	@Override
	/**
	 * 返回在无阻塞的理想情况下（不存在内存或资源约束）此队列能接受的附加元素数量；如果没有内部限制，则返回 Integer.MAX_VALUE。
	 */
	public int remainingCapacity() {
		return Integer.MAX_VALUE;
	}

	@Override
	public int drainTo(Collection<? super E> c) {
		throw new UnsupportedOperationException("成本高，未实现");
	}

	@Override
	public int drainTo(Collection<? super E> c, int maxElements) {
		throw new UnsupportedOperationException("成本高，未实现");
	}

}
