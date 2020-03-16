package com.qiuxs.cuteframework.core.persistent.util;

import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;
import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDGeneraterable;
import com.qiuxs.cuteframework.core.persistent.database.service.seq.IDGeneraterDatabase;
import com.qiuxs.cuteframework.core.persistent.redis.seq.IDGeneraterRedis;

public class IDGenerateUtil {

	public static final String SEQ_TYPE_DB = "db";

	public static final String SEQ_TYPE_REDIS = "redis";
	
	private static String seqType;

	public static Long getNextLongId(String tableName) {
		String seq_type = getSeqType();
		IDGeneraterable idGenerater = null;
		switch (seq_type) {
		case SEQ_TYPE_DB:
			idGenerater = ApplicationContextHolder.getBean(IDGeneraterDatabase.class);
			break;
		case SEQ_TYPE_REDIS:
			idGenerater = ApplicationContextHolder.getBean(IDGeneraterRedis.class);
			break;
		default:
			throw new IllegalArgumentException("unkonw seq_type");
		}
		return idGenerater.getNextId(tableName);
	}

	private static String getSeqType() {
		if (seqType == null) {
			seqType = EnvironmentContext.getSeqType();
		}
		return seqType;
	}

	
	
}
