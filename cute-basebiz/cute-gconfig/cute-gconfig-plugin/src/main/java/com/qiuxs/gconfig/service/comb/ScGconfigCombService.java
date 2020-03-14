package com.qiuxs.gconfig.service.comb;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.core.context.UserContext;
import com.qiuxs.cuteframework.core.persistent.database.dao.page.PageInfo;
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
		Long userId = UserContext.getUserIdOpt();
		for (ScGconfig gc : gconfigs) {
			ScGconfigOwnerVal ownerVal = this.scGconfigOwnerValService.getByUk(gc.getCode(), ScGconfigOwnerVal.OWNER_TYPE_USER, userId);
			if (ownerVal != null) {
				gc.setVal(ownerVal.getVal());
				gc.setCreatedBy(ownerVal.getCreatedBy());
				gc.setCreatedTime(ownerVal.getCreatedTime());
				gc.setUpdatedBy(ownerVal.getUpdatedBy());
				gc.setUpdatedTime(ownerVal.getUpdatedTime());
			}

			if (gc.getInputType() == ScGconfig.INPUT_TYPE_SELGIN_SELECT) {
				ScGconfigOptions options = this.scGconfigOptionsService.getByUk(gc.getCode(), gc.getVal());
				gc.setValCaption(options.getName());
			} else if (gc.getInputType() == ScGconfig.INPUT_TYPE_MUL_SELECT) {
				String sval = gc.getVal();
				String[] split = sval.split(",");
				StringBuilder sbCaption = new StringBuilder();
				for (String val : split) {
					ScGconfigOptions options = this.scGconfigOptionsService.getByUk(gc.getCode(), val);
					sbCaption.append(options.getName());
				}
				gc.setValCaption(sbCaption.toString());
			} else {
				gc.setValCaption(gc.getVal());
			}
		}
		return gconfigs;
	}

}
