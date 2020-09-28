package com.qiuxs.cuteframework.web.action.cache;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.qiuxs.cuteframework.core.basic.utils.TimeUtils;

@Retention(RUNTIME)
@Target(METHOD)
public @interface ActionCache {

	/** 缓存Keys */
	String[] keys();

	/** 有效时间 */
	long expiresIn() default TimeUtils.SECONDS * 30;
	
}
