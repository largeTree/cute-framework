package com.qiuxs.cuteframework.tech.mc.redis.comn;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import com.qiuxs.cuteframework.core.basic.constants.SymbolConstants;
import com.qiuxs.cuteframework.core.basic.utils.CollectionUtils;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.tech.mc.McFactory;

/**
 * 通用缓存池
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年9月28日 上午11:57:35 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class CommonCachePool {

	private static final class Holder {
		private static final Map<String, DataWrapper> cacheMap = McFactory.getFactory().createMap("common_cache_pool");
	}
	
	/**
	 * 按Map组成缓存key，存入缓存
	 *  
	 * @author qiuxs  
	 * @param keySeed
	 * @param keyKeys
	 * 		指定map中的哪些key生成缓存
	 * @param expiresIn
	 * @param data
	 */
	public static void put(Map<String, ?> keySeed, Set<String> keyKeys, String prefix, long expiresIn, Serializable data) {
		String key = genKeyByMap(keySeed, prefix, keyKeys);
		put(key, expiresIn, data);
	}
	
	/**
	 * 按Map组成key，获取缓存
	 *  
	 * @author qiuxs  
	 * @param <T>
	 * @param keySeed
	 * @param prefix
	 * 		前缀， 不允许为空
	 * @param keyKeys
	 * @return
	 */
	public static <T extends Serializable> T get(Map<String, ?> keySeed, String prefix, Set<String> keyKeys) {
		String key = genKeyByMap(keySeed, prefix, keyKeys);
		return get(key);
	}
	
	/**
	 * 生成key
	 *  
	 * @author qiuxs  
	 * @param keySeed
	 * @param keyKeys
	 * @return
	 */
	public static String genKeyByMap(Map<String, ?> keySeed, String prefix, Set<String> keyKeys) {
		if (StringUtils.isBlank(prefix)) {
			ExceptionUtils.throwRuntimeException("prefix is empty");
		}
		if (CollectionUtils.isEmpty(keyKeys) && CollectionUtils.isEmpty(keySeed)) {
			ExceptionUtils.throwRuntimeException("keySeed and keyKeys both null");
		}
		if (CollectionUtils.isEmpty(keyKeys)) {
			keyKeys = keySeed.keySet();
		}
		StringBuilder sb = new StringBuilder(prefix);
		sb.append(SymbolConstants.SEPARATOR_UNDERLINE);
		for (String key : keyKeys) {
			sb.append(keySeed.get(key)).append(SymbolConstants.SEPARATOR_UNDERLINE);
		}
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}

	/**
	 * 放入缓存
	 *  
	 * @author qiuxs  
	 * @param key
	 * @param expirin
	 * @param data
	 */
	public static void put(String key, long expiresIn, Serializable data) {
		DataWrapper dw = new DataWrapper(expiresIn);
		dw.setData(data);
		Holder.cacheMap.put(key, dw);
	}

	/**
	 * 获取缓存
	 *  
	 * @author qiuxs  
	 * @param <T>
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T get(String key) {
		DataWrapper dw = Holder.cacheMap.get(key);
		if (dw != null && dw.expired()) {
			dw = null;
			Holder.cacheMap.remove(key);
		}
		return dw != null ? (T)dw.getData() : null;
	}

}
