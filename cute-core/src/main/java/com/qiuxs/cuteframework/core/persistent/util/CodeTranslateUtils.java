package com.qiuxs.cuteframework.core.persistent.util;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.aop.support.AopUtils;

import com.qiuxs.cuteframework.core.basic.code.provider.ICodeTranslatable;
import com.qiuxs.cuteframework.core.basic.utils.reflect.MethodUtils;

public class CodeTranslateUtils {
	
	/**
	 * 获取编码类型
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public static Class<?> getCodeType(ICodeTranslatable<?> codeTranslate) {
		// 获取参数类型
		List<Method> methods = MethodUtils.getMethods(AopUtils.getTargetClass(codeTranslate), "getCaption", true);
		Class<?> paramType = null;
		for (Method mth : methods) {
			Class<?>[] types = mth.getParameterTypes();
			if (types.length > 0) {
				Class<?> type = types[0];
				if (!type.equals(Object.class)) {
					paramType = types[0];
					break;
				}
			}
		}
		return paramType;
	}

}
