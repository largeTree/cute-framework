package com.hzecool.core.cache.mc;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.hzecool.core.cache.mc.logger.McLogger;
import com.hzecool.core.cache.mc.redis.RedisMap;
import com.hzecool.fdn.bean.Pair;
import com.hzecool.fdn.exception.utils.ExceptionUtil;

/**
 * 缓存map工具类,方便redis map的key/field/value的经典存储模式的数据存取
 * @see McMapHelper
 */

public class McMapHelper {

//	public  Logger logger = Logger.getLogger(McMapHelper.class);

	private String dbIndex;
	/**指定redis master name*/
    private String masterName;
	
	public McMapHelper(){
		
	}
	
	public McMapHelper(String masterName){
		this.masterName = masterName;
	}
	
	public McMapHelper(String dbIndex, String masterName){
		this.dbIndex = dbIndex;
		this.masterName = masterName;
	}
	
	/**
	 * 存放<redisKey, McMap>缓存的map
	 */
	private volatile Map<String, Map<?, ?>> rkMcMapMap = new HashMap<String, Map<?,  ?>>();
	
	/**
	 * 获取指定redisKey的McMap
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <K, V> Map<K, V> getMap(String redisKey) {
		return (Map<K, V>) getMapWithInit(redisKey);
//		Map<?, ?> cacheMap = getMapWithInit(redisKey);
//		if (cacheMap == null) {
//			return null;
//		} else {
//			return (Map<K, V>)cacheMap;
//		}
	}
	
	/**
	 * 判断指定redisKey的McMap是否包含key
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @param key
	 * @return
	 */
	public <K> boolean containsKey(String redisKey, K key) {
		Map<?, ?> cacheMap = getMapWithInit(redisKey);
		if (cacheMap == null) {
			return false;
		} else {
			return cacheMap.containsKey(key);
		}
	}

	/**
	 * 获取指定redisKey的McMap中键为key的值，额外指定值的class类型
	 * -适用于其他tomcat放入，当前tomcat获取的情形
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @param key
	 * @param valueClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <K, V>  V  get(String redisKey, K key, Class<V> valueClass) {
		Map<?, ?> cacheMap = getMapWithInit(redisKey);
		if (cacheMap != null) {
			V v = (V)cacheMap.get(key);
			if (v == null) {
				if (McLogger.logger.isDebugEnabled()) {
					McLogger.logger.debug(cacheMap.keySet());
				}
			}
			return v;
		} else if (key == null || valueClass == null) {
			return null;
		} else {
			cacheMap = getMapWithInit(redisKey, key.getClass(), valueClass);
			if (cacheMap != null) {
				V v = (V)cacheMap.get(key);
				if (v == null) {
					if (McLogger.logger.isDebugEnabled()) {
						McLogger.logger.debug(cacheMap.keySet());
					}
				}
				return v;
			}
			return null;
		}
	}
	

	/**
	 * 获取指定redisKey的McMap中键为key的值
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <K, V> V get(String redisKey, K key) {
		Map<?, ?> cacheMap = getMapWithInit(redisKey);
		if (cacheMap == null) {
			return null;
		} else {
			return (V)cacheMap.get(key);
		}
	}
	
	/**
	 * 同时指定keyClazz/valueClazz和key/value；
	 * 用于keyClazz/valueClazz是基类，但是key/value是子类的场景
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @param keyClazz
	 * @param valueClazz
	 * @param key
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public  <K, V> V put(String redisKey, Class<K> keyClazz, Class<V> valueClazz, K key, V value) {
		Map<?, ?> cacheMap = getMapWithInit(redisKey, keyClazz, valueClazz);
		V oldValue = null;
		if (cacheMap != null) {
			oldValue = (V)cacheMap.get(key);
		}
		putInner(redisKey, key, value);
		return oldValue;
	}

	/**
	 * 设置指定redisKey的McMap的键值，并返回原来的值
	 * -先获取然后更新
	 * 
	 * @param redisKey redis key
	 * @param key redis field
	 * @param value 
	 * 
	 * @return 旧值
	 */
	public  <K, V>  V put(String redisKey, K key, V value) {
		V oldValue = get(redisKey, key);
		putInner(redisKey, key, value);
		return oldValue;
	}

	/**
	 * 若指定redisKey的McMap的键key有值，则更新值
	 * -若key原来不存在，则不会设置值
	 * 
	 * @param redisKey redis key
	 * @param key redis field
	 * @param value 
	 * 
	 * @return 旧值
	 */
	public  <K, V>  V putEx(String redisKey, K key,
			V value) {
		V oldValue = get(redisKey, key);
		if (oldValue != null) {
			putInner(redisKey, key, value);
		}
		return oldValue;
	}

	/**
	 * 若指定redisKey的McMap的键key没有值，则设置值
	 * -若key原来存在，则不会更新值
	 * 
	 * @param redisKey redis key
	 * @param key redis field
	 * @param value 
	 * 
	 * @return 旧值
	 */
	public  <K, V>  V putNx(String redisKey, K key,
			V value) {
		V oldValue = get(redisKey, key);
		if (oldValue == null) {
			putInner(redisKey, key, value);
		}
		return oldValue;
	}
	
	/**
	 * 删除单元元素
	 *  
	 * @author laisf  
	 * @param redisKey
	 * @param key
	 */
	public  <K, V> void remove(String redisKey, K key) {
		Map<?, ?> cacheMap = getMapWithInit(redisKey);
		if (cacheMap != null) {
			cacheMap.remove(key);
		}
	}

	/**
	 * 设置Map整个的过期时间（30分钟）；到期后整个map自动删除。
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @return
	 */
	public boolean expire(String redisKey) {
		return expire(redisKey, McConstants.DEFAULT_EXPIRE_TIME);
	}
	
	/**
	 * 设置Map整个的过期时间（单位秒）；到期后整个map自动删除。
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @param expireSecond 秒
	 * @return
	 */
	public boolean expire(String redisKey, Integer expireSecond) {
		Map<?, ?> cacheMap = getMapWithInit(redisKey);
		if (cacheMap == null) {
			return false;
		} else {
			if (cacheMap instanceof RedisMap) {
				try {
					return ((RedisMap<?, ?>) cacheMap).expire(expireSecond);
				} catch (Exception e) {
					McLogger.logger.error("expire exception: " + e.getMessage(), e);
					return false;
				}
			}
			return false;
		}
	}
	
	/**
	 * 清除指定redisKey的McMap
	 * 
	 */
	public  void clear(String redisKey) {
//		Map<String, Object> redisMap = getCacheMapByKey(key);
		Map<?, ?> cacheMap = getMapWithInit(redisKey);
		if (cacheMap != null) {
			cacheMap.clear();
		}
	}
	
	
	/**
	 * 清除rkMcMapMap中所有McMap的值
	 * 
	 */
	public  void clear() {
		Set<String> mapids = rkMcMapMap.keySet();
		for (String mapid : mapids) {
			Map<?, ?> map = rkMcMapMap.get(mapid);
			if (map != null) {
				map.clear();
			}
		}
	}
	
	public  void clearLocal() {
		rkMcMapMap.clear();
	}
	
	public  void clearLocal(String redisKey) {
		rkMcMapMap.remove(redisKey);
	}

	/**
	 * 设置指定redisKey的McMap的键值
	 * -注意：若是第一次设置，底层键值类型就是传入键值的类型。
	 * 
	 * @param redisKey redis key
	 * @param key redis field
	 * @param value 
	 */
	private  <K, V>  void putInner(String redisKey, K key, V value) {
		if (value != null) {
			@SuppressWarnings("unchecked")
			Map<K, V> cacheMap = getMapWithInit(redisKey, (Class<K>) key.getClass(), (Class<V>) value.getClass());
			cacheMap.put(key, value);
		}
	}
	
	/**
	 * 获取指定redisKey的McMap，本地不存在则连接到Redis
	 * -通过mcKeyValTypeMap获取键值类型
	 */
	private  Map<?, ?> getMapWithInit(String redisKey) {
//		Map<?, ?> cacheMap =  rkMcMapMap.get(redisKey);
//		if (cacheMap == null) {
//			if (McLogger.logger.isDebugEnabled()) {
//				McLogger.logger.debug("redisKey = " + redisKey + "; localMcMap = " + rkMcMapMap.keySet());
//			}
//			Pair<Class<?>, Class<?>> keyValType = getKeyValType(redisKey);
//			if (keyValType != null) {
////				McLogger.logger.info("Init McMap: redisKey = " + redisKey);
//				cacheMap = initMcMap(redisKey, keyValType.getV1(), keyValType.getV2());
//			}
//		}
//		return cacheMap;
		return getMapWithInit(redisKey, null, null);
	}

	/**
	 * 获取指定redisKey的McMap，本地不存在则连接到Redis
	 * -参数指定键值类型
	 */
	@SuppressWarnings("unchecked")
	private <K, V> Map<K, V> getMapWithInit(String redisKey, Class<K> keyClazz, Class<V> valClazz) {
		Map<K, V> cacheMap = (Map<K, V>) rkMcMapMap.get(redisKey);
		if(cacheMap == null){
			if (McLogger.logger.isDebugEnabled()) {
				McLogger.logger.debug("redisKey = " + redisKey + "; rkMcMapMap = " + rkMcMapMap.keySet());
			}
			if (keyClazz == null || valClazz == null) {
				Pair<Class<?>, Class<?>> keyValType = getKeyValType(redisKey);
				if (keyValType != null) {
					keyClazz = (Class<K>) keyValType.getV1();
					valClazz = (Class<V>) keyValType.getV2();
				}
			}
			if (keyClazz == null || valClazz == null) {
				return null;
			}
			cacheMap = initMcMap(redisKey, keyClazz, valClazz);
		}
		return cacheMap;
	}

	/**
	 * 初始化McMap
	 * -建立与缓存服务器连接
	 * -同时存储McMap的键值类型
	 */
	@SuppressWarnings("unchecked")
	private <K, V> Map<K, V> initMcMap(String redisKey, Class<K> keyClazz, Class<V> valClazz) {
		Map<K, V> cacheMap;
		//增加同步块，避免两个进程同时初始化
		synchronized (rkMcMapMap) {
			//要增加此判断， 因为两个进程同时初始化的时候，第二个进程进来已经初始化好了，直接取就好了 
			if (!rkMcMapMap.containsKey(redisKey)) {
				cacheMap = getMcFactory().createMap(keyClazz, valClazz, 5, redisKey, false);//不同范围的Redis允许创建相同的key
				McLogger.logger.info("Create McMap: redisKey = " + redisKey + 
						", keyClazz = " + keyClazz.getName() + ", valClazz = " + valClazz.getName() + 
						", cacheMap.class = " + cacheMap.getClass().getName());
				rkMcMapMap.put(redisKey, cacheMap);
				if (McLogger.logger.isDebugEnabled()) {
					McLogger.logger.debug("cacheMap.keySet() = " + cacheMap.keySet());
				}
				setRkKeyValType(redisKey, keyClazz, valClazz);
			} else {
				cacheMap = (Map<K, V>) rkMcMapMap.get(redisKey);
			}
		}
		return cacheMap;
	}
	
	
	private McFactory getMcFactory(){
		//dbIndex==null时，就用mc_redis_pool_db配置的dbIndex
		//masterName==null时，就用 mc_redis_master_name配置的masterName
		return McFactory.getFactory(dbIndex, masterName);
	}
	
	
	/**********************************************键值类型存储***************************************************/
	/**
	 * 存储McMap的key和value的Class类型。
	 * -mcKeyValTypeMap和rkMcMapMap对应：键相同，mcKeyValTypeMap的值是Pair<?,?>，rkMcMapMap的值是Map<?,?>
	 */
	@SuppressWarnings("rawtypes")
	private volatile  Map<String, Pair> rkKeyValTypeMap = null; 
	private Object keyValueLock = new Object();
	/**
	 * 获取所有McMap的键值类型对的Map
	 *  
	 * @author fengdg  
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private  Map<String, Pair> getRkKeyValTypeMap() {
		if (rkKeyValTypeMap == null) {
			synchronized (keyValueLock) {
				if (rkKeyValTypeMap == null) {
					rkKeyValTypeMap =  getMcFactory().createMap(String.class, 
							Pair.class, 5, "key_value_type", false);//不同范围的Redis允许创建相同的key
					McLogger.logger.info("Create McMap: redisKey = key_value_type"  + 
							", keyClazz = String" + ", valClazz = Pair" + 
							", cacheMap.class = " + rkKeyValTypeMap.getClass().getName());
				}
			}
		}
		return rkKeyValTypeMap;
	}
	
	
	/**
	 * 设置指定redisKey的McMap的键值类型
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @param keyClazz
	 * @param valClazz
	 */
	private  void setRkKeyValType(String redisKey, Class<?> keyClazz, Class<?> valClazz){
		@SuppressWarnings("rawtypes")
		Map<String, Pair> clazzTypeMap = getRkKeyValTypeMap();
		if (clazzTypeMap != null) {
			clazzTypeMap.put(redisKey, new Pair<String, String>(keyClazz.getName(), valClazz.getName()));
		}
	}
	
	/**
	 * 获取指定redisKey的McMap的键值类型对
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @param must
	 * @return
	 */
	private  Pair<Class<?>, Class<?>> getKeyValTypeInner(String redisKey, boolean must){
		Pair<Class<?>, Class<?>> keyValType = null;
		@SuppressWarnings("rawtypes")
		Map<String, Pair> clazzTypeMap = getRkKeyValTypeMap();
		if (clazzTypeMap != null) {
			Pair<?, ?> keyValueType = clazzTypeMap.get(redisKey);
			if (keyValueType != null) {
				try {
					Class<?> keyClazz = Class.forName(keyValueType.getV1().toString());
					Class<?> valueClazz = Class.forName(keyValueType.getV2().toString());
					keyValType = new Pair<Class<?>, Class<?>>(keyClazz, valueClazz);
				} catch (ClassNotFoundException e) {
					McLogger.logger.error("ex:" + e.getMessage(), e);
				}
			}
		}
		if (must && keyValType == null) {
			ExceptionUtil.throwLogicalException("mc_map_key_val_type_not_exist", redisKey);
		}
		return keyValType;
	}
	
	/**
	 * 获取指定redisKey的McMap的键值类型对
	 * -不存在则返回null
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @return
	 */
	private  Pair<Class<?>, Class<?>> getKeyValType(String redisKey){
		return getKeyValTypeInner(redisKey, false);
	}
	
	/**
	 * 删除指定redisKey的键值类型
	 *  
	 * @author fengdg  
	 * @param redisKey
	 */
	public void removeKeyValType(String redisKey) {
		@SuppressWarnings("rawtypes")
		Map<String, Pair> clazzTypeMap = getRkKeyValTypeMap();
		if (clazzTypeMap != null) {
			clazzTypeMap.remove(redisKey);
		}
	}
	/**
	 * 获取指定redisKey的McMap的键值类型对
	 * -不存在则抛异常
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @return
	 */
//	private  Pair<Class<?>, Class<?>> getKeyValTypeMust(String redisKey){
//		return getKeyValTypeInner(redisKey, true);
//	}
}
