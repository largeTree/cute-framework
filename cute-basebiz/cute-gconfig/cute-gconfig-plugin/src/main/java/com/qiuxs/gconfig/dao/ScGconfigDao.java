package com.qiuxs.gconfig.dao;

import com.qiuxs.cuteframework.core.persistent.database.dao.IBaseUKDao;
import com.qiuxs.cuteframework.tech.mybatis.dao.MyBatisRepository;
import com.qiuxs.gconfig.entity.ScGconfig;

/**
 * 全局配置Dao接口
 * 
 * 创建时间 ：2020-03-04 10:37:13
 * @author qiuxs
 *
 */
@MyBatisRepository
public interface ScGconfigDao extends IBaseUKDao<Long, ScGconfig> {

}
