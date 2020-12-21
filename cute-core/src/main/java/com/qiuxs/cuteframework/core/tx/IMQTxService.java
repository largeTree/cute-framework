package com.qiuxs.cuteframework.core.tx;

import java.util.List;

import com.qiuxs.cuteframework.core.tx.mq.TxMessage;
import com.qiuxs.cuteframework.tech.microsvc.disttx.DistTransInfo;

public interface IMQTxService {

	void cacheTxMessage(TxMessage txMessage);

	public void commit(List<String> txIds);

	public void rollback(List<String> txIds);

	boolean checkTransInfoInCache(DistTransInfo transInfo);

}
