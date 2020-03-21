package com.qiuxs.cuteframework.tech.mc.redis.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.qiuxs.cuteframework.core.basic.Constants;
import com.qiuxs.cuteframework.core.basic.utils.NumberUtils;
import com.qiuxs.cuteframework.core.basic.utils.io.SerializeUtil;
import com.qiuxs.cuteframework.core.persistent.redis.RedisConfiguration;
import com.qiuxs.cuteframework.tech.mc.McConstants;
import com.qiuxs.cuteframework.tech.mc.redis.RedisFacade;

/**
 * 命名map,redis key=java map name, redis field=java map key
 * 格式：<String(redisKey), [<K(mapKey), V(mapValue)>,...]>
 * 
 * 推荐用法：V(mapValue)代表一个对象
 * 
 * @author JinXinhua 2016-05-25
 *
 * @param <K>
 * @param <V>
 */
public class RedisMap<K, V> implements IRedisMap<K, V> {

	private RedisFacade redisFacade;
	private String nameKey;
	private byte[] namekeyBytes;

	/**
	 * redis键统一加map:
	 * 
	 * @param name
	 */
	public RedisMap(String name) {
		this.nameKey = "map:" + name;
		this.namekeyBytes = this.nameKey.getBytes();
	}

	/**
	 * 指定数据库索引
	 * @param name
	 * @param dbIndex
	 * 2019年6月25日 下午10:05:20
	 * @author qiuxs
	 */
	public RedisMap(String name, int dbIndex) {
		this(name);
		this.redisFacade = new RedisFacade(RedisConfiguration.DEFAUL_POOL, dbIndex);
	}

	/**
	 * 
	 * 指定RedisFacade
	 * 
	 * @param name
	 * @param redisFacade
	 */
	public RedisMap(String name, RedisFacade redisFacade) {
		this(name);
		this.redisFacade = redisFacade;
	}

	/**
	 * 功能描述: 用于延迟初始化RedisFacade. lazy initialization holder class模式
	 */
	private static class DefaultRedisFacadeHolder {
		static final RedisFacade REDIS_DAO = new RedisFacade();
	}

	/**
	 * 
	 * 初始化时未指定dao，则使用默认的dao
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
		Long l = getRedisFacade().hlen(namekeyBytes);
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
	public boolean containsKey(Object key) {
		byte[] keyFieldBytes = SerializeUtil.serialize(key);
		Boolean b = getRedisFacade().hexists(namekeyBytes, keyFieldBytes);
		return b.booleanValue();
	}

	/** 此方法开销很大，因为要先把所有值取回，慎用 */
	@Override
	public boolean containsValue(Object value) {
		Collection<V> c = values();
		boolean b = false;
		if (c != null) {
			b = c.contains(value);
		}
		return b;
	}

	@Override
	public V get(Object key) {
		byte[] keyFieldBytes = SerializeUtil.serialize(key);
		byte[] valBytes = getRedisFacade().hget(namekeyBytes, keyFieldBytes);
		return SerializeUtil.unserialize(valBytes);
	}

	/**
	 * 将指定的值与此映射中的指定键关联（可选操作）。如果此映射以前包含一个该键的映射关系，则用指定值替换旧值
	 * 
	 * @return 返回null,因为redis返回的是long，不是以前的值
	 */
	@Override
	public V put(K key, V val) {
		byte[] keyFieldBytes = SerializeUtil.serialize(key);
		byte[] oldData = getRedisFacade().hget(namekeyBytes, keyFieldBytes);
		byte[] valBytes = SerializeUtil.serialize(val);
		getRedisFacade().hset(namekeyBytes, keyFieldBytes, valBytes);
		if (oldData != null) {
			return SerializeUtil.unserial(oldData);
		}
		return null;
	}

	/**
	 * 返回null,因为redis返回的是long，不是以前的值
	 */
	@Override
	public V remove(Object key) {
		byte[] keyFieldBytes = SerializeUtil.serialize(key);
		byte[] oldData = getRedisFacade().hget(namekeyBytes, keyFieldBytes);
		getRedisFacade().hdel(namekeyBytes, keyFieldBytes);
		if (oldData != null) {
			return SerializeUtil.unserial(oldData);
		}
		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		if (m != null) {
			for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
				put(entry.getKey(), entry.getValue());
			}
		}
	}

	@Override
	public void clear() {
		getRedisFacade().del(nameKey);
	}

	@Override
	public Set<K> keySet() {
		Set<byte[]> set = getRedisFacade().hkeys(namekeyBytes);
		Set<K> ret = null;
		if (set != null) {
			ret = new HashSet<K>();
			Iterator<byte[]> iter = set.iterator();
			while (iter.hasNext()) {
				K k = SerializeUtil.unserialize(iter.next());
				ret.add(k);
			}
		}
		return ret;
	}

	@Override
	public Collection<V> values() {
		List<byte[]> set = getRedisFacade().hvals(namekeyBytes);
		Collection<V> ret = null;
		if (set != null) {
			ret = new ArrayList<V>();
			Iterator<byte[]> iter = set.iterator();
			while (iter.hasNext()) {
				V v = SerializeUtil.unserialize(iter.next());
				ret.add(v);
			}
		}
		return ret;
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		Set<Map.Entry<K, V>> set = null;

		Map<K, V> map = getMap();
		if (map != null) {
			set = map.entrySet();
		}

		return set;
	}

	private Map<K, V> getMap() {
		Map<K, V> map = null;
		Map<byte[], byte[]> map2 = getRedisFacade().hgetAll(this.namekeyBytes);
		if (map2 != null) {
			map = new HashMap<K, V>();
			for (Map.Entry<byte[], byte[]> entry : map2.entrySet()) {
				K k = SerializeUtil.unserialize(entry.getKey());
				V v = SerializeUtil.unserialize(entry.getValue());
				map.put(k, v);
			}
		}
		return map;
	}

	/**
	 * 一次读取多个keys的value值，以列表方式
	 * 
	 * @author laisf
	 * @param keys
	 * @return
	 */
	public List<V> getListByKeys(List<K> listKey) {
		List<byte[]> listValBytes = hmget(listKey);
		List<V> listResult = new ArrayList<V>();
		for (byte[] valBytes : listValBytes) {
			V result = SerializeUtil.unserialize(valBytes);
			listResult.add(result);
		}
		return listResult;
	}

	/**
	 * 一次读取多个keys的value值，以列表方式
	 * 
	 * @author laisf
	 * @param keys
	 * @return
	 */
	public Map<K, V> getMapByKeys(List<K> listKey) {
		List<byte[]> listValBytes = hmget(listKey);
		Map<K, V> mapResult = new HashMap<K, V>();
		for (int i = 0; i < listKey.size(); i++) {
			V result = SerializeUtil.unserialize(listValBytes.get(i));
			mapResult.put(listKey.get(i), result);
		}
		return mapResult;
	}

	/**
	 * 从redis中一次性读取多个key值的字节数组列表
	 * 
	 * @author laisf
	 * @param keys
	 * @return
	 */
	private List<byte[]> hmget(List<K> listKey) {
		byte[][] keysBytes = new byte[listKey.size()][];
		for (int i = 0; i < listKey.size(); i++) {
			keysBytes[i] = SerializeUtil.serialize(listKey.get(i));
		}
		List<byte[]> listValBytes = getRedisFacade().hmget(namekeyBytes, keysBytes);
		return listValBytes;
	}

	@Override
	public String toString() {
		String s = null;
		Map<K, V> map = getMap();
		if (map != null) {
			s = map.toString();
		}
		return s;
	}

	public String getNameKey() {
		return nameKey;
	}

	/**
	 * 重置失效时间
	 *  
	 * @author fengdg  
	 * @param id
	 * @param expireSecond
	 */
	public boolean expire(Integer expireSecond) {
		Long result = getRedisFacade().expire(namekeyBytes, expireSecond);
		if (NumberUtils.isEmpty(result)) {
			return false;
		} else if (result == Constants.TRUE) {
			return true;
		} else {//不会走到这
			return false;
		}
	}

	/**
	 * 重置默认失效时间
	 *  
	 * @author fengdg  
	 * @param id
	 */
	public boolean expire() {
		return expire(McConstants.DEFAULT_EXPIRE_TIME);
	}

}
