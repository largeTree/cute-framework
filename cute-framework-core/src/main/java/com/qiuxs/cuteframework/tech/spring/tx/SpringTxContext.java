package com.qiuxs.cuteframework.tech.spring.tx;

import java.util.ArrayList;
import java.util.List;

import javax.activation.DataSource;

import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;
import com.qiuxs.cuteframework.core.context.TLVariableHolder;

/**
 * Spring事务上下文
 * @author qiuxs
 *
 */
public class SpringTxContext {

	/** 事务监听器初始化状态 */
	private static final String TL_INITED_KEY = "stx_synchronization_init_flag";
	/** 提交之后操作，不论提交是否成功 */
	private static final String TL_AFTER_COMMIT_HANDLE = "stx_after_commit";
	/** 事务完成之后操作，提交成功调用commited,失败调用rolledBack,状态未知调用unkonw */
	private static final String TL_AFTER_COMPLETION_HANDLE = "stx_after_completion";

	/**
	 * 当前是否在事务环境中
	 * 
	 * @return
	 */
	public static boolean isTransactional() {
		DataSource dataSource = ApplicationContextHolder.getBean(DataSource.class);
		ConnectionHolder conHolder = (ConnectionHolder) TransactionSynchronizationManager.getResource(dataSource);
		if (conHolder != null && conHolder.getConnectionHandle() != null) {
			return true;
		}
		return false;
	}

	/**
	 * 注册本地Spring事务事件
	 */
	public static void handleTransactionSynchronization() {
		// 不重复注册
		if (isInited()) {
			return;
		}
		TransactionSynchronization synchronization = new TransactionSynchronization() {
			@Override
			public void afterCommit() {
				List<AfterCommitRunnable<?>> afterCommitCallbackList = getAfterCommitRunnables();
				if (afterCommitCallbackList.size() > 0) {
					afterCommitCallbackList.forEach(callback -> {
						callback.call();
					});
				}
			}

			@Override
			public void afterCompletion(int status) {
				List<AfterCompletionRunnable<?>> afterCompletionCallbacks = getAfterCompletionRunnables();
				if (afterCompletionCallbacks.size() > 0) {
					afterCompletionCallbacks.forEach(callback -> {
						callback.call(status);
					});
				}
			}
		};
		TransactionSynchronizationManager.registerSynchronization(synchronization);
		// 设置为已注册
		TLVariableHolder.setVariable(TL_INITED_KEY, true);
	}

	/**
	 * 添加一个事务提交后事件，可区分事务是否提交成功
	 * @param afterCompletion
	 */
	public static void addAfterCompletionRunnable(AfterCompletionRunnable<?> afterCompletion) {
		getAfterCompletionRunnables().add(afterCompletion);
	}

	private static List<AfterCompletionRunnable<?>> getAfterCompletionRunnables() {
		List<AfterCompletionRunnable<?>> list = TLVariableHolder.getVariable(TL_AFTER_COMPLETION_HANDLE);
		if (list == null) {
			list = new ArrayList<>();
			TLVariableHolder.setVariable(TL_AFTER_COMPLETION_HANDLE, list);
		}
		return list;
	}

	/**
	 * 添加一个事务提交后事件
	 * 提交后执行，不论是否提交成功
	 * @param afterCommitRunnable
	 */
	public static void addAfterCommitRunnable(AfterCommitRunnable<?> afterCommitRunnable) {
		getAfterCommitRunnables().add(afterCommitRunnable);
	}

	private static List<AfterCommitRunnable<?>> getAfterCommitRunnables() {
		List<AfterCommitRunnable<?>> list = TLVariableHolder.getVariable(TL_AFTER_COMMIT_HANDLE);
		if (list == null) {
			list = new ArrayList<>();
			TLVariableHolder.setVariable(TL_AFTER_COMMIT_HANDLE, list);
		}
		return list;
	}

	/**
	 * 是否已经注册过
	 * 
	 * @return
	 */
	private static boolean isInited() {
		Boolean inited = TLVariableHolder.getVariable(TL_INITED_KEY);
		return inited != null && inited.booleanValue();
	}

}
