package com.qiuxs.cuteframework.core.persistent.mybatis;

import org.apache.ibatis.mapping.MappedStatement;

/**
 * 基础sql生成器
 * @author qiuxs
 *
 */
public class BaseMyBatisSelectProvider {

	public String get(MappedStatement ms) {
		return "select * from user where id = #{id}";
	}

}
