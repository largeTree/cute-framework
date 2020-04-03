package com.qiuxs.cuteframework.core.tx;

import java.util.List;

import com.qiuxs.cuteframework.core.tx.mq.TxMessage;

public interface IMQTxService {

	void sendPrepare(TxMessage txMessage);

	public void commit(List<Long> txIds);

	public void rollback(List<Long> txIds);

}
