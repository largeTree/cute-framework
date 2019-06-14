package com.hzecool.core.cache.mc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.hzecool.core.cache.mc.McFactory.McType;
import com.hzecool.core.cache.mc.logger.McLogger;
import com.hzecool.fdn.exception.utils.ExceptionUtil;

/**
 * 缓存map工具类,方便redis map的key/field/value的经典存储模式的数据存取
 * @see McCollectionHelper
 */

public class McCollectionHelper {

	private String dbIndex;
	/**指定redis master name*/
    private String masterName;
	
	public McCollectionHelper(){
		
	}
	
	public McCollectionHelper(String masterName){
		this.masterName = masterName;
	}
	
	public McCollectionHelper(String dbIndex, String masterName){
		this.dbIndex = dbIndex;
		this.masterName = masterName;
	}
	
	private McFactory getMcFactory(){
		//dbIndex==null时，就用mc_redis_pool_db配置的dbIndex
		//masterName==null时，就用 mc_redis_master_name配置的masterName
		return McFactory.getFactory(dbIndex, masterName);
	}
	
	/**
	 * 存放<redisKey, McCollection>缓存的map
	 */
	private volatile Map<String, Collection<?>> rkMcCollectionMap = new HashMap<String, Collection<?>>();
	
	/**
	 * 获取redisKey对应的McCollection
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> Collection<E> getCollection(String redisKey, McType mcType) {
		return (Collection<E>) getCollectionWithInit(redisKey, mcType);
	}

	/**
	 * 添加value到redisKey对应的McCollection
	 * -注意：若是第一次设置，底层值类型就是传入值的类型。
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @param value
	 * @param mcType	仅用于初始化
	 */
	public <E> void add(String redisKey, E value, McType mcType) {
		if (value != null) {
			@SuppressWarnings("unchecked")
			Collection<E> cacheMap = getCollectionWithInit(redisKey, (Class<E>) value.getClass(), mcType);
			cacheMap.add(value);
		}
	}

	/**
	 * 判断redisKey对应的McCollection是否包含value
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @param value
	 * @return
	 */
	public <E> boolean contains(String redisKey, E value, McType mcType) {
		Collection<?> cacheMap = getCollectionWithInit(redisKey, mcType);
		if (cacheMap == null) {
			return false;
		} else {
			return cacheMap.contains(value);
		}
	}

	/**
	 * 删除redisKey对应的McCollection中的value
	 *  
	 * @param redisKey
	 * @param value
	 */
	public  <E> void remove(String redisKey, E value, McType mcType) {
		@SuppressWarnings("unchecked")
		Collection<E> cacheMap = (Collection<E>) getCollectionWithInit(redisKey, mcType);
		if (cacheMap != null) {
			cacheMap.remove(value);
		}
	}

	
	/**
	 * 清空指定redisKey的McCollection
	 *  
	 * @author fengdg  
	 * @param redisKey
	 */
	public  void clear(String redisKey, McType mcType) {
		Collection<?> cacheMap = getCollectionWithInit(redisKey, mcType);
		if (cacheMap != null) {
			cacheMap.clear();
		}
		rkMcCollectionMap.remove(redisKey);
	}
	
	/**
	 * 清空本地缓存的所有McCollection
	 *  
	 * @author fengdg
	 */
	public  void clear() {
		Set<String> collectionIds = rkMcCollectionMap.keySet();
		for (String collectionId : collectionIds) {
			Collection<?> mcCollection = rkMcCollectionMap.get(collectionId);
			if (mcCollection != null) {
				mcCollection.clear();
			}
		}
		rkMcCollectionMap.clear();
	}
	
	/**
	 * 清空本地的缓存Map，不清空底层的McCollection
	 *  
	 * @author fengdg  
	 */
	public  void clearLocal() {
		rkMcCollectionMap.clear();
	}
	
//	/**
//	 * 获取redisKey对应的McCollection
//	 *  
//	 * @author fengdg  
//	 * @param redisKey
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	private <E> Collection<E> getMcCollectionByKey(String redisKey) {
//		return (Collection<E>) rkMcCollectionMap.get(redisKey);
//	}
	
	/**
	 * 获取指定redisKey的McCollection，本地不存在则连接到Redis
	 * -通过rkValTypeMap获取值类型
	 */
	private  Collection<?> getCollectionWithInit(String redisKey, McType mcType) {
		return getCollectionWithInit(redisKey, null, mcType);
	}

	/**
	 * 获取指定redisKey的McCollection，本地不存在则连接到Redis
	 * -参数指定键值类型
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @param valClazz
	 * @param mcType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <E> Collection<E> getCollectionWithInit(String redisKey, Class<E> valClazz, McType mcType) {
		Collection<E> mcCollection = (Collection<E>) rkMcCollectionMap.get(redisKey);
		if(mcCollection == null){
			if (McLogger.logger.isDebugEnabled()) {
				McLogger.logger.debug("redisKey = " + redisKey + "; rkMcCollectionMap = " + rkMcCollectionMap.keySet());
			}
			if (valClazz == null) {
				valClazz = (Class<E>) getValType(redisKey);
			}
			if (valClazz == null) {
				return null;
			}
			mcCollection = initMcCollection(redisKey, valClazz, mcType);
		}
		return mcCollection;
	}

	/**
	 * 初始化McCollection
	 * -建立与缓存服务器连接
	 * -同时存储McCollection的值类型
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @param valClazz
	 * @param mcType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <E> Collection<E> initMcCollection(String redisKey, Class<E> valClazz, McType mcType) {
		if (mcType != McType.list && mcType != McType.set) {
			return null;
		}
		Collection<E> mcCollection = null;
		//增加同步块，避免两个进程同时初始化
		synchronized (rkMcCollectionMap) {
			//要增加此判断， 因为两个进程同时初始化的时候，第二个进程进来已经初始化好了，直接取就好了 
			if (!rkMcCollectionMap.containsKey(redisKey)) {
				if (mcType == McType.list) {
					mcCollection = getMcFactory().createList(valClazz, redisKey, false);//不同范围的Redis允许创建相同的key
				} else if (mcType == McType.set) {
					mcCollection = getMcFactory().createSet(valClazz, redisKey, false);//不同范围的Redis允许创建相同的key
				}
				McLogger.logger.info("Create McSet: redisKey = " + redisKey + 
						", valClazz = " + valClazz.getName() + 
						", cacheMap.class = " + mcCollection.getClass().getName());
				rkMcCollectionMap.put(redisKey, mcCollection);
				putRkValType(redisKey, valClazz);
			} else {
				mcCollection = (Set<E>) rkMcCollectionMap.get(redisKey);
			}
		}
		return mcCollection;
	}
	
	/**********************************************键值类型存储***************************************************/
	/**
	 * 存储McCollection的<redisKey,值类型>。
	 */
	private volatile  Map<String, String> rkValTypeMap = null; 
	private Object valueLock = new Object();
	
	/**
	 * 获取所有McCollection的<redisKey,值类型>的Map
	 *  
	 * @author fengdg  
	 * @return
	 */
	private  Map<String, String> getRkValTypeMap() {
		if (rkValTypeMap == null) {
			synchronized (valueLock) {
				if (rkValTypeMap == null) {
					rkValTypeMap =  getMcFactory().createMap(String.class, 
							String.class, 5, "collection_value_type", false);//不同范围的Redis允许创建相同的key
					McLogger.logger.info("Create McMap: redisKey = collection_value_type"  + 
							", valClazz = String" + 
							", cacheMap.class = " + rkValTypeMap.getClass().getName());
				}
			}
		}
		return rkValTypeMap;
	}
	
	
	/**
	 * 设置指定redisKey的McMap的键值类型
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @param valClazz
	 */
	private  void putRkValType(String redisKey, Class<?> valClazz){
		Map<String, String> clazzTypeMap = getRkValTypeMap();
		if (clazzTypeMap != null) {
			clazzTypeMap.put(redisKey, valClazz.getName());
		}
	}
	
	/**
	 * 获取redisKey对应的McCollection的值类型
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @param must
	 * @return
	 */
	private  Class<?> getValTypeInner(String redisKey, boolean must){
		Map<String, String> clazzTypeMap = getRkValTypeMap();
		if (clazzTypeMap != null) {
			String ValueType = clazzTypeMap.get(redisKey);
			if (ValueType != null) {
				try {
					Class<?> valueClazz = Class.forName(ValueType);
					return valueClazz;
				} catch (ClassNotFoundException e) {
					McLogger.logger.error("ex:" + e.getMessage(), e);
				}
			}
		}
		if (must) {
			ExceptionUtil.throwLogicalException("mc_collection_val_type_not_exist", redisKey);
		}
		return null;
	}
	
	/**
	 * 获取指定redisKey的McCollection的值类型
	 * -不存在则返回null
	 *  
	 * @author fengdg  
	 * @param redisKey
	 * @return
	 */
	private  Class<?> getValType(String redisKey){
		return getValTypeInner(redisKey, false);
	}
}
