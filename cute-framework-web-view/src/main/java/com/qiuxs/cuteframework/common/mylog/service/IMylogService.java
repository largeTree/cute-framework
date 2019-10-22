package com.qiuxs.cuteframework.common.mylog.service;

import com.qiuxs.cuteframework.common.mylog.dao.MylogDao;
import com.qiuxs.cuteframework.common.mylog.entity.Mylog;
import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDataPropertyService;


/**
 * 服务接口
 * 
 * 创建时间 ：2019-07-25 22:32:08
 * @author qiuxs
 */
public interface IMylogService extends IDataPropertyService<Long, Mylog, MylogDao> {

}
