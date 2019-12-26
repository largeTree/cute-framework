package com.qiuxs.cuteframework.tech.mc.redis.utils;

import com.qiuxs.cuteframework.tech.mc.redis.RedisFacade;

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
	 * redisFacade连接中的键key对应的值加1
	 *  
	 * @author fengdg  
	 * @param redisFacade
	 * @param key redis中的键
	 * @return
	 */
	public static Long incr(RedisFacade redisFacade, String key) {
		if (redisFacade == null) {
			return null;
		} else {
			return redisFacade.incr(key);
		}
	}

	/**
	 * redisFacade连接中的键key对应的值加increment
	 *  
	 * @author fengdg  
	 * @param redisFacade
	 * @param key 	Redis中的键
	 * @param increment	增量
	 * @return
	 */
	public static Long incrBy(RedisFacade redisFacade, String key, long increment) {
		if (redisFacade == null) {
			return null;
		} else {
			return redisFacade.incrBy(key, increment);
		}
	}

	/**
	 * redisFacade连接中的键key对应的值减1
	 *  
	 * @author fengdg  
	 * @param redisFacade
	 * @param key	Redis中的键
	 * @return
	 */
	public static Long decr(RedisFacade redisFacade, String key) {
		if (redisFacade == null) {
			return null;
		} else {
			return redisFacade.decr(key);
		}
	}

	/**
	 * redisFacade连接中的键key对应的值减decrement
	 *  
	 * @author fengdg  
	 * @param redisFacade
	 * @param key		Redis中的键
	 * @param decrement	减量
	 * @return
	 */
	public static Long decrBy(RedisFacade redisFacade, String key, long decrement) {
		if (redisFacade == null) {
			return null;
		} else {
			return redisFacade.decrBy(key, decrement);
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
		return hincr(redisMap.getRedisFacade(), redisMap.getNameKey(), field);
	}

	/**
	 * redisFacade连接中的键key对应的hashes中的字段field值加1
	 *  
	 * @author fengdg  
	 * @param redisFacade
	 * @param key	Redis的键
	 * @param field	hashes的字段
	 * @return
	 */
	public static Long hincr(RedisFacade redisFacade, String key, String field) {
		return hincrBy(redisFacade, key, field, 1);
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
		return hincrBy(redisMap.getRedisFacade(), redisMap.getNameKey(), field, 1);
	}

	/**
	 * redisFacade连接中的键key对应的hashes中的字段field值赠increment
	 *  
	 * @author fengdg  
	 * @param redisFacade
	 * @param key		Redis的键
	 * @param field		hashes的字段
	 * @param increment	增量
	 * @return
	 */
	public static Long hincrBy(RedisFacade redisFacade, String key, String field, long increment) {
		if (redisFacade == null) {
			return null;
		} else {
			return redisFacade.hincrBy(key, field, increment);
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
		return hdecr(redisMap.getRedisFacade(), redisMap.getNameKey(), field);
	}

	/**
	 * redisFacade连接中的键key对应的hashes中的字段field值减1
	 *  
	 * @author fengdg  
	 * @param redisFacade
	 * @param key	Redis中的键
	 * @param field	hashes中的字段
	 * @return
	 */
	public static Long hdecr(RedisFacade redisFacade, String key, String field) {
		return hdecrBy(redisFacade, key, field, 1);
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
		return hdecrBy(redisMap.getRedisFacade(), redisMap.getNameKey(), field, 1);
	}

	/**
	 * redisFacade连接中的键key对应的hashes中的字段field值减decrement
	 *  
	 * @author qiuxs  
	 * @param redisFacade
	 * @param key	Redis中的键
	 * @param field	hashes中的字段
	 * @param decrement	减量
	 * @return
	 */
	public static Long hdecrBy(RedisFacade redisFacade, String key, String field, long decrement) {
		if (redisFacade == null) {
			return null;
		} else {
			return redisFacade.hincrBy(key, field, -decrement);
		}
	}
}
