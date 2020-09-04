package com.qiuxs.gconfig.service;

import com.qiuxs.gconfig.dao.ScGconfigOwnerValDao;
import com.qiuxs.gconfig.entity.ScGconfigOwnerVal;
import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDataPropertyService;


/**
 * 全局配置所有者的值服务接口
 * 
 * 创建时间 ：2020-03-04 10:23:40
 * @author qiuxs
 */
public interface IScGconfigOwnerValService extends IDataPropertyService<Long, ScGconfigOwnerVal, ScGconfigOwnerValDao> {

	/**
	 * 获取指定所有者的参数配置
	 *  
	 * @author qiuxs  
	 * @param ownerType
	 * @param userId
	 * @param code
	 * @return
	 */
	public ScGconfigOwnerVal getOwnerVal(String domain, int ownerType, Long userId, String code);

}
