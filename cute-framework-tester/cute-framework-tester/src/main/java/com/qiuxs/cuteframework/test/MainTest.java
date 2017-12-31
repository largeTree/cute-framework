package com.qiuxs.cuteframework.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.qiuxs.cuteframework.core.service.filter.IInsertFilter;
import com.qiuxs.cuteframework.core.service.filter.impl.IdGenerateFilter;
import com.qiuxs.cuteframework.student.entity.Student;
import com.qiuxs.cuteframework.student.service.StudentService;

/**
 * Hello world!
 *
 */
public class MainTest {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		StudentService studentSvc = (StudentService) context.getBean("StudentService");
		Student bean = new Student();
		IInsertFilter<Long, Student> idFilter = new IdGenerateFilter<Long, Student>("student");
		idFilter.preInsert(bean);
		bean.setName("名字");
		bean.setAge(18);
		studentSvc.insert(bean);
		context.close();
	}
}
