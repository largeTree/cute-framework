package com.qiuxs.gconfig.dao;

import com.qiuxs.cuteframework.core.persistent.database.dao.IBaseUKDao;
import com.qiuxs.cuteframework.tech.mybatis.dao.MyBatisRepository;
import com.qiuxs.gconfig.entity.ScGconfigOptions;

/**
 * 全局配置选项Dao接口
 * 
 * 创建时间 ：2020-03-04 10:23:40
 * @author qiuxs
 *
 */
@MyBatisRepository
public interface ScGconfigOptionsDao extends IBaseUKDao<Long, ScGconfigOptions> {

}
