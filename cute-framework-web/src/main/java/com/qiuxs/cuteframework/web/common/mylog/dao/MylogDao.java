package com.qiuxs.cuteframework.web.common.mylog.dao;

import org.springframework.stereotype.Repository;

import com.qiuxs.cuteframework.core.persistent.database.dao.IBaseDao;
import com.qiuxs.cuteframework.web.common.mylog.entity.Mylog;

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
