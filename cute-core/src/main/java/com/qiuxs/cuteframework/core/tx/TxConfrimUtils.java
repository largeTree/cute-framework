package com.qiuxs.cuteframework.core.tx;

import java.util.ArrayList;
import java.util.List;

import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;
import com.qiuxs.cuteframework.core.context.TLVariableHolder;

public class TxConfrimUtils {

	private static final String TO_DO_MQ_TX_IDS_KEY = "_rmq_client_todo_txids";
	
	public static final String MQ_TX_SERVICE_NAME = "mqTxService";

	private static IMQTxService mqTxService;

	private static IMQTxService getMqTxService() {
		if (mqTxService == null) {
			mqTxService = ApplicationContextHolder.getBean(MQ_TX_SERVICE_NAME);
		}
		return mqTxService;
	}

	public static void putMqToDoTxIdLocal(Long txId) {
		getOrGenMqToDoTxIdList().add(txId);
	}

	public static void removeMqToDoTxLocal(Long txId) {
		getOrGenMqToDoTxIdList().remove(txId);
	}

	public static void removeMqToDoTxLocal() {
		TLVariableHolder.setVariable(TO_DO_MQ_TX_IDS_KEY, null);
	}

	public static List<Long> getMqToDoTxIdList() {
		return TLVariableHolder.getVariable(TO_DO_MQ_TX_IDS_KEY);
	}

	public static List<Long> getOrGenMqToDoTxIdList() {
		List<Long> txIds = getMqToDoTxIdList();
		if (txIds == null) {
			txIds = new ArrayList<Long>();
			TLVariableHolder.setVariable(TO_DO_MQ_TX_IDS_KEY, txIds);
		}
		return txIds;
	}

	public static void commit() {
		List<Long> txIds = getMqToDoTxIdList();
		if (txIds != null) {
			getMqTxService().commit(txIds);
			removeMqToDoTxLocal();
		}
	}

	public static void rollback() {
		List<Long> txIds = getMqToDoTxIdList();
		if (txIds != null) {
			getMqTxService().rollback(txIds);
			removeMqToDoTxLocal();
		}
	}

}
