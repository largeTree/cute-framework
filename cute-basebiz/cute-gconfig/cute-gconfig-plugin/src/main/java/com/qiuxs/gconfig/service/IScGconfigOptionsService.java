package com.qiuxs.gconfig.service;

import java.util.List;

import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDataPropertyService;
import com.qiuxs.gconfig.entity.ScGconfigOptions;


/**
 * 全局配置选项服务接口
 * 
 * 创建时间 ：2020-03-04 10:23:40
 * @author qiuxs
 */
public interface IScGconfigOptionsService extends IDataPropertyService<Long, ScGconfigOptions> {

	/**
	 * 根据code查询所有的选项
	 *  
	 * @author qiuxs  
	 * @param code
	 * @return
	 */
	public List<ScGconfigOptions> getByCode(String domain, String code);

}
