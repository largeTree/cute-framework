package com.qiuxs.cuteframework.core.persistent.unit.service;

import com.qiuxs.cuteframework.core.persistent.unit.dao.DsUnitDao;
import com.qiuxs.cuteframework.core.persistent.unit.entity.DsUnit;
import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDataPropertyService;


/**
 * 单元对应数据库关系服务接口
 * 
 * 创建时间 ：2020-03-10 09:48:14
 * @author qiuxs
 */
public interface IDsUnitService extends IDataPropertyService<Long, DsUnit, DsUnitDao> {

	public boolean hasDsUnit();

}
