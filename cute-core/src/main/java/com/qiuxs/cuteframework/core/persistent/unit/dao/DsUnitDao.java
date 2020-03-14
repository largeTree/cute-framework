package com.qiuxs.cuteframework.core.persistent.unit.dao;

import com.qiuxs.cuteframework.core.persistent.database.dao.IBaseUKDao;
import com.qiuxs.cuteframework.tech.mybatis.dao.MyBatisRepository;
import com.qiuxs.cuteframework.core.persistent.unit.entity.DsUnit;

/**
 * 单元对应数据库关系Dao接口
 * 
 * 创建时间 ：2020-03-10 09:48:14
 * @author qiuxs
 *
 */
@MyBatisRepository
public interface DsUnitDao extends IBaseUKDao<Long, DsUnit> {

	public long hasDsUnit();

}
