package com.qiuxs.cuteframework.web.log.service;

import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDataPropertyService;
import com.qiuxs.cuteframework.web.log.dao.RequestLogDao;
import com.qiuxs.cuteframework.web.log.entity.RequestLog;


/**
 * 请求日志记录服务接口
 * 
 * 创建时间 ：2019-05-30 22:26:46
 * @author qiuxs
 */
public interface IRequestLogService extends IDataPropertyService<Long, RequestLog, RequestLogDao> {

}
