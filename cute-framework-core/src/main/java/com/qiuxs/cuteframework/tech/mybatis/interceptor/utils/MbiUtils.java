package com.qiuxs.cuteframework.tech.mybatis.interceptor.utils;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import com.qiuxs.cuteframework.core.basic.utils.StringUtils;

public class MbiUtils {

	public static Object getTarget(Object proxy) {
		MetaObject metaObject = getMetaObject(proxy);
		return metaObject.getOriginalObject();
	}

	public static MetaObject getMetaObject(Object proxy) {
		MetaObject metaObject = SystemMetaObject.forObject(proxy);
		// 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环可以分离出最原始的的目标类)
		while (metaObject.hasGetter("h")) {
			Object object = metaObject.getValue("h");
			metaObject = SystemMetaObject.forObject(object);
		}
		// 分离最后一个代理对象的目标类
		while (metaObject.hasGetter("target")) {
			Object object = metaObject.getValue("target");
			metaObject = SystemMetaObject.forObject(object);
		}
		return metaObject;
	}

	public static String getNameSpace(MappedStatement ms) {
		return StringUtils.substringBeforeLast(ms.getId(), ".");

	}

	public static String getSqlId(MappedStatement ms) {
		return StringUtils.substringAfterLast(ms.getId(), ".");
	}

}
