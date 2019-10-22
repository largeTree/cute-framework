package com.qiuxs.cuteframework.common.mylog.dao;

import org.springframework.stereotype.Repository;

import com.qiuxs.cuteframework.common.mylog.entity.Mylog;
import com.qiuxs.cuteframework.core.persistent.database.dao.IBaseDao;

/**
 * Dao接口
 * 
 * 创建时间 ：2019-07-25 22:32:08
 * @author qiuxs
 *
 */
@Repository
public interface MylogDao extends IBaseDao<Long, Mylog> {

}
