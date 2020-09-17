package com.qiuxs.rmq.log.service;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.core.tx.IMQTxService;
import com.qiuxs.cuteframework.core.tx.TxConfrimUtils;
import com.qiuxs.cuteframework.core.tx.mq.TxMessage;
import com.qiuxs.cuteframework.tech.mc.McFactory;
import com.qiuxs.rmq.ProducerUtils;

@Service("mqTxService")
public class MQTxService implements IMQTxService {
	
	private static Logger log = LogManager.getLogger(MQTxService.class);

	/**
	 * 事务消息缓存
	 * 功能描述: <br/>  
	 * 新增原因: TODO<br/>  
	 * 新增日期: 2020年4月3日 下午11:04:16 <br/>  
	 *  
	 * @author qiuxs   
	 * @version 1.0.0
	 */
	private static final class TxMessageHolder {
		private static final Map<Long, TxMessage> txMessageMap = McFactory.getFactory().createMap("rmq_tx_message_map");

		private static void putMessage(Long txId, TxMessage txMessage) {
			txMessageMap.put(txId, txMessage);
			addTxId(txId);
		}

		private static void addTxId(Long txId) {
			TxConfrimUtils.putMqToDoTxIdLocal(txId);
		}
	}
	
	@Override
	public void sendPrepare(TxMessage txMessage) {
		TxMessageHolder.putMessage(txMessage.getTxId(), txMessage);
	}

	@Override
	public void commit(List<Long> txIds) {
		for (Long txId : txIds) {
			TxMessage txMessage = TxMessageHolder.txMessageMap.get(txId);
			try {
				SendResult sendResult = ProducerUtils.sendDirect((Message) txMessage.getMessage());
				log.info("Commit TxMessage txId = " + txId + ", mqMsgId = " + sendResult.getMsgId());
			} catch (Throwable e) {
				log.error("TxMessageCommit failed, ext = " + e.getLocalizedMessage(), e);
			}
		}
	}

	@Override
	public void rollback(List<Long> txIds) {
		for (Long txId : txIds) {
			TxMessageHolder.txMessageMap.remove(txId);
		}
	}

}
