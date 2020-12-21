package com.qiuxs.cuteframework.core.tx;

import java.util.List;

import com.qiuxs.cuteframework.core.tx.mq.TxMessage;
import com.qiuxs.cuteframework.tech.microsvc.disttx.DistTransInfo;

public interface IMQTxService {

	/**
	 * 事务消息加入缓存
	 * @param txMessage
	 */
	void cacheTxMessage(TxMessage txMessage);

	/***
	 * 提交事务消息
	 * @param txKeys
	 */
	public void commit(List<String> txKeys);

	/**
	 * 回滚事务消息
	 * @param txKeys
	 */
	public void rollback(List<String> txKeys);

	/**
	 * 检查事务消息是否还在缓存中
	 * @param transInfo
	 * @return
	 */
	boolean checkTransInfoInCache(DistTransInfo transInfo);

	/**
	 * 逐出已超时的事务消息
	 */
	void expulsionTimeoutedTransactions();

}
