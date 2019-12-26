package com.qiuxs.cuteframework.web.common.mylog.service;

import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDataPropertyService;
import com.qiuxs.cuteframework.web.common.mylog.dao.MylogDao;
import com.qiuxs.cuteframework.web.common.mylog.entity.Mylog;


/**
 * 服务接口
 * 
 * 创建时间 ：2019-07-25 22:32:08
 * @author qiuxs
 */
public interface IMylogService extends IDataPropertyService<Long, Mylog, MylogDao> {

}
