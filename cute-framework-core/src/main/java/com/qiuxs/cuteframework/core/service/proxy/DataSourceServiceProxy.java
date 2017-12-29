package com.qiuxs.cuteframework.core.service.proxy;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.core.basic.ex.ExceptionCode;
import com.qiuxs.cuteframework.core.basic.ex.LogicalException;

@Aspect
@Component
public class DataSourceServiceProxy {

	@AfterThrowing("execution(* **.service.*.*(..))")
	public void modifyAfterThrowing(Object target, Throwable t) {
		if (t instanceof LogicalException) {
			throw (LogicalException) t;
		} else if (t instanceof DuplicateKeyException) {
			// 唯一索引冲突异常
			String msg = "";
			throw new LogicalException(ExceptionCode.DuplicateKey, msg);
		} else {
			throw new RuntimeException(t.getLocalizedMessage(), t);
		}
	}
}
