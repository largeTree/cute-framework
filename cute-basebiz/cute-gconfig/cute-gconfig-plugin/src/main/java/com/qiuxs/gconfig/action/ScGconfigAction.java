package com.qiuxs.gconfig.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.core.basic.utils.JsonUtils;
import com.qiuxs.cuteframework.core.persistent.database.dao.page.PageInfo;
import com.qiuxs.cuteframework.core.persistent.database.dao.page.PageSettings;
import com.qiuxs.cuteframework.web.action.BaseAction;
import com.qiuxs.cuteframework.web.bean.ActionResult;
import com.qiuxs.cuteframework.web.bean.ReqParam;
import com.qiuxs.gconfig.dao.ScGconfigDao;
import com.qiuxs.gconfig.entity.ScGconfig;
import com.qiuxs.gconfig.service.IScGconfigService;
import com.qiuxs.gconfig.service.comb.ScGconfigCombService;

/**
 * 全局配置Action
 *
 * 创建时间 ：2020-03-04 10:37:13
 * @author qiuxs
 * 
 */
@Service
public class ScGconfigAction extends BaseAction<Long, ScGconfig, ScGconfigDao, IScGconfigService> {

	@Resource
	private IScGconfigService scgconfigService;

	@Resource
	private ScGconfigCombService scGconfigCombService;

	/**
	 * 查询系统配置
	 *  
	 * @author qiuxs  
	 * @param params
	 * @return
	 */
	public ActionResult findGConfigs(ReqParam params, String jsonData) {
		PageInfo pageInfo = PageSettings.preparePageInfo(params);
		List<ScGconfig> gconfigs = this.scGconfigCombService.findGconfigs(JsonUtils.parseJSONObject(jsonData), pageInfo);
		return super.responseList(params.getWrapper(), gconfigs, pageInfo.getTotal(), pageInfo.getSumrow());
	}
	
	/**
	 * 获取单个全局配置详情
	 *  
	 * @author qiuxs  
	 * @param params
	 * @return
	 */
	public ActionResult getGConfig(ReqParam params) {
		return super.get(params);
	}

	@Override
	protected IScGconfigService getService() {
		return this.scgconfigService;
	}

}
