package com.hzecool.core.cache.mc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存map工具类,方便redis map的key/field/value的经典存储模式的数据存取
 * 
 * @author lsf
 *
 */

public class McMapUtil {

	private static Map<String, McMapHelper> rmdHelperMap = new ConcurrentHashMap<String, McMapHelper>();
	
	private static McMapHelper getHelper(String dbIndex, String masterName) {
		String key = McUtils.getCacheKey(dbIndex, masterName);
		McMapHelper helper = rmdHelperMap.get(key);
		if (helper == null) {
			helper = new McMapHelper(dbIndex, masterName);
			rmdHelperMap.put(key, helper);
		}
		return helper;
	}
	
//	/**
//	 * 缺省的helper，使用mc_redis_pool_db配置的dbIndex 和 mc_redis_master_name配置的masterName
//	 *  
//	 * @author fengdg  
//	 * @return
//	 */
//	private static McMapHelper getDefaultHelper() {
//		return getHelper(null, null);
//	}
//	
//	/**
//	 * 子系统集群内共享的Helper，使用mc_redis_pool_db配置的dbIndex 和 mc_redis_master_name配置的masterName
//	 *  
//	 * @author fengdg  
//	 * @return
//	 */
//	private static McMapHelper getSubSysClusterHelper() {
//		return getDefaultHelper();
//	}
//	
//
//	/**
//	 * 应用集群内共享的Helper，使用mc_redis_pool_sess_db配置的dbIndex 和 sess_redis_master_name配置的masterName
//	 *  
//	 * @author fengdg  
//	 * @return
//	 */
//	private static McMapHelper getAppClusterHelper() {
//		String dbIndex = EnvironmentHolder.getEnvParamWithDefault(McConstants.APP_CLUSTER_MASTER_NAME_KEY, McConstants.APP_CLUSTER_MASTER_NAME_VAL);
//		String masterName = EnvironmentHolder.getEnvParamWithDefault(McConstants.APP_CLUSTER_DATABASE_KEY, McConstants.APP_CLUSTER_DATABASE_VAL);
//		return getHelper(dbIndex, masterName);
//	}

	/**
	 * 获取指定redisKey的McMap
	 * -不要用该方法进行Map的初始化
	 * 
	 * @author fengdg
	 * @param redisKey
	 * @return
	 */
	private static <K, V>  Map<K, V> getMap(String dbIndex, String masterName, String redisKey) {
		return getHelper(dbIndex, masterName).getMap(redisKey);
	}

	/**
	 * 判断指定redisKey的McMap是否包含key
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @param key
	 * @return
	 */
	private static <K> boolean containsKey(String dbIndex, String masterName, String redisKey, K key) {
		return getHelper(dbIndex, masterName).containsKey(redisKey, key);
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
	private static <K, V>  V  get(String dbIndex, String masterName, String redisKey, K key, Class<V> valueClass) {
		return getHelper(dbIndex, masterName).get(redisKey, key, valueClass);
	}
	
	/**
	 * 获取指定redisKey的McMap中键为key的值
	 * 
	 * @author fengdg
	 * @param redisKey
	 * @param key
	 * @return
	 */
	private static <K, V>  V  get(String dbIndex, String masterName, String redisKey, K key) {
		return getHelper(dbIndex, masterName).get(redisKey, key);
	}

	/**
	 * 设置指定redisKey的McMap的键值，并返回原来的值 -先获取然后更新
	 * 
	 * @param redisKey
	 *            redis key
	 * @param key
	 *            redis field
	 * @param value
	 * 
	 * @return 旧值
	 */
	private static <K, V> V put(String dbIndex, String masterName, String redisKey, K key, V value) {
		return getHelper(dbIndex, masterName).put(redisKey, key, value);
	}

	/**
	 * 设置指定redisKey的McMap的键值，并返回原来的值 -先获取然后更新
	 * 
	 * @param redisKey
	 *            redis key
	 * @param keyClazz
	 * @param valueClazz
	 * @param key
	 *            redis field
	 * @param value
	 * 
	 * @return 旧值
	 */
	private static <K, V> V put(String dbIndex, String masterName, String redisKey, 
			Class<K> keyClazz, Class<V> valueClazz, K key, V value) {
		return getHelper(dbIndex, masterName).put(redisKey, keyClazz, valueClazz, key, value);
	}

	/**
	 * 若指定redisKey的McMap的键key有值，则更新值 -若key原来不存在，则不会设置值
	 * 
	 * @param redisKey
	 *            redis key
	 * @param key
	 *            redis field
	 * @param value
	 * 
	 * @return 旧值
	 */
	private static <K, V> V putEx(String dbIndex, String masterName, String redisKey, K key,
			V value) {
		return getHelper(dbIndex, masterName).putEx(redisKey, key, value);
	}

	/**
	 * 若指定redisKey的McMap的键key没有值，则设置值 -若key原来存在，则不会更新值
	 * 
	 * @param redisKey
	 *            redis key
	 * @param key
	 *            redis field
	 * @param value
	 * 
	 * @return 旧值
	 */
	private static <K, V> V putNx(String dbIndex, String masterName, String redisKey, K key,
			V value) {
		return getHelper(dbIndex, masterName).putNx(redisKey, key, value);
	}

	/**
	 * 删除单元元素
	 * 
	 * @author laisf
	 * @param redisKey
	 * @param key
	 */
	private static <K, V> void remove(String dbIndex, String masterName, String redisKey, K key) {
		getHelper(dbIndex, masterName).remove(redisKey, key);
	}

	/**
	 * 设置Map整个的过期时间（单位秒）；到期后整个map自动删除。
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @param expireSecond 秒
	 * @return
	 */
	private static boolean expire(String dbIndex, String masterName, String redisKey, Integer expireSecond) {
		return getHelper(dbIndex, masterName).expire(redisKey, expireSecond);
	}

	/**
	 * 清除指定redisKey的缓存
	 * 
	 */
	private static void clear(String dbIndex, String masterName, String redisKey) {
		getHelper(dbIndex, masterName).clear(redisKey);
	}

	/**
	 * 清除所有缓存
	 * 
	 */
	private static void clear(String dbIndex, String masterName) {
		getHelper(dbIndex, masterName).clear();
	}

	/**
	 * 清除本地缓存
	 * 
	 */
	private static void clearLocal(String dbIndex, String masterName) {
		getHelper(dbIndex, masterName).clearLocal();
	}

	private static void clearLocal(String dbIndex, String masterName, String redisKey) {
		getHelper(dbIndex, masterName).clearLocal(redisKey);
	}

	/*************************************************应用集群内************************************************/
	/**
	 * 获取指定redisKey的McMap
	 * -不要用该方法进行Map的初始化
	 * 
	 * @author fengdg
	 * @param redisKey
	 * @return
	 */
	public static <K, V>  Map<K, V> getMapInAppCluster(String redisKey) {
		return getMap(McConstants.getAppClusterDatabase(), McConstants.getAppClusterMasterName(), redisKey);
	}

	/**
	 * 判断指定redisKey的McMap是否包含key
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @param key
	 * @return
	 */
	public static <K> boolean containsKeyInAppCluster(String redisKey, K key) {
		return containsKey(McConstants.getAppClusterDatabase(), McConstants.getAppClusterMasterName(), redisKey, key);
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
	public static <K, V>  V  getInAppCluster(String redisKey, K key, Class<V> valueClass) {
		return get(McConstants.getAppClusterDatabase(), McConstants.getAppClusterMasterName(), redisKey, key, valueClass);
	}
	
	/**
	 * 获取指定redisKey的McMap中键为key的值
	 * 
	 * @author fengdg
	 * @param redisKey
	 * @param key
	 * @return
	 */
	public static <K, V>  V  getInAppCluster(String redisKey, K key) {
		return get(McConstants.getAppClusterDatabase(), McConstants.getAppClusterMasterName(), redisKey, key);
	}

	/**
	 * 设置指定redisKey的McMap的键值，并返回原来的值 -先获取然后更新
	 * 
	 * @param redisKey
	 *            redis key
	 * @param key
	 *            redis field
	 * @param value
	 * 
	 * @return 旧值
	 */
	public static <K, V> V putInAppCluster(String redisKey, K key, V value) {
		return put(McConstants.getAppClusterDatabase(), McConstants.getAppClusterMasterName(), redisKey, key, value);
	}

	/**
	 * 若指定redisKey的McMap的键key有值，则更新值 -若key原来不存在，则不会设置值
	 * 
	 * @param redisKey
	 *            redis key
	 * @param key
	 *            redis field
	 * @param value
	 * 
	 * @return 旧值
	 */
	public static <K, V> V putExInAppCluster(String redisKey, K key,
			V value) {
		return putEx(McConstants.getAppClusterDatabase(), McConstants.getAppClusterMasterName(), redisKey, key, value);
	}

	/**
	 * 若指定redisKey的McMap的键key没有值，则设置值 -若key原来存在，则不会更新值
	 * 
	 * @param redisKey
	 *            redis key
	 * @param key
	 *            redis field
	 * @param value
	 * 
	 * @return 旧值
	 */
	public static <K, V> V putNxInAppCluster(String redisKey, K key,
			V value) {
		return putNx(McConstants.getAppClusterDatabase(), McConstants.getAppClusterMasterName(), redisKey, key, value);
	}

	/**
	 * 删除单元元素
	 * 
	 * @author laisf
	 * @param redisKey
	 * @param key
	 */
	public static <K, V> void removeInAppCluster(String redisKey, K key) {
		remove(McConstants.getAppClusterDatabase(), McConstants.getAppClusterMasterName(), redisKey, key);
	}

	/**
	 * 删除单元元素
	 * 
	 * @author laisf
	 * @param redisKey
	 * @param key
	 */
	public static <K, V> void expireInAppCluster(String redisKey) {
		expireInAppCluster(redisKey, McConstants.DEFAULT_EXPIRE_TIME);
	}

	/**
	 * 删除单元元素
	 * 
	 * @author laisf
	 * @param redisKey
	 * @param key
	 */
	public static <K, V> void expireInAppCluster(String redisKey, Integer expireSecond) {
		expire(McConstants.getAppClusterDatabase(), McConstants.getAppClusterMasterName(), redisKey, expireSecond);
	}

	/**
	 * 清除指定redisKey的缓存
	 * 
	 */
	public static void clearInAppCluster(String redisKey) {
		clear(McConstants.getAppClusterDatabase(), McConstants.getAppClusterMasterName(), redisKey);
	}

	/**
	 * 清除所有缓存
	 * 
	 */
	public static void clearInAppCluster() {
		clear(McConstants.getAppClusterDatabase(), McConstants.getAppClusterMasterName());
	}

	/**
	 * 清除本地缓存
	 * 
	 */
	public static void clearLocalInAppCluster() {
		clearLocal(McConstants.getAppClusterDatabase(), McConstants.getAppClusterMasterName());
	}

	/*************************************************子系统集群内************************************************/
	/**
	 * 获取指定redisKey的McMap
	 * -不要用该方法进行Map的初始化
	 * 
	 * @author fengdg
	 * @param redisKey
	 * @return
	 */
	public static <K, V>  Map<K, V> getMap(String redisKey) {
		return getMap(null, null, redisKey);
	}

	/**
	 * 判断指定redisKey的McMap是否包含key
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @param key
	 * @return
	 */
	public static <K> boolean containsKey(String redisKey, K key) {
		return containsKey(null, null, redisKey, key);
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
	public static <K, V>  V  get(String redisKey, K key, Class<V> valueClass) {
		return get(null, null, redisKey, key, valueClass);
	}
	
	/**
	 * 获取指定redisKey的McMap中键为key的值
	 * 
	 * @author fengdg
	 * @param redisKey
	 * @param key
	 * @return
	 */
	public static <K, V>  V  get(String redisKey, K key) {
		return get(null, null, redisKey, key);
	}

	/**
	 * 设置指定redisKey的McMap的键值，并返回原来的值 -先获取然后更新
	 * 
	 * @param redisKey
	 *            redis key
	 * @param key
	 *            redis field
	 * @param value
	 * 
	 * @return 旧值
	 */
	public static <K, V> V put(String redisKey, K key, V value) {
		return put(null, null, redisKey, key, value);
	}

	/**
	 * 若指定redisKey的McMap的键key有值，则更新值 -若key原来不存在，则不会设置值
	 * 
	 * @param redisKey
	 *            redis key
	 * @param key
	 *            redis field
	 * @param value
	 * 
	 * @return 旧值
	 */
	public static <K, V> V putEx(String redisKey, K key,
			V value) {
		return putEx(null, null, redisKey, key, value);
	}

	/**
	 * 若指定redisKey的McMap的键key没有值，则设置值 -若key原来存在，则不会更新值
	 * 
	 * @param redisKey
	 *            redis key
	 * @param key
	 *            redis field
	 * @param value
	 * 
	 * @return 旧值
	 */
	public static <K, V> V putNx(String redisKey, K key,
			V value) {
		return putNx(null, null, redisKey, key, value);
	}

	/**
	 * 设置指定redisKey的McMap的键值，并返回原来的值 -先获取然后更新
	 * 
	 * @param redisKey
	 *            redis key
	 * @param key
	 *            redis field
	 * @param value
	 * 
	 * @return 旧值
	 */
	public static <K, V> V put(String redisKey, Class<K> keyClazz, Class<V> valueClazz, K key, V value) {
		return put(null, null, redisKey, keyClazz, valueClazz, key, value);
	}


	/**
	 * 删除单元元素
	 * 
	 * @author laisf
	 * @param redisKey
	 * @param key
	 */
	public static <K, V> void expire(String redisKey) {
		expire(redisKey, McConstants.DEFAULT_EXPIRE_TIME);
	}

	/**
	 * 删除单元元素
	 * 
	 * @author laisf
	 * @param redisKey
	 * @param key
	 */
	public static <K, V> void expire(String redisKey, Integer expireSecond) {
		expire(null, null, redisKey, expireSecond);
	}

	/**
	 * 删除单元元素
	 * 
	 * @author laisf
	 * @param redisKey
	 * @param key
	 */
	public static <K, V> void remove(String redisKey, K key) {
		remove(null, null, redisKey, key);
	}

	/**
	 * 清除指定redisKey的缓存
	 * 
	 */
	public static void clear(String redisKey) {
		clear(null, null, redisKey);
	}

	/**
	 * 清除所有缓存
	 * 
	 */
	public static void clear() {
		clear(null, null);
	}

	/**
	 * 清除本地缓存
	 * 
	 */
	public static void clearLocal() {
		clearLocal(null, null);
	}
	
	public static void clearLocal(String redisKey) {
		clearLocal(null, null, redisKey);
	}

	/**
	 * 删除mymaster中redisKey的键值类型
	 *  
	 * @author fengdg  
	 * @param redisKey
	 */
	public static void removeKeyValType(String redisKey) {
		getHelper(null, null).removeKeyValType(redisKey);
	}

}
