package com.qiuxs.cuteframework.web.log.dao;

import org.springframework.stereotype.Repository;

import com.qiuxs.cuteframework.core.persistent.database.dao.IBaseDao;
import com.qiuxs.cuteframework.web.log.entity.ApiRequestLog;

/**
 * 请求日志记录Dao接口
 * 
 * 创建时间 ：2019-05-30 22:26:46
 * @author qiuxs
 *
 */
@Repository
public interface ApiRequestLogDao extends IBaseDao<Long, ApiRequestLog> {

}
