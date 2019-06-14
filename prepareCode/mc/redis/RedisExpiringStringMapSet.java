package com.hzecool.core.cache.mc.redis;

import com.hzecool.core.context.EnvironmentHolder;
import com.hzecool.fdn.bean.IVal;
import com.hzecool.tech.redis.RedisDAO;

/**
 * 
 * 功能描述: 存放自动失效的RedisStringMap的set集合 
 * --若T是IStringBizKeyBean，则额外存储bizKeyMap；
 * --基于Redis的失效机制来使整个objMap失效（移除）
 * 
 * 格式： 
 * --objMap部分存储对象：<String("map:"+prefix+":"+id),[<String(objProp), String(objPropValue)>,...]>
 * --idSet部分存储对象id：<String(prefix+className+"Ids"),[id,...]>
 * --bizkeyMap部分存储：<String(prefix+className+"BizKeys"),[<String(bizKey), String(id)>,...]>
 * 
 * 问题：
 * --objMap自动失效后，idSet中的失效objMap仍然存在
 *  
 * @author fengdg   
 * @version 1.0.0
 * @since 2017年3月14日 下午3:52:52
 */
public class RedisExpiringStringMapSet<T extends IVal<String>> extends RedisStringMapSet<T>{
	/**默认超时时间30分钟*/
	private static final Integer DEFAULT_TIMEOUT = 30 * 60;
	private static String redisMasterName = "expmaster";

	public RedisExpiringStringMapSet(String prefix, Class<T> tClazz) {
		super(prefix, tClazz);
	}
	/**
	 * 功能描述: 用于延迟初始化RedisDAO. lazy initialization holder class模式
	 */
	private static class DefaultRedisDAOHolder {
		static RedisDAO REDIS_DAO;
		static {
			String masterName = EnvironmentHolder.getEnvParam("exp_redis_master_name");
			if (masterName != null) {
				RedisExpiringStringMapSet.redisMasterName = masterName;
			}
			/** 用自己的连接池 ， expstrmapset=ExpiringStringMapSet*/
			REDIS_DAO = new RedisDAO("expstrmapset", "0", RedisExpiringStringMapSet.redisMasterName);	
		}
//		static final RedisDAO REDIS_DAO = new RedisDAO("expstrmapset", "0", "expmaster");
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
	
	/**
	 * 重置失效时间
	 *  
	 * @author fengdg  
	 * @param id
	 * @param expireSecond
	 */
	public void expire(String id, Integer expireSecond) {
		getRedisDAO().expire(getMapKey(id), expireSecond);
	}
	
	/**
	 * 重置默认失效时间
	 *  
	 * @author fengdg  
	 * @param id
	 */
	public void expire(String id) {
		getRedisDAO().expire(getMapKey(id), DEFAULT_TIMEOUT);
	}

}
