package com.qiuxs.cuteframework.web.common.biz.func.dao;

import org.springframework.stereotype.Repository;

import com.qiuxs.cuteframework.core.persistent.database.dao.IBaseDao;
import com.qiuxs.cuteframework.web.common.biz.func.entity.Func;

/**
 * 功能菜单表Dao接口
 * 
 * 创建时间 ：2019-07-11 22:36:22
 * @author qiuxs
 *
 */
@Repository
public interface FuncDao extends IBaseDao<String, Func> {

}
