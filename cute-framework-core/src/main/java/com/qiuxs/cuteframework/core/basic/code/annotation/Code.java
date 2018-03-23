package com.qiuxs.cuteframework.core.basic.code.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 单个编码
 * @author qiuxs
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface Code {
	/**
	 * 所属编码集
	 * @return
	 */
	String domain();

	/**
	 * 说明文字
	 * @return
	 */
	String caption();
}
