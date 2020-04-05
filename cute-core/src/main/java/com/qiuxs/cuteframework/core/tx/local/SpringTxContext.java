package com.qiuxs.cuteframework.core.tx.local;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.context.TLVariableHolder;
import com.qiuxs.cuteframework.core.persistent.database.lookup.DataSourceContext;
import com.qiuxs.cuteframework.core.persistent.database.lookup.DynamicDataSource;
import com.qiuxs.cuteframework.core.tx.TxConfrimUtils;

/**
 * Spring事务上下文
 * @author qiuxs
 *
 */
public class SpringTxContext {

	private static Logger log = LogManager.getLogger(SpringTxContext.class);

	/** 事务监听器初始化状态 */
	private static final String TL_INITED_KEY = "stx_synchronization_init_flag";
	/** 事务方法执行缓存 */
	private static final String TL_TX_METHOD_INVOCATION = "stx_method_invocation";
	/** 事务数据源ID */
	private static final String TL_TX_DSID = "stx_tx_dsId";
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
		DataSource dataSource = DynamicDataSource.getDynamicDataSource();
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
		TransactionSynchronizationAdapter synchronization = new TransactionSynchronizationAdapter() {
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
				try {
					if (status == TransactionSynchronization.STATUS_COMMITTED) {
						TxConfrimUtils.commit();
					} else if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
						TxConfrimUtils.rollback();
					}
				} catch (Throwable e) {
					log.error("TxConfrim commit or rollback failed, ext = " + e.getLocalizedMessage(), e);
				}
				List<AfterCompletionRunnable<?>> afterCompletionCallbacks = getAfterCompletionRunnables();
				if (afterCompletionCallbacks.size() > 0) {
					afterCompletionCallbacks.forEach(callback -> {
						try {
							callback.call(status);
						} catch (Throwable e) {
							log.error("call AfterCompletionCallback[" + callback.getClass().getName() 
									+ "] failed, ext = " + e.getLocalizedMessage(), e);
						}
					});
				}
				clearTxContext();
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

	/**
	 * 保存事务状态，并设置事务数据源
	 * @param invocation
	 */
	public static void saveTxContext(MethodInvocation invocation) {
		if (getTxMethodInvocation() != null) {
			return; // 一个大事务会调用多次service的方法，但只要记住第一个就行了，事务开始时使用的数据源
		}
		setTxMethodInvocation(invocation);
		Object svc = invocation.getThis();
		String dsId = null;
		if (svc != null && DataSourceContext.isDsSwitchAuto()) {
			dsId = DataSourceContext.getDsIdBySvc(svc);
		} else {
			dsId = DataSourceContext.getDsId();
		}

		// 保存事务数据源ID
		setTxDsId(dsId);

		if (dsId != null) {
			// 设置当前线程需要的事务id
			DataSourceContext.setUpDs(dsId);
		}
	}

	/**
	 * 判断当前数据源是否等于事务数据源
	 * @return
	 */
	public static boolean isTxDsId() {
		String curDsId = DataSourceContext.getDsId();
		String txDsId = getTxDsId();
		if (StringUtils.equals(curDsId, txDsId)) {
			if (log.isDebugEnabled()) {
				log.debug("curDsId = " + curDsId + ", txDsId = " + txDsId);
			}
			return true;
		} else {
			DynamicDataSource dds = DynamicDataSource.getDynamicDataSource();
			String entryDb = dds.getEntryDb();
			if (log.isDebugEnabled()) {
				log.debug("curDsId = " + curDsId + ", txDsId = " + txDsId + ", entryDsId = " + entryDb);
			}
			if (curDsId == null && entryDb.equals(txDsId)) {
				return true;
			} else if (txDsId == null && entryDb.equals(curDsId)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取事务数据源ID
	 * @return
	 */
	public static String getTxDsId() {
		return TLVariableHolder.getVariable(TL_TX_DSID);
	}

	/**
	 * 保存事务数据源ID
	 * @param dsId
	 */
	private static void setTxDsId(String dsId) {
		if (log.isDebugEnabled()) {
			log.debug("Tx DsId = " + dsId);
		}
		TLVariableHolder.setVariable(TL_TX_DSID, dsId);
	}

	/**
	 * 保存事务方法执行上下文
	 * @param invocation
	 */
	public static void setTxMethodInvocation(MethodInvocation invocation) {
		TLVariableHolder.setVariable(TL_TX_METHOD_INVOCATION, invocation);
	}

	/**
	 * 获取事务方法执行上下文
	 * @return
	 */
	public static MethodInvocation getTxMethodInvocation() {
		return TLVariableHolder.getVariable(TL_TX_METHOD_INVOCATION);
	}

	public static void clearTxContext() {
		TLVariableHolder.removeVariable(TL_TX_METHOD_INVOCATION);
		TLVariableHolder.removeVariable(TL_AFTER_COMPLETION_HANDLE);
		TLVariableHolder.removeVariable(TL_TX_DSID);
		TLVariableHolder.removeVariable(TL_AFTER_COMMIT_HANDLE);
		TLVariableHolder.removeVariable(TL_INITED_KEY);
	}
}
