package com.hzecool.core.cache.mc;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.hzecool.core.cache.mc.McFactory.McType;

public class McCollectionUtils {
	/**<RedisMasterDatabase, McCollectionHelper>*/
	private static Map<String, McCollectionHelper> rmdHelperMap = new ConcurrentHashMap<String, McCollectionHelper>();
	
	/**
	 * 获取Helper
	 *  
	 * @author fengdg  
	 * @param dbIndex
	 * @param masterName
	 * @return
	 */
	private static McCollectionHelper getHelper(String dbIndex, String masterName) {
		String key = McUtils.getCacheKey(dbIndex, masterName);
		McCollectionHelper helper = rmdHelperMap.get(key);
		if (helper == null) {
			helper = new McCollectionHelper(dbIndex, masterName);
			rmdHelperMap.put(key, helper);
		}
		return helper;
	}
	
	/**
	 * 获取redisKey对应的McSet
	 *  
	 * @author fengdg  
	 * @param dbIndex
	 * @param masterName
	 * @param redisKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <E> Set<E> set(String dbIndex, String masterName, String redisKey) {
		return (Set<E>) getHelper(dbIndex, masterName).getCollection(redisKey, McType.set);
	}
	
	/**
	 * 获取redisKey对应的McList
	 *  
	 * @author fengdg  
	 * @param dbIndex
	 * @param masterName
	 * @param redisKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <E> List<E> list(String dbIndex, String masterName, String redisKey) {
		return (List<E>) getHelper(dbIndex, masterName).getCollection(redisKey, McType.list);
	}
	
	/**
	 * redisKey对应的McSet是否包含value
	 *  
	 * @author fengdg  
	 * @param dbIndex
	 * @param masterName
	 * @param redisKey
	 * @param value
	 * @return
	 */
	private static <E> boolean setContains(String dbIndex, String masterName, String redisKey, E value) {
		return getHelper(dbIndex, masterName).contains(redisKey, value, McType.set);
	}
	
	/**
	 * redisKey对应的McList是否包含value
	 *  
	 * @author fengdg  
	 * @param dbIndex
	 * @param masterName
	 * @param redisKey
	 * @param value
	 * @return
	 */
	private static <E> boolean listContains(String dbIndex, String masterName, String redisKey, E value) {
		return getHelper(dbIndex, masterName).contains(redisKey, value, McType.list);
	}
	
	/**
	 * 添加value到redisKey对应的McSet
	 *  
	 * @author fengdg  
	 * @param dbIndex
	 * @param masterName
	 * @param redisKey
	 * @param value
	 */
	private static <E> void setAdd(String dbIndex, String masterName, String redisKey, E value) {
		getHelper(dbIndex, masterName).add(redisKey, value, McType.set);
	}
	
	/**
	 * 添加value到redisKey对应的McList
	 *  
	 * @author fengdg  
	 * @param dbIndex
	 * @param masterName
	 * @param redisKey
	 * @param value
	 */
	private static <E> void listAdd(String dbIndex, String masterName, String redisKey, E value) {
		getHelper(dbIndex, masterName).add(redisKey, value, McType.list);
	}

	/**
	 * 删除Set的单个元素
	 *  
	 * @author fengdg  
	 * @param dbIndex
	 * @param masterName
	 * @param redisKey
	 * @param value
	 */
	private static <E> void setRemove(String dbIndex, String masterName, String redisKey, E value) {
		getHelper(dbIndex, masterName).remove(redisKey, value, McType.set);
	}
	
	/**
	 * 删除List的单个元素
	 *  
	 * @author fengdg  
	 * @param dbIndex
	 * @param masterName
	 * @param redisKey
	 * @param value
	 */
	private static <E> void listRemove(String dbIndex, String masterName, String redisKey, E value) {
		getHelper(dbIndex, masterName).remove(redisKey, value, McType.list);
	}
	
	/**
	 * 清空指定redisKey的McSet
	 *  
	 * @author fengdg  
	 * @param dbIndex
	 * @param masterName
	 * @param redisKey
	 */
	private static void setClear(String dbIndex, String masterName, String redisKey) {
		getHelper(dbIndex, masterName).clear(redisKey, McType.set);
	}
	
	/**
	 * 清空指定redisKey的McList
	 *  
	 * @author fengdg  
	 * @param dbIndex
	 * @param masterName
	 * @param redisKey
	 */
	private static void listClear(String dbIndex, String masterName, String redisKey) {
		getHelper(dbIndex, masterName).clear(redisKey, McType.list);
	}

	/**
	 * 清空本地缓存的所有McCollection
	 *  
	 * @author fengdg  
	 * @param dbIndex
	 * @param masterName
	 */
	private static void clear(String dbIndex, String masterName) {
		getHelper(dbIndex, masterName).clear();
	}

	/**
	 * 清空本地的缓存Map，不清空底层的McCollection
	 *  
	 * @author fengdg  
	 * @param dbIndex
	 * @param masterName
	 */
	private static void clearLocal(String dbIndex, String masterName) {
		getHelper(dbIndex, masterName).clearLocal();
	}

	/*************************************应用集群内***********************************/
	/**
	 * 获取redisKey对应的McSet
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @return
	 */
	public static <E> Set<E> setInAppCluster(String redisKey) {
		return set(McConstants.getAppClusterDatabase(), McConstants.getAppClusterMasterName(), redisKey);
	}
	
	/**
	 * 获取redisKey对应的McList
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @return
	 */
	public static <E> List<E> listInAppCluster(String redisKey) {
		return list(McConstants.getAppClusterDatabase(), McConstants.getAppClusterMasterName(), redisKey);
	}
	
	/**
	 * redisKey对应的McSet是否包含value
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @param value
	 * @return
	 */
	public static <E> boolean setContainsInAppCluster(String redisKey, E value) {
		return setContains(McConstants.getAppClusterDatabase(), McConstants.getAppClusterMasterName(), redisKey, value);
	}
	
	/**
	 * redisKey对应的McList是否包含value
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @param value
	 * @return
	 */
	public static <E> boolean listContainsInAppCluster(String redisKey, E value) {
		return listContains(McConstants.getAppClusterDatabase(), McConstants.getAppClusterMasterName(), redisKey, value);
	}
	
	/**
	 * 添加value到redisKey对应的McSet
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @param value
	 */
	public static <E> void setAddInAppCluster(String redisKey, E value) {
		setAdd(McConstants.getAppClusterDatabase(), McConstants.getAppClusterMasterName(), redisKey, value);
	}
	
	/**
	 * 添加value到redisKey对应的McSet
	 *  
	 * @author fengdg  
	 * @param dbIndex
	 * @param masterName
	 * @param redisKey
	 * @param value
	 */
	public static <E> void listAddInAppCluster(String redisKey, E value) {
		listAdd(McConstants.getAppClusterDatabase(), McConstants.getAppClusterMasterName(), redisKey, value);
	}

	/**
	 * 删除McSet的单个元素
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @param value
	 */
	public static <E> void setRemoveInAppCluster(String redisKey, E value) {
		setRemove(McConstants.getAppClusterDatabase(), McConstants.getAppClusterMasterName(), redisKey, value);
	}
	
	/**
	 * 删除McList的单个元素
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @param value
	 */
	public static <E> void listRemoveInAppCluster(String redisKey, E value) {
		listRemove(McConstants.getAppClusterDatabase(), McConstants.getAppClusterMasterName(), redisKey, value);
	}
	
	/**
	 * 清空指定redisKey的McSet
	 *  
	 * @author fengdg  
	 * @param redisKey
	 */
	public static void setClearInAppCluster(String redisKey) {
		setClear(McConstants.getAppClusterDatabase(), McConstants.getAppClusterMasterName(), redisKey);
	}
	
	/**
	 * 清空指定redisKey的McList
	 *  
	 * @author fengdg  
	 * @param redisKey
	 */
	public static void listClearInAppCluster(String redisKey) {
		listClear(McConstants.getAppClusterDatabase(), McConstants.getAppClusterMasterName(), redisKey);
	}
	
	/**
	 * 清空本地缓存的所有McCollection
	 *  
	 * @author fengdg  
	 */
	public static void clearInAppCluster() {
		clear(McConstants.getAppClusterDatabase(), McConstants.getAppClusterMasterName());
	}

	/**
	 * 清空本地的缓存Map，不清空底层的McCollection
	 *  
	 * @author fengdg  
	 */
	public static void clearLocalInAppCluster() {
		clearLocal(McConstants.getAppClusterDatabase(), McConstants.getAppClusterMasterName());
	}

	/*************************************子系统集群内*********************************/
	/**
	 * 获取redisKey对应的McSet
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @return
	 */
	public static <E> Set<E> set(String redisKey) {
		return set(null, null, redisKey);
	}
	
	/**
	 * 获取redisKey对应的McList
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @return
	 */
	public static <E> List<E> list(String redisKey) {
		return list(null, null, redisKey);
	}
	
	/**
	 * redisKey对应的McSet是否包含value
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @param value
	 * @return
	 */
	public static <E> boolean setContains(String redisKey, E value) {
		return setContains(null, null, redisKey, value);
	}
	
	/**
	 * redisKey对应的McList是否包含value
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @param value
	 * @return
	 */
	public static <E> boolean listContains(String redisKey, E value) {
		return listContains(null, null, redisKey, value);
	}
	
	/**
	 * 添加value到redisKey对应的McSet
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @param value
	 */
	public static <E> void setAdd(String redisKey, E value) {
		setAdd(null, null, redisKey, value);
	}
	
	/**
	 * 添加value到redisKey对应的McSet
	 *  
	 * @author fengdg  
	 * @param dbIndex
	 * @param masterName
	 * @param redisKey
	 * @param value
	 */
	public static <E> void listAdd(String redisKey, E value) {
		listAdd(null, null, redisKey, value);
	}

	/**
	 * 删除McSet的单个元素
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @param value
	 */
	public static <E> void setRemove(String redisKey, E value) {
		setRemove(null, null, redisKey, value);
	}
	
	/**
	 * 删除McList的单个元素
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @param value
	 */
	public static <E> void listRemove(String redisKey, E value) {
		listRemove(null, null, redisKey, value);
	}
	
	/**
	 * 清空指定redisKey的McSet
	 *  
	 * @author fengdg  
	 * @param redisKey
	 */
	public static void setClear(String redisKey) {
		setClear(null, null, redisKey);
	}
	
	/**
	 * 清空指定redisKey的McList
	 *  
	 * @author fengdg  
	 * @param redisKey
	 */
	public static void listClear(String redisKey) {
		listClear(null, null, redisKey);
	}
	
	/**
	 * 清空本地缓存的所有McCollection
	 *  
	 * @author fengdg  
	 */
	public static void clear() {
		clear(null, null);
	}

	/**
	 * 清空本地的缓存Map，不清空底层的McCollection
	 *  
	 * @author fengdg  
	 */
	public static void clearLocal() {
		clearLocal(null, null);
	}

}
