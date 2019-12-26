package com.qiuxs.cuteframework.web.common.mylog.dao;

import com.qiuxs.cuteframework.core.persistent.database.dao.IBaseDao;
import com.qiuxs.cuteframework.tech.mybatis.dao.MyBatisRepository;
import com.qiuxs.cuteframework.web.common.mylog.entity.Mylog;

/**
 * Dao接口
 * 
 * 创建时间 ：2019-07-25 22:32:08
 * @author qiuxs
 *
 */
@MyBatisRepository
public interface MylogDao extends IBaseDao<Long, Mylog> {

}
