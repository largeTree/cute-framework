package com.qiuxs.cuteframework.tech.spring.tx;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import com.qiuxs.cuteframework.core.tx.local.SpringTxContext;

/**
 * 
 * @author qiuxs
 *
 */
public class CuteTransactionInterceptor extends TransactionInterceptor {

	private static final long serialVersionUID = 5661136630296337304L;

	public CuteTransactionInterceptor(PlatformTransactionManager platformTransactionManager,
			AnnotationTransactionAttributeSource attributes) {
		super(platformTransactionManager, attributes);
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		// 保存事务状态
		SpringTxContext.saveTxContext(invocation);
		// 获取真实的目标类
		Class<?> targetClass = (invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null);
		return invokeWithinTransaction(invocation.getMethod(), targetClass, new InvocationCallback() {

			@Override
			public Object proceedWithInvocation() throws Throwable {
				Object result = invocation.proceed();
				return result;
			}
		});
	}

	@Override
	protected void commitTransactionAfterReturning(TransactionInfo txInfo) {
		SpringTxContext.handleTransactionSynchronization();
		super.commitTransactionAfterReturning(txInfo);
	}

	@Override
	protected void completeTransactionAfterThrowing(TransactionInfo txInfo, Throwable ex) {
		SpringTxContext.handleTransactionSynchronization();
		super.completeTransactionAfterThrowing(txInfo, ex);
	}

}
