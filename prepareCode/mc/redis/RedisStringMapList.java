package com.hzecool.core.cache.mc.redis;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.hzecool.core.context.EnvironmentHolder;
import com.hzecool.fdn.bean.IKeyVal;
import com.hzecool.fdn.bean.IVal;
import com.hzecool.fdn.utils.MapUtils;
import com.hzecool.tech.redis.RedisDAO;

/**
 * 
 * 功能描述: 存放RedisStringMap的List集合：可以基于idList进行分页（idSet不支持）
 * --若T是IKeyVal，则额外存储bizKeyMap；
 * 
 * 格式： 
 * --objMap部分存储对象：<String("map:"+prefix+":"+id),[<String(objProp), String(objPropValue)>,...]>
 * --idList部分存储对象id：<String(prefix+className+"Ids"),[id,...]>
 * --bizkeyMap部分存储：<String(prefix+className+"BizKeys"),[<String(bizKey), String(id)>,...]>
 * 
 * <p>
 * 新增原因: TODO
 * 
 * @author fengdg
 * @version 1.0.0
 * @since 2017年3月14日 下午3:52:52
 */
public class RedisStringMapList<T extends IVal<String>> {
	//TODO: idSet和bizKeyMap在session自动超时情况下不会自动清除
	protected RedisDAO redisDAO;
	//idSet相关
	protected String setKey;
	protected List<String> idList;//
	//objMap相关
	protected String prefix;
	protected Class<T> tClazz;
	//bizKeyMap相关：用于业务主键到objMap的key的映射
	protected Map<String, String> bizKeyMap;

	public RedisStringMapList(String prefix, Class<T> tClazz) {
		this.prefix = prefix;
		this.tClazz = tClazz;
		
		this.setKey = prefix + ":" + tClazz.getSimpleName() + "Ids";
		this.idList = new RedisStringList(setKey, getRedisDAO());
		
		if (IKeyVal.class.isAssignableFrom(tClazz)) {
			this.bizKeyMap = new RedisStringMap(prefix + ":" + tClazz.getSimpleName() + "BizKeys", getRedisDAO());
		}
	}

	/**
	 * 功能描述: 用于延迟初始化RedisDAO. lazy initialization holder class模式
	 */
	private static class DefaultRedisDAOHolder {
		static String masterName = EnvironmentHolder.getEnvParam("string_map_collection_master_name");
		/** map用的比较多，用自己的连接池 ，strmapset=StringMapSet */
		static final RedisDAO REDIS_DAO = new RedisDAO("strmapset", "0", StringUtils.isNotEmpty(masterName) ? masterName :"mymaster");//master名称需要在配置文件中配置，并且运维那边需要在redis服务端配置。
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

	public boolean exists(String id) {
		Boolean exist = getRedisDAO().exists(getMapKey(id));
		return exist != null ? exist.booleanValue() : false;
	}

	public T getObject(String id) {
		if (id == null) {
			return null;
		}
		if (exists(id)) {
			Map<String, String> fieldMap = getRedisDAO().hgetAll(getMapKey(id));
			T entity = MapUtils.map2Bean(fieldMap, tClazz);
			return entity;
		} else {
			return null;
		}
	}

	/**
	 * 放入对象
	 *  
	 * @author fengdg  
	 * @param entity
	 */
	public void putObject(T entity) {
		Map<String, String> map = MapUtils.bean2StringMap(entity, true);
		getRedisDAO().hmset(getMapKey(entity.getVal()), map);
		if (bizKeyMap != null && IKeyVal.class.isAssignableFrom(tClazz)) {
			@SuppressWarnings("unchecked")
			IKeyVal<?, String> bean = (IKeyVal<?, String>)entity;
			bizKeyMap.put(bean.getKey().toString(), entity.getVal());
		}
		//允许重复，因为排重代价太高；使用Set则不能方便的分页
		idList.add(entity.getVal());
	}
	
	/**
	 * 通过主键删除
	 *  
	 * @author fengdg  
	 * @param id
	 */
	public void removeObject(String id) {
		if (id == null) {
			return;
		}
		T entity = getObject(id);
		if (entity != null && bizKeyMap != null && IKeyVal.class.isAssignableFrom(tClazz)) {
			IKeyVal<?,?> bean = (IKeyVal<?,?>)entity;
			bizKeyMap.remove(bean.getKey());
		}
		getRedisDAO().del(getMapKey(id));
		idList.remove(id);
	}
	
	/**
	 * 通过业务主键删除
	 *  
	 * @author fengdg  
	 * @param bizKey
	 */
	public void removeObjectByBizKey(String bizKey) {
		if (bizKey == null || bizKeyMap == null) {
			return ;
		}
		String id = bizKeyMap.get(bizKey);
		if (id != null) {
			bizKeyMap.remove(bizKey);
			getRedisDAO().del(getMapKey(id));
			idList.remove(id);
		}
	}
	
	public String getFieldValue(String id, String field) {
		return getRedisDAO().hget(getMapKey(id), field);
	}

	public Long setFieldValue(String id, String field, String value) {
		return getRedisDAO().hset(getMapKey(id), field, value);
	}

	public String getMapKey(String id) {
		return "map:" + prefix + ":" + id;
	}

	public List<String> getIds() {
		return idList;
	}
}
