package com.hzecool.core.cache.mc.redis;

import com.hzecool.tech.redis.RedisDAO;

/**
 * 
 * 功能描述: 操作的Redis中计数器的工具类
 * <p>
 * 新增原因: TODO
 * 
 * @author fengdg
 * @version 1.0.0
 * @since 2017年3月14日 上午9:41:28
 */
public class RedisCounterUtils {
	
	/**
	 * redisDao连接中的键key对应的值加1
	 *  
	 * @author fengdg  
	 * @param redisDAO
	 * @param key redis中的键
	 * @return
	 */
	public static Long incr(RedisDAO redisDAO, String key) {
		if (redisDAO == null) {
			return null;
		} else {
			return redisDAO.incr(key);
		}
	}
	
	/**
	 * redisDao连接中的键key对应的值加increment
	 *  
	 * @author fengdg  
	 * @param redisDAO
	 * @param key 	Redis中的键
	 * @param increment	增量
	 * @return
	 */
	public static Long incrBy(RedisDAO redisDAO, String key, long increment) {
		if (redisDAO == null) {
			return null;
		} else {
			return redisDAO.incrBy(key, increment);
		}
	}

	/**
	 * redisDao连接中的键key对应的值减1
	 *  
	 * @author fengdg  
	 * @param redisDAO
	 * @param key	Redis中的键
	 * @return
	 */
	public static Long decr(RedisDAO redisDAO, String key) {
		if (redisDAO == null) {
			return null;
		} else {
			return redisDAO.decr(key);
		}
	}
	
	/**
	 * redisDao连接中的键key对应的值减decrement
	 *  
	 * @author fengdg  
	 * @param redisDAO
	 * @param key		Redis中的键
	 * @param decrement	减量
	 * @return
	 */
	public static Long decrBy(RedisDAO redisDAO, String key, long decrement) {
		if (redisDAO == null) {
			return null;
		} else {
			return redisDAO.decrBy(key, decrement);
		}
	}

	/**
	 * redisMap的字段field值加1
	 *  
	 * @author fengdg  
	 * @param redisMap
	 * @param field	redisMap中的字段
	 * @return 增加后的值
	 */
	public static <K, V> Long hincr(IRedisMap<K, V> redisMap, String field) {
		return hincr(redisMap.getRedisDAO(), redisMap.getNameKey(), field);
	}

	/**
	 * redisDao连接中的键key对应的hashes中的字段field值加1
	 *  
	 * @author fengdg  
	 * @param redisDAO
	 * @param key	Redis的键
	 * @param field	hashes的字段
	 * @return
	 */
	public static Long hincr(RedisDAO redisDAO, String key, String field) {
		return hincrBy(redisDAO, key, field, 1);
	}

	/**
	 * redisMap的字段field值加increment
	 *  
	 * @author fengdg  
	 * @param redisMap
	 * @param field		hashes的字段
	 * @param increment	增量
	 * @return
	 */
	public static <K, V> Long hincrBy(IRedisMap<K, V> redisMap, String field, long increment) {
		return hincrBy(redisMap.getRedisDAO(), redisMap.getNameKey(), field, 1);
	}

	/**
	 * redisDao连接中的键key对应的hashes中的字段field值赠increment
	 *  
	 * @author fengdg  
	 * @param redisDAO
	 * @param key		Redis的键
	 * @param field		hashes的字段
	 * @param increment	增量
	 * @return
	 */
	public static Long hincrBy(RedisDAO redisDAO, String key, String field, long increment) {
		if (redisDAO == null) {
			return null;
		} else {
			return redisDAO.hincrBy(key, field, increment);
		}
	}
	
	/**
	 * redisMap的字段field值键1
	 *  
	 * @author fengdg  
	 * @param redisMap
	 * @param field	redisMap的字段
	 * @return
	 */
	public static <K, V> Long hdecr(IRedisMap<K, V> redisMap, String field) {
		return hdecr(redisMap.getRedisDAO(), redisMap.getNameKey(), field);
	}

	/**
	 * redisDao连接中的键key对应的hashes中的字段field值减1
	 *  
	 * @author fengdg  
	 * @param redisDAO
	 * @param key	Redis中的键
	 * @param field	hashes中的字段
	 * @return
	 */
	public static Long hdecr(RedisDAO redisDAO, String key, String field) {
		return hdecrBy(redisDAO, key, field, 1);
	}

	/**
	 * redisMap的字段field值减decrement
	 *  
	 * @author fengdg  
	 * @param redisMap
	 * @param field		redisMap的字段
	 * @param decrement 减量
	 * @return
	 */
	public static <K, V> Long hdecrBy(IRedisMap<K, V> redisMap, String field, long decrement) {
		return hdecrBy(redisMap.getRedisDAO(), redisMap.getNameKey(), field, 1);
	}

	/**
	 * redisDao连接中的键key对应的hashes中的字段field值减decrement
	 *  
	 * @author fengdg  
	 * @param redisDAO
	 * @param key	Redis中的键
	 * @param field	hashes中的字段
	 * @param decrement	减量
	 * @return
	 */
	public static Long hdecrBy(RedisDAO redisDAO, String key, String field, long decrement) {
		if (redisDAO == null) {
			return null;
		} else {
			return redisDAO.hincrBy(key, field, -decrement);
		}
	}
}
