package com.qiuxs.gconfig.client;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.core.basic.bean.UserLite;
import com.qiuxs.cuteframework.core.basic.utils.BeanUtil;
import com.qiuxs.cuteframework.core.persistent.database.lookup.DataSourceContext;
import com.qiuxs.gconfig.client.dto.GConfigDTO;
import com.qiuxs.gconfig.entity.ScGconfig;
import com.qiuxs.gconfig.entity.ScGconfigOwnerVal;
import com.qiuxs.gconfig.service.IScGconfigOwnerValService;
import com.qiuxs.gconfig.service.IScGconfigService;

@Component
public class GConfigClientUtils {
	
	private static Logger log = LoggerFactory.getLogger(GConfigClientUtils.class);
	
	private static final Map<Integer, Map<Long, Map<String, GConfigDTO>>> cacheMap = new ConcurrentHashMap<>();  
	
	private static IScGconfigService scGconfigService;

	private static IScGconfigOwnerValService scGconfigOwnerValService;

	/**
	 * 获取系统配置
	 *  
	 * @author qiuxs  
	 * @param code
	 * @return
	 */
	public static String getSystemConfig(String code) {
		return getSystemConfig(null, code);
	}
	
	/**
	 * 获取int型系统配置
	 *  
	 * @author qiuxs  
	 * @param code
	 * @param defVal
	 * @return
	 */
	public static int getSystemConfigInt(String code, int defVal) {
		String val = getSystemConfig(code);
		if (val == null) {
			return defVal;
		} else {
			return Integer.parseInt(val);
		}
	}
	
	/**
	 * 
	 *  获取BigDecimal类型的系统配置
	 * @author qiuxs  
	 * @param code
	 * @param defVal
	 * @return
	 */
	public static BigDecimal getSystemConfigBigDecimal(String code, BigDecimal defVal) {
		String val = getSystemConfig(code);
		if (val == null) {
			return defVal;
		} else {
			return new BigDecimal(val);
		}
	}
	
	/**
	 * 获取系统配置
	 *  
	 * @author qiuxs  
	 * @param userLite
	 * 		不指定时取当前会话
	 * @param code
	 * @return
	 */
	public static String getSystemConfig(UserLite userLite, String code) {
		return getConfig(userLite, ScGconfig.DOMAIN_SYSTEM, code);
	}
	
	/**
	 * 获取指定的参数值
	 *  
	 * @author qiuxs  
	 * @param userLite
	 * @param domain
	 * @param code
	 * @return
	 */
	private static String getConfig(UserLite userLite, String domain, String code) {
		String oldDsId = null;
		
		try {
			Long userId = userLite == null ? 0L : userLite.getUserId();
			Long roleId = userLite == null ? 0L : userLite.getRoleId();
			// 获取当前用户的缓存
			Map<String, GConfigDTO> userConfigMap = getUserCache(userId);
			
			String configKey = configKey(domain, code);
			
			// 从用户缓存中获取配置对象
			GConfigDTO configDTO = userConfigMap.get(configKey);
			
			if (configDTO == null) {
				if (!DataSourceContext.isDsSwitchAuto()) {
					oldDsId = DataSourceContext.setEntryDb();
				}
				ScGconfig scGconfig = scGconfigService.getByUk(domain, code);
				if (scGconfig == null) {
					// ExceptionUtils.throwLogicalException("gconfig_not_exists", domain, code);
					// 没有查到配置时返回null、外层处理
					return null;
				}
				// 配置对象
				configDTO = new GConfigDTO();
				BeanUtil.assignmentProperty(scGconfig, configDTO, "id,createdTime,createdBy,updatedTime,updatedBy");
				
				// 设置用户自己的值
				ScGconfigOwnerVal ownerVal = scGconfigOwnerValService.getOwnerVal(domain, ScGconfigOwnerVal.OWNER_TYPE_USER, userId, code);
				if (ownerVal == null) {
					// 设置角色的值
					ownerVal = scGconfigOwnerValService.getOwnerVal(domain, ScGconfigOwnerVal.OWNER_TYPE_ROLE, roleId, code);
				}
				
				if (ownerVal != null) {
					configDTO.setVal(ownerVal.getVal());
				} else {
					configDTO.setVal(scGconfig.getVal());
				}
				
				// 保存用户配置缓存
				userConfigMap.put(configKey, configDTO);
			}
			return configDTO.getVal();
		} finally {
			if (oldDsId != null) {
				DataSourceContext.setUpDs(oldDsId);
			}
		}
	}

	/**
	 * 获取用户级别的配置
	 *  
	 * @author qiuxs  
	 * @param userId
	 * @return
	 */
	private static Map<String, GConfigDTO> getUserCache(Long userId) {
		Map<String, GConfigDTO> userCache = getCache(ScGconfigOwnerVal.OWNER_TYPE_USER).get(userId);
		if (userCache == null) {
			userCache = new ConcurrentHashMap<String, GConfigDTO>();
			getCache(ScGconfigOwnerVal.OWNER_TYPE_USER).put(userId, userCache);
		}
		return userCache;
	}
	
//	/**
//	 * 获取角色级别缓存
//	 *  
//	 * @author qiuxs  
//	 * @param roleId
//	 * @return
//	 */
//	private static Map<String, GConfigDTO> getRoleCache(Long roleId) {
//		@SuppressWarnings("unchecked")
//		Map<String, GConfigDTO> roleCache = getCache(OWNER_DOMAIN_ROLE).get(roleId);
//		if (roleCache == null) {
//			roleCache = new HashMap<String, GConfigDTO>();
//			getCache(OWNER_DOMAIN_ROLE).put(roleId, roleCache);
//		}
//		return roleCache;
//	}
//	
//	private static Map<String, GConfigDTO> getSystemCache() {
//		@SuppressWarnings("unchecked")
//		Map<String, GConfigDTO> systemCache = getCache(OWNER_DOMAIN_SYSTEM).get(0L);
//		if (systemCache == null) {
//			systemCache = new HashMap<String, GConfigDTO>();
//			getCache(OWNER_DOMAIN_SYSTEM).put(0L, systemCache);
//		}
//		return systemCache;
//	}
	
	/**
	 * 设置用户的配置缓存
	 *  
	 * @author qiuxs  
	 * @param userId
	 * @param cacheMap
	 */
//	@SuppressWarnings("rawtypes")
//	private static void putUserCache(Long userId, Map<String, GConfigDTO> cacheMap) {
//		Map<Long, Map> userCache = getCache(ScGconfigOwnerVal.OWNER_TYPE_USER);
//		userCache.put(userId, cacheMap);
//	}
	
//	/**
//	 * 设置角色缓存
//	 *  
//	 * @author qiuxs  
//	 * @param roleId
//	 * @param cacheMap
//	 */
//	private static void putRoleCache(Long roleId, Map<String, GConfigDTO> cacheMap) {
//		getCache(OWNER_DOMAIN_ROLE).put(roleId, cacheMap);
//	}
//	
//	/***
//	 * 设置系统级缓存
//	 *  
//	 * @author qiuxs  
//	 * @param cacheMap
//	 */
//	private static void putSystemCache(Map<String, GConfigDTO> cacheMap) {
//		getCache(OWNER_DOMAIN_SYSTEM).put(0L, cacheMap);
//	}
	
	private static String configKey(String domain, String code) {
		return domain + "." + code;
	}
	
	/**
	 * 全局配置map
	 *  
	 * @author qiuxs  
	 * @return
	 */
	private static Map<Long, Map<String, GConfigDTO>> getCache(Integer ownerDomain) {
		Map<Long, Map<String, GConfigDTO>> map = cacheMap.get(ownerDomain);
		if (map == null) {
			map = new ConcurrentHashMap<>();
			cacheMap.put(ownerDomain, map);
		}
		return map;
	}
	
	
	/**
	 * 失效缓存
	 *  
	 * @author qiuxs  
	 * @param domain
	 * @param ownerType
	 * @param ownerId
	 * @param code
	 */
	public static void invalidCache(String domain, Integer ownerType, Long ownerId, String code) {
		log.info("invalidCache domain = {}, ownerType = {}, ownerId = {}, code = {}");
		cacheMap.clear();
		// ownerType为空直接清理所有缓存
//		if (ownerType == null) {
//			cacheMap.clear();
//			return;
//		}
//		Map<Long, Map<String, GConfigDTO>> ownerMap = cacheMap.get(ownerType);
//		// ownerType对应的map为空，直接返回
//		if (ownerMap == null) {
//			return;
//		}
//		// ownerId为空，清理当前ownerType的所有缓存
//		if (ownerId == null) {
//			ownerMap.clear();
//			return;
//		}
//		Map<String, GConfigDTO> ownerIdMap = ownerMap.get(ownerId);
//		// ownerIdMap为空，直接返回
//		if (ownerIdMap == null) {
//			return;
//		}
//		// code的domain任意为空，直接清理当前ownerId的所有缓存
//		if (StringUtils.isBlank(code) || StringUtils.isBlank(domain)) {
//			ownerIdMap.clear();
//			return;
//		}
//		ownerIdMap.remove(configKey(domain, code));
	}

	@Resource
	public void setScGconfigService(IScGconfigService scGconfigService) {
		GConfigClientUtils.scGconfigService = scGconfigService;
	}

//	@Resource
//	public void setScGconfigOptionsService(IScGconfigOptionsService scGconfigOptionsService) {
//		GConfigClientUtils.scGconfigOptionsService = scGconfigOptionsService;
//	}
	
	@Resource
	public void setScGconfigOwnerValService(IScGconfigOwnerValService scGconfigOwnerValService) {
		GConfigClientUtils.scGconfigOwnerValService = scGconfigOwnerValService;
	}

}
