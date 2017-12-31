package com.qiuxs.cuteframework.student.dao;

import com.qiuxs.cuteframework.core.dao.IBaseDao;
import com.qiuxs.cuteframework.core.dao.mybatis.MyBatisRepository;
import com.qiuxs.cuteframework.student.entity.Student;

@MyBatisRepository
public interface StudentDao extends IBaseDao<Long, Student> {

}
