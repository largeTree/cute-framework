package com.qiuxs.cuteframework.tech.spring.tx;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

public class CuteTransactionInterceptor extends TransactionInterceptor {

	private static final long serialVersionUID = 5661136630296337304L;

	public CuteTransactionInterceptor(PlatformTransactionManager platformTransactionManager,
			AnnotationTransactionAttributeSource attributes) {
		super(platformTransactionManager, attributes);
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		return super.invoke(invocation);
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
