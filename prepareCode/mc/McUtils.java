package com.hzecool.core.cache.mc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hzecool.core.cache.mc.redis.RedisMap;
import com.hzecool.fdn.Constant;
import com.hzecool.fdn.utils.StringUtils;

/**
 * 
 * 功能描述: 缓存工具类，处理一些集合对象不支持的功能 
 * 新增日期: 2017年1月23日 上午10:32:27 <br/>  
 *  
 * @author laisf   
 * @version 1.0.0
 */
public class McUtils {
	/**
	 * 获取本地缓存map的key
	 *  
	 * @author fengdg  
	 * @param dbIndex
	 * @param masterName
	 * @return
	 */
	public static String getCacheKey(String dbIndex, String masterName) {
		if (StringUtils.isEmpty(dbIndex) && StringUtils.isEmpty(masterName)) {
			return "";
		} else if (StringUtils.isEmpty(dbIndex)) {
			return masterName;
		} else if (StringUtils.isEmpty(masterName)) {
			return dbIndex;
		} else {
			return dbIndex + Constant.SEPARATOR_HYPHEN + masterName;
		}
	}


	/****************************************************************/
	/**
	 *  一次传入多个keys,返回这些keys对的key-value，
	 *  如果是redisMap，这样访问可以减少访问次数
	 * @author laisf  
	 * @param mapCache
	 * @param listKey
	 * @return
	 */
	public static <K, V> Map<K, V>  getMapByKeys(Map<K, V> mapCache,
			List<K> listKey){
		Map<K, V> mapResult = null;
		if(mapCache instanceof RedisMap){
			RedisMap<K, V> redisCacheMap = (RedisMap<K, V>) mapCache;
			mapResult = redisCacheMap.getMapByKeys(listKey);
		}else{
			//本地map处理方式
			mapResult = new HashMap<K, V>();
			for(K key : listKey){
				mapResult.put(key, mapCache.get(key));
			}
		}
		return mapResult;
	}
	
	/**
	 *  一次传入多个keys,返回这些keys对的key-value，
	 *  如果是redisMap，这样访问可以减少访问次数
	 * @author laisf  
	 * @param mapCache
	 * @param listKey
	 * @return
	 */
	public static <K, V> Map<K, V>  getMapByKeys(Map<K, V> mapCache,
			K[] keys){
		List<K> listKey = Arrays.asList(keys);
		return getMapByKeys(mapCache, listKey);
	}
}
