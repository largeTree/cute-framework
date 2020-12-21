package com.qiuxs.rmq;

import com.qiuxs.cuteframework.core.tx.mq.TxMessage;
import com.qiuxs.cuteframework.tech.microsvc.disttx.DistTransInfo;

/**
 * mq事务消息体 功能描述: <br/>
 * 新增原因: TODO<br/>
 * 新增日期: 2020年4月3日 下午11:43:35 <br/>
 * 
 * @author qiuxs
 * @version 1.0.0
 */
public final class MqTxMessage implements TxMessage {

	private static final long serialVersionUID = -1713577573612541202L;

	/** 分布式事务上下文 */
	private final DistTransInfo distTx;
	private final SerializableSendResult localTransactionSendResult;
	private final long brontime;

	public MqTxMessage(DistTransInfo distTx, SerializableSendResult serializableSendResult) {
		this.distTx = distTx;
		this.localTransactionSendResult = serializableSendResult;
		this.brontime = System.currentTimeMillis();
	}

	@Override
	public DistTransInfo getDistTx() {
		return distTx;
	}

	public SerializableSendResult getLocalTransactionSendResult() {
		return localTransactionSendResult;
	}

	@Override
	public long getBorntime() {
		return brontime;
	}

}
