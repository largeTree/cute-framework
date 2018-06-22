package com.qiuxs.cuteframework.tech.spring.tx;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.qiuxs.cuteframework.core.context.TLVariableHolder;

public class SpringTxContext {

	/** 事务监听器初始化状态 */
	private static final String TL_INITED_KEY = "stx_synchronization_init_flag";
	/** 提交之后操作，不论提交是否成功 */
	private static final String TL_AFTER_COMMIT_HANDLE = "stx_after_commit";
	/** 事务完成之后操作，提交成功调用commited,失败调用rolledBack,状态未知调用unkonw */
	private static final String TL_AFTER_COMPLETION_HANDLE = "stx_after_completion";
	
	/**
	 * 当前是否在事务环境中
	 * @return
	 */
	public static boolean isTransactional() {
		return false;
	}

	public static void handleTransactionSynchronization() {
		// 不重复注册
		if (isInited()) {
			return;
		}
		TransactionSynchronization synchronization = new TransactionSynchronization() {
			@Override
			public void afterCommit() {
				List<AfterCommitCallback<?>> afterCommitCallbackList = getAfterCommitCallbackList();
				if (afterCommitCallbackList.size() > 0) {
					afterCommitCallbackList.forEach(callback -> {
						callback.call();
					});
				}
			}

			@Override
			public void afterCompletion(int status) {
				List<AfterCompletionCallback<?>> afterCompletionCallbacks = getAfterCompletionCallback();
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

	private static List<AfterCompletionCallback<?>> getAfterCompletionCallback() {
		List<AfterCompletionCallback<?>> list = TLVariableHolder.getVariable(TL_AFTER_COMPLETION_HANDLE);
		if (list == null) {
			list = new ArrayList<>();
			TLVariableHolder.setVariable(TL_AFTER_COMPLETION_HANDLE, list);
		}
		return list;
	}

	private static List<AfterCommitCallback<?>> getAfterCommitCallbackList() {
		List<AfterCommitCallback<?>> list = TLVariableHolder.getVariable(TL_AFTER_COMMIT_HANDLE);
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
