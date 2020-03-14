package com.qiuxs.gconfig.dao;

import com.qiuxs.cuteframework.core.persistent.database.dao.IBaseUKDao;
import com.qiuxs.cuteframework.tech.mybatis.dao.MyBatisRepository;
import com.qiuxs.gconfig.entity.ScGconfigOwnerVal;

/**
 * 全局配置所有者的值Dao接口
 * 
 * 创建时间 ：2020-03-04 10:23:40
 * @author qiuxs
 *
 */
@MyBatisRepository
public interface ScGconfigOwnerValDao extends IBaseUKDao<Long, ScGconfigOwnerVal> {

}
