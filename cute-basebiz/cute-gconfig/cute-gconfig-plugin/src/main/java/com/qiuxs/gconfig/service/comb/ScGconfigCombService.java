package com.qiuxs.gconfig.service.comb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.basic.utils.CollectionUtils;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.MapUtils;
import com.qiuxs.cuteframework.core.basic.utils.NumberUtils;
import com.qiuxs.cuteframework.core.context.UserContext;
import com.qiuxs.cuteframework.core.persistent.database.dao.page.PageInfo;
import com.qiuxs.cuteframework.tech.task.AsyncTaskExecutor;
import com.qiuxs.cuteframework.tech.task.RunnableAsyncTask;
import com.qiuxs.gconfig.client.GConfigClientUtils;
import com.qiuxs.gconfig.client.dto.GConfigDTO;
import com.qiuxs.gconfig.client.dto.GConfigOptions;
import com.qiuxs.gconfig.entity.ScGconfig;
import com.qiuxs.gconfig.entity.ScGconfigOptions;
import com.qiuxs.gconfig.entity.ScGconfigOwnerVal;
import com.qiuxs.gconfig.service.impl.ScGconfigOptionsService;
import com.qiuxs.gconfig.service.impl.ScGconfigOwnerValService;
import com.qiuxs.gconfig.service.impl.ScGconfigService;

@Service
public class ScGconfigCombService {

	@Resource
	private ScGconfigService scGconfigService;
	@Resource
	private ScGconfigOptionsService scGconfigOptionsService;
	@Resource
	private ScGconfigOwnerValService scGconfigOwnerValService;

	/**
	 * 查询参数，并设置值为当前合适的值
	 *  
	 * @author qiuxs  
	 * @param params
	 * @param pageInfo
	 * @return
	 */
	public List<ScGconfig> findGconfigs(Map<String, Object> params, PageInfo pageInfo) {
		List<ScGconfig> gconfigs = this.scGconfigService.findByMap(params, pageInfo);
		for (ScGconfig gc : gconfigs) {
			ScGconfigOwnerVal ownerVal = this.getOwnerValue(gc.getCode());
			
			if (ownerVal != null) {
				gc.setVal(ownerVal.getVal());
				gc.setCreatedBy(ownerVal.getCreatedBy());
				gc.setCreatedTime(ownerVal.getCreatedTime());
				gc.setUpdatedBy(ownerVal.getUpdatedBy());
				gc.setUpdatedTime(ownerVal.getUpdatedTime());
			}

			gc.setValCaption(this.getCaption(gc.getDomain(), gc.getInputType(), gc.getCode(), gc.getVal()));
		}
		return gconfigs;
	}

	/**
	 * 根据code获取参数
	 *  
	 * @author qiuxs  
	 * @param code
	 * @return
	 */
	public GConfigDTO getGConfigByCode(String domain, String code) {
		ScGconfig scGconfig = this.scGconfigService.getByUk(domain, code);
		if (scGconfig == null) {
			ExceptionUtils.throwLogicalException("gconfig_not_exists", domain, code);
		}
		GConfigDTO dto = new GConfigDTO();
		dto.setCode(code);
		dto.setDomian(scGconfig.getDomain());
		dto.setInputType(scGconfig.getInputType());
		
		ScGconfigOwnerVal ownerValue = this.getOwnerValue(code);
		if (ownerValue != null) {
			dto.setVal(ownerValue.getVal());
		} else {
			dto.setVal(scGconfig.getVal());
		}
		
		dto.setValCaption(this.getCaption(dto.getDomian(), dto.getInputType(), code, dto.getVal()));
		
		if (dto.getInputType() == ScGconfig.INPUT_TYPE_MUL_SELECT || dto.getInputType() == ScGconfig.INPUT_TYPE_SELGIN_SELECT) {
			List<ScGconfigOptions> options = this.scGconfigOptionsService.findByMap(MapUtils.genMap("code", dto.getCode()));
			List<GConfigOptions> opts = new ArrayList<>(options.size());
			if (CollectionUtils.isNotEmpty(options)) {
				for (ScGconfigOptions option : options) {
					GConfigOptions opt = new GConfigOptions();
					opt.setCode(option.getCode());
					opt.setName(option.getName());
					opt.setVal(option.getOptVal());
					opts.add(opt);
				}
			}
			dto.setOpts(opts);
		}
		
		return dto;
	}
	
	/**
	 * 保存参数配置
	 * 
	 *  {
     *       "domain": "system",
     *       "values": [
     *           {
     *               "ownerType": 1,
     *               "ownerId": 2,
     *               "code": "xxx",
     *               "val": "xxx"
     *           }
     *       ]
     *   }
	 *  
	 * @author qiuxs  
	 * @param params
	 */
	@Transactional
	public void saveConfigOwnerValue(String domain, JSONArray values) {
		for (int i = 0; i < values.size(); i++) {
			JSONObject config = values.getJSONObject(i);
			String code = config.getString("code");
			String val = config.getString("val");
			Integer ownerType = config.getInteger("ownerType");
			Long ownerId = config.getLong("ownerId");
			ScGconfig scGconfig = this.scGconfigService.getByUk(domain, code);
			if (scGconfig == null) {
				ExceptionUtils.throwLogicalException("gconfig_not_exists", domain, code);
			}
			
			AsyncTaskExecutor.execute(new RunnableAsyncTask<Object>(null) {
				@Override
				public void execute(Object preparParam) {
					// 失效缓存
					GConfigClientUtils.invalidCache(domain, ownerType, ownerId, code);
				}
				
			}, true);
			
			if ((ownerType & ScGconfigOwnerVal.OWNER_TYPE_SYSTEM) > 0) {
				// 系统级别
				// 直接修改参数原表
				ScGconfig updateBean = new ScGconfig();
				updateBean.setId(scGconfig.getId());
				updateBean.setVal(val);
				this.scGconfigService.updateDirect(updateBean);
				continue;
			}
			
			ScGconfigOwnerVal ownerValue = new ScGconfigOwnerVal();
			ownerValue.setCode(code);
			ownerValue.setOwnerType(ownerType);
			ownerValue.setOwnerId(ownerId);
			ownerValue.setVal(val);
			this.scGconfigOwnerValService.save(ownerValue);
		}
	}
	
	private ScGconfigOwnerVal getOwnerValue(String code) {
		ScGconfigOwnerVal ownerVal = null; 
		Long userId = UserContext.getUserIdOpt();
		if (userId != null) {
			ownerVal = this.scGconfigOwnerValService.getByUk(code, ScGconfigOwnerVal.OWNER_TYPE_USER, userId);
		}
		if (ownerVal == null) {
			Long roleId = UserContext.getRoleIdOpt();
			if (NumberUtils.isNotEmpty(roleId)) {
				ownerVal = this.scGconfigOwnerValService.getByUk(code, ScGconfigOwnerVal.OWNER_TYPE_ROLE, roleId);
			}
		}
		return ownerVal;
	}
	
	private String getCaption(String domain, int inputType, String code, String val) {
		if (inputType == ScGconfig.INPUT_TYPE_SELGIN_SELECT) {
			ScGconfigOptions options = this.scGconfigOptionsService.getByUk(domain, code, val);
			return options.getName();
		} else if (inputType == ScGconfig.INPUT_TYPE_MUL_SELECT) {
			String sval = val;
			String[] split = sval.split(",");
			StringBuilder sbCaption = new StringBuilder();
			for (String singleVal : split) {
				ScGconfigOptions options = this.scGconfigOptionsService.getByUk(domain, code, singleVal);
				sbCaption.append(options.getName());
			}
			return sbCaption.toString();
		} else {
			return val;
		}

	}

}
