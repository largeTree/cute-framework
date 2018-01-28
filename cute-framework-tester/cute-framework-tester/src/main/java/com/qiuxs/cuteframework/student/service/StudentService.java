package com.qiuxs.cuteframework.student.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.core.basic.bean.BaseField;
import com.qiuxs.cuteframework.core.basic.bean.ViewProperty;
import com.qiuxs.cuteframework.core.service.impl.AbstractDataPropertyService;
import com.qiuxs.cuteframework.student.dao.StudentDao;
import com.qiuxs.cuteframework.student.entity.Student;

@Service("StudentService")
public class StudentService extends AbstractDataPropertyService<Long, Student, StudentDao> {

	@Resource
	private StudentDao dao;

	public StudentService() {
		super();
		super.setDescription("学生");
		super.setTableName("student");
		super.setEntityClass(Student.class);
	}

	@Override
	protected StudentDao getDao() {
		return this.dao;
	}

	@Override
	protected void initInnerProperty(Map<String, ViewProperty<?>> properties) {
		properties.put("id", new ViewProperty<>(new BaseField("id", "主键", "Long"), null));
		properties.put("name", new ViewProperty<>(new BaseField("name", "姓名", "String"), null));
		properties.put("age", new ViewProperty<>(new BaseField("age", "年龄", "Integer"), null));
	}

}
