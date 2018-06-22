package com.qiuxs.cuteframework.tech.spring.tx;

import org.springframework.transaction.support.TransactionSynchronization;

/**
 * 事务完成之后触发其中一个方法
 * 提交成功：commited
 * 提交失败：rolledBack
 * 状态未知：unkonw
 * 
 * 特别注意：线程复用的情况下，不可在下面的事件中操作数据库，否则将导致再次使用此线程时无法获取到数据库连接
 * 原因：
 * 		在下面的事件中再次获取到的连接将被缓存到{@link org.springframework.transaction.support.TransactionSynchronizationManager}}中
 * 		但由于此时事务已经提交或回滚，所以在操作完成之后Spring不会再将上述类中的连接清除
 * 		在后续的操作中将会获取到一个isClosed=false,但_conn=null的链接
 * 		获取连接连接参见{@link org.springframework.jdbc.datasource.DataSourceUtils#doGetConnection(javax.sql.DataSource)}
 * 
 * @author qiuxs
 *
 * @param <P>
 */
public abstract class AfterCompletionCallback<P> {

	private P preparParam;

	public AfterCompletionCallback(P preparParam) {
		this.preparParam = preparParam;
	}

	void call(int status) {
		switch (status) {
		case TransactionSynchronization.STATUS_COMMITTED:
			this.commited(this.preparParam);
			break;
		case TransactionSynchronization.STATUS_ROLLED_BACK:
			this.rolledBack(this.preparParam);
			break;
		default:
			this.unkonw(this.preparParam);
			break;
		}
	}

	public void commited(P preparParam) {
	}

	public void rolledBack(P preparParam) {
	}

	public void unkonw(P preparParam) {
	}
}
