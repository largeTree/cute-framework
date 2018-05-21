package com.qiuxs.cuteframework.core.persistent.util;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.core.basic.config.EnvironmentHolder;
import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;
import com.qiuxs.cuteframework.core.persistent.service.IDGeneraterable;

@Component
public class IDGenerateUtil {

	@Resource
	private EnvironmentHolder envHolder;

	public static final String SEQ_TYPE_DB = "db";

	public static final String SEQ_TYPE_REDIS = "redis";

	public Object getNextId(String tableName) {
		String seq_type = envHolder.getEnvProp("seq_type");
		IDGeneraterable idGenerater = null;
		switch (seq_type) {
		case SEQ_TYPE_DB:
			idGenerater = ApplicationContextHolder.getBean("IDGeneraterDataSource");
			break;
		case SEQ_TYPE_REDIS:
			idGenerater = ApplicationContextHolder.getBean("IDGeneraterRedis");
			break;
		default:
			throw new IllegalArgumentException("unkonw seq_type");
		}
		return idGenerater.getNextId(tableName);
	}

}
