package com.qiuxs.gconfig.service;

import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDataPropertyService;
import com.qiuxs.gconfig.entity.ScGconfig;


/**
 * 全局配置服务接口
 * 
 * 创建时间 ：2020-03-04 10:37:13
 * @author qiuxs
 */
public interface IScGconfigService extends IDataPropertyService<Long, ScGconfig> {

	/**
	 * 根据唯一约束获取一行
	 *  
	 * @author qiuxs  
	 * @param domain
	 * @param code
	 * @return
	 */
	public ScGconfig getByUk(String domain, String code);
}
