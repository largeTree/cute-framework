package com.qiuxs.cuteframework.web.log.dao;

import com.qiuxs.cuteframework.core.persistent.database.dao.IBaseDao;
import com.qiuxs.cuteframework.tech.mybatis.dao.MyBatisRepository;
import com.qiuxs.cuteframework.web.log.entity.RequestLog;

/**
 * 请求日志记录Dao接口
 * 
 * 创建时间 ：2019-05-30 22:26:46
 * @author qiuxs
 *
 */
@MyBatisRepository
public interface RequestLogDao extends IBaseDao<Long, RequestLog> {

}
