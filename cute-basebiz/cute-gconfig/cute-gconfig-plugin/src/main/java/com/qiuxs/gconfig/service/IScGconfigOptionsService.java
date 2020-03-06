package com.qiuxs.gconfig.service;

import com.qiuxs.gconfig.dao.ScGconfigOptionsDao;
import com.qiuxs.gconfig.entity.ScGconfigOptions;

import java.util.List;

import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDataPropertyService;


/**
 * 全局配置选项服务接口
 * 
 * 创建时间 ：2020-03-04 10:23:40
 * @author qiuxs
 */
public interface IScGconfigOptionsService extends IDataPropertyService<Long, ScGconfigOptions, ScGconfigOptionsDao> {

	/**
	 * 根据code查询所有的选项
	 *  
	 * @author qiuxs  
	 * @param code
	 * @return
	 */
	public List<ScGconfigOptions> getByCode(String code);

}
