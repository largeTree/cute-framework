package com.qiuxs.cuteframework.student.entity;

import com.qiuxs.cuteframework.core.entity.impl.AbstractEntity;

public class Student extends AbstractEntity<Long> {

	private String name;
	private Integer age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

}
