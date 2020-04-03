package com.qiuxs.rmq;

import org.apache.rocketmq.common.message.Message;

import com.qiuxs.cuteframework.core.tx.mq.TxMessage;

/**
 * mq事务消息体
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年4月3日 下午11:43:35 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public final class MqTxMessage implements TxMessage {

	private static final long serialVersionUID = -1713577573612541202L;

	private final Long txId;
	private final Message msg;
	private final long brontime;

	public MqTxMessage(Long txId, Message msg) {
		this.txId = txId;
		this.msg = msg;
		this.brontime = System.currentTimeMillis();
	}

	@Override
	public Long getTxId() {
		return txId;
	}

	@Override
	public Object getMessage() {
		return msg;
	}

	@Override
	public long getBorntime() {
		return brontime;
	}

}
