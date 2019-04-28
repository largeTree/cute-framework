package com.qiuxs.cuteframework.core.basic.utils.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

@Retention(RUNTIME)
@Target(FIELD)
public @interface XMLListItem {
	
	String value();
	
	@SuppressWarnings("rawtypes")
	Class<? extends List> listType() default ArrayList.class;
	
}
