package com.qiuxs.gconfig.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.core.basic.bean.UserLite;
import com.qiuxs.cuteframework.core.basic.utils.BeanUtil;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.tech.mc.McFactory;
import com.qiuxs.gconfig.client.dto.GConfigDTO;
import com.qiuxs.gconfig.client.dto.GConfigOptions;
import com.qiuxs.gconfig.entity.ScGconfig;
import com.qiuxs.gconfig.entity.ScGconfigOptions;
import com.qiuxs.gconfig.entity.ScGconfigOwnerVal;
import com.qiuxs.gconfig.service.IScGconfigOptionsService;
import com.qiuxs.gconfig.service.IScGconfigOwnerValService;
import com.qiuxs.gconfig.service.IScGconfigService;

@Component
public class GConfigClientUtils {

	private static class Holder {
		@SuppressWarnings("rawtypes")
		private static Map<Long, Map> mapGconfigDto = McFactory.getFactory().createMap(Long.class, Map.class, 10, "gconfig_dto_holder");
	}

	private static IScGconfigService scGconfigService;

	private static IScGconfigOptionsService scGconfigOptionsService;
	
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
	 * 获取系统配置
	 *  
	 * @author qiuxs  
	 * @param userLite
	 * 		不指定时取当前会话
	 * @param code
	 * @return
	 */
	public static String getSystemConfig(UserLite userLite, String code) {
		Long userId = userLite == null ? 0L : userLite.getUserId();
		
		// 获取当前用户的缓存
		Map<String, GConfigDTO> userConfigMap = getUserCache(userId);
		
		// 从用户缓存中获取配置对象
		GConfigDTO configDTO = userConfigMap.get(code);
		if (configDTO == null) {
			ScGconfig scGconfig = scGconfigService.getByUk(code);
			if (scGconfig == null) {
				ExceptionUtils.throwLogicalException("gconfig_not_exists", code);
			}
			// 配置对象
			configDTO = new GConfigDTO();
			BeanUtil.assignmentProperty(scGconfig, configDTO, "id,createdTime,createdBy,updatedTime,updatedBy");
			
			// 可选值
			List<ScGconfigOptions> options = scGconfigOptionsService.getByCode(code);
			List<GConfigOptions> simpleOptions = BeanUtil.assignmentPropertyBatch(options, GConfigOptions.class, "createdTime,createdBy,updatedTime,updatedBy");
			configDTO.setOpts(simpleOptions);
			
			// 设置用户自己的值
			ScGconfigOwnerVal ownerVal = scGconfigOwnerValService.getOwnerVal(ScGconfigOwnerVal.OWNER_TYPE_USER, userId, code);
			configDTO.setVal(ownerVal.getVal());
			
			// 保存用户配置缓存
			userConfigMap.put(code, configDTO);
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
		Map<String, GConfigDTO> userCache = getCache().get(userId);
		if (userCache == null) {
			userCache = new HashMap<String, GConfigDTO>();
			getCache().put(userId, userCache);
		}
		return userCache;
	}
	
	/**
	 * 设置用户的配置缓存
	 *  
	 * @author qiuxs  
	 * @param userId
	 * @param cacheMap
	 */
	public static void putUserCache(Long userId, Map<String, GConfigDTO> cacheMap) {
		getCache().put(userId, cacheMap);
	}
	
	/**
	 * 全局配置map
	 *  
	 * @author qiuxs  
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static Map<Long, Map> getCache() {
		return Holder.mapGconfigDto;
	}

	@Resource
	public void setScGconfigService(IScGconfigService scGconfigService) {
		GConfigClientUtils.scGconfigService = scGconfigService;
	}

	@Resource
	public void setScGconfigOptionsService(IScGconfigOptionsService scGconfigOptionsService) {
		GConfigClientUtils.scGconfigOptionsService = scGconfigOptionsService;
	}
	
	@Resource
	public void setScGconfigOwnerValService(IScGconfigOwnerValService scGconfigOwnerValService) {
		GConfigClientUtils.scGconfigOwnerValService = scGconfigOwnerValService;
	}

}
