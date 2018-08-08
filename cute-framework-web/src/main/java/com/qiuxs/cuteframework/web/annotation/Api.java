package com.qiuxs.cuteframework.web.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
public @interface Api {

	String value() default "";

	String desc() default "";

	boolean loginFlag() default true;

	boolean authFlag() default false;

}
