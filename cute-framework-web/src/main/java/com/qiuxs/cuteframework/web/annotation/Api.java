package com.qiuxs.cuteframework.web.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
public @interface Api {

	/** apiKey */
	String value();

	/** api描述 */
	String desc() default "";

	/** 是否需要会话验证 */
	boolean login() default true;

	/** 是否需要授权认证 */
	boolean authFlag() default false;

}
