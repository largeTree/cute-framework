package com.hzecool.core.cache.mc.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hzecool.core.cache.mc.McConstants;
import com.hzecool.fdn.Constant;
import com.hzecool.fdn.utils.NumberUtils;
import com.hzecool.fdn.utils.StringUtils;
import com.hzecool.fdn.utils.io.SerializeUtil;
import com.hzecool.tech.redis.RedisDAO;

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


	private RedisDAO redisDAO;
	String nameKey;
	byte[] namekeyBytes;

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
	 * 
	 * 指定RedisDAO
	 * 
	 * @param name
	 * @param redisDao
	 */
	public RedisMap(String name, RedisDAO redisDao) {
		this(name);
		this.redisDAO = redisDao;
	}
	
	/**
	 *指定master的redisMap
	 *@author laisf 
	 * @param name
	 */
	public RedisMap(String name, String masterName) {
		this(masterName, null, masterName);
	}

	public RedisMap(String name, String dbIndex, String masterName) {
		this(name);
		//dbIndex和masterName同时为null时，就用mc_redis_pool_db和mc_redis_master_name配置的dbIndex和masterName
		if(StringUtils.isNotEmpty(dbIndex) || StringUtils.isNotEmpty(masterName)){
			this.redisDAO = new RedisDAO("map", dbIndex, masterName);
		}
	}

	/**
	 * 功能描述: 用于延迟初始化RedisDAO. lazy initialization holder class模式
	 */
	private static class DefaultRedisDAOHolder {
		/** map用的比较多，用自己的连接池 */
		//默认的使用mc_redis_pool_db配置的dbIndex和mc_redis_master_name配置的masterName
		static final RedisDAO REDIS_DAO = new RedisDAO("map");
	}

	/**
	 * 
	 * 初始化时未指定dao，则使用默认的dao
	 * 
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
		Long l = getRedisDAO().hlen(namekeyBytes);
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
		Boolean b = getRedisDAO().hexists(namekeyBytes, keyFieldBytes);
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
		byte[] valBytes = getRedisDAO().hget(namekeyBytes, keyFieldBytes);
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
		byte[] valBytes = SerializeUtil.serialize(val);
		getRedisDAO().hset(namekeyBytes, keyFieldBytes, valBytes);
		return null;
	}

	/**
	 * 返回null,因为redis返回的是long，不是以前的值
	 */
	@Override
	public V remove(Object key) {
		byte[] keyFieldBytes = SerializeUtil.serialize(key);
		getRedisDAO().hdel(namekeyBytes, keyFieldBytes);
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
		getRedisDAO().del(nameKey);
	}

	@Override
	public Set<K> keySet() {
		Set<byte[]> set = getRedisDAO().hkeys(namekeyBytes);
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
		List<byte[]> set = getRedisDAO().hvals(namekeyBytes);
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
		Map<byte[], byte[]> map2 = getRedisDAO().hgetAll(this.namekeyBytes);
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
		List<byte[]> listValBytes = getRedisDAO().hmget(namekeyBytes, keysBytes);
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
		Long result = getRedisDAO().expire(namekeyBytes, expireSecond);
		if (NumberUtils.isEmpty(result)) {
			return false;
		} else if (result == Constant.TRUE) {
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
