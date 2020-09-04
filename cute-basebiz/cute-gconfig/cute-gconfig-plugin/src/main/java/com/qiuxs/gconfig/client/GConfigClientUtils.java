package com.qiuxs.gconfig.client;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.core.basic.bean.UserLite;
import com.qiuxs.cuteframework.core.basic.utils.BeanUtil;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.tech.mc.McFactory;
import com.qiuxs.gconfig.client.dto.GConfigDTO;
import com.qiuxs.gconfig.entity.ScGconfig;
import com.qiuxs.gconfig.entity.ScGconfigOwnerVal;
import com.qiuxs.gconfig.service.IScGconfigOwnerValService;
import com.qiuxs.gconfig.service.IScGconfigService;

@Component
public class GConfigClientUtils {

	private static final String OWNER_DOMAIN_USER = "user";
	private static final String OWNER_DOMAIN_ROLE = "role";
	private static final String OWNER_DOMAIN_SYSTEM = "system";
	
	private static class Holder {
		@SuppressWarnings("rawtypes")
		private static Map<String, Map> mapGconfigDto = McFactory.getFactory().createMap(String.class, Map.class, 10, "gconfig_dto_holder");
	}

	private static IScGconfigService scGconfigService;

	// private static IScGconfigOptionsService scGconfigOptionsService;
	
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
		Long userId = userLite == null ? 0L : userLite.getUserId();
		Long roleId = userLite == null ? 0L : userLite.getRoleId();
		
		// 获取当前用户的缓存
		Map<String, GConfigDTO> userConfigMap = getUserCache(userId);
		
		String configKey = configKey(domain, code);
		
		// 从用户缓存中获取配置对象
		GConfigDTO configDTO = userConfigMap.get(configKey);
		
		if (configDTO == null) {
			ScGconfig scGconfig = scGconfigService.getByUk(domain, code);
			if (scGconfig == null) {
				ExceptionUtils.throwLogicalException("gconfig_not_exists", domain, code);
			}
			// 配置对象
			configDTO = new GConfigDTO();
			BeanUtil.assignmentProperty(scGconfig, configDTO, "id,createdTime,createdBy,updatedTime,updatedBy");
			
//			// 可选值 不需要缓存
//			List<ScGconfigOptions> options = scGconfigOptionsService.getByCode(domain, code);
//			List<GConfigOptions> simpleOptions = BeanUtil.assignmentPropertyBatch(options, GConfigOptions.class, "createdTime,createdBy,updatedTime,updatedBy");
//			configDTO.setOpts(simpleOptions);
			
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
			putUserCache(userId, userConfigMap);
		}
		return configDTO.getVal();
	}

	/**
	 * 获取用户级别的配置
	 *  
	 * @author qiuxs  
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, GConfigDTO> getUserCache(Long userId) {
		Map<String, GConfigDTO> userCache = getCache(OWNER_DOMAIN_USER).get(userId);
		if (userCache == null) {
			userCache = new HashMap<String, GConfigDTO>();
			getCache(OWNER_DOMAIN_USER).put(userId, userCache);
		}
		return userCache;
	}
	
	/**
	 * 获取角色级别缓存
	 *  
	 * @author qiuxs  
	 * @param roleId
	 * @return
	 */
	private static Map<String, GConfigDTO> getRoleCache(Long roleId) {
		@SuppressWarnings("unchecked")
		Map<String, GConfigDTO> roleCache = getCache(OWNER_DOMAIN_ROLE).get(roleId);
		if (roleCache == null) {
			roleCache = new HashMap<String, GConfigDTO>();
			getCache(OWNER_DOMAIN_ROLE).put(roleId, roleCache);
		}
		return roleCache;
	}
	
	private static Map<String, GConfigDTO> getSystemCache() {
		@SuppressWarnings("unchecked")
		Map<String, GConfigDTO> systemCache = getCache(OWNER_DOMAIN_SYSTEM).get(0L);
		if (systemCache == null) {
			systemCache = new HashMap<String, GConfigDTO>();
			getCache(OWNER_DOMAIN_SYSTEM).put(0L, systemCache);
		}
		return systemCache;
	}
	
	/**
	 * 设置用户的配置缓存
	 *  
	 * @author qiuxs  
	 * @param userId
	 * @param cacheMap
	 */
	public static void putUserCache(Long userId, Map<String, GConfigDTO> cacheMap) {
		getCache(OWNER_DOMAIN_USER).put(userId, cacheMap);
	}
	
	/**
	 * 设置角色缓存
	 *  
	 * @author qiuxs  
	 * @param roleId
	 * @param cacheMap
	 */
	public static void putRoleCache(Long roleId, Map<String, GConfigDTO> cacheMap) {
		getCache(OWNER_DOMAIN_ROLE).put(roleId, cacheMap);
	}
	
	/***
	 * 设置系统级缓存
	 *  
	 * @author qiuxs  
	 * @param cacheMap
	 */
	public static void putSystemCache(Map<String, GConfigDTO> cacheMap) {
		getCache(OWNER_DOMAIN_SYSTEM).put(0L, cacheMap);
	}
	
	private static String configKey(String domain, String code) {
		return domain + "." + code;
	}
	
	/**
	 * 全局配置map
	 *  
	 * @author qiuxs  
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Map<Long, Map> getCache(String ownerDomain) {
		Map map = Holder.mapGconfigDto.get(ownerDomain);
		if (map == null) {
			map = new HashMap<>();
			Holder.mapGconfigDto.put(ownerDomain, map);
		}
		return map;
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
