package com.qiuxs.rmq.log.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.core.basic.config.IConfiguration;
import com.qiuxs.cuteframework.core.basic.config.UConfigUtils;
import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.cuteframework.core.tx.IMQTxService;
import com.qiuxs.cuteframework.core.tx.TxConfrimUtils;
import com.qiuxs.cuteframework.core.tx.mq.TxMessage;
import com.qiuxs.cuteframework.tech.mc.McFactory;
import com.qiuxs.cuteframework.tech.microsvc.disttx.DistTransInfo;
import com.qiuxs.rmq.MqClientContants;
import com.qiuxs.rmq.MqTxMessage;
import com.qiuxs.rmq.ProducerUtils;
import com.qiuxs.rmq.SerializableSendResult;

@Service("mqTxService")
public class MQTxService implements IMQTxService {
	
	private static Logger log = LogManager.getLogger(MQTxService.class);
	
	/** 事务消息超时时间，超时后自动回滚事务消息， 默认十分钟 */
	private Long transactionMessageTimeout;

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
		private static final Map<String, TxMessage> txMessageMap = McFactory.getFactory().createMap("rmq_tx_message_map");

		private static void putMessage(DistTransInfo distTx, TxMessage txMessage) {
			String key = key(distTx.getTxId(), distTx.getUnitId());
			txMessageMap.put(key, txMessage);
			addTxId(key);
		}

		private static void addTxId(String key) {
			TxConfrimUtils.putMqToDoTxIdLocal(key);
		}
		
		private static TxMessage getTxMessage(DistTransInfo distTx) {
			return getTxMessage(distTx.getTxId(), distTx.getUnitId());
		}
		
		private static TxMessage getTxMessage(Long txId, Long unitId) {
			return txMessageMap.get(key(txId, unitId));
		}

		private static TxMessage getTxMessage(String txKey) {
			DistTransInfo distTxInfo = parseKey(txKey);
			return getTxMessage(distTxInfo);
		}
		
		private static void removeTxMessage(String txKey) {
			txMessageMap.remove(txKey);
		}
		
		private static DistTransInfo parseKey(String txKey) {
			return DistTransInfo.parseTxKey(txKey);
		}
		
		private static String key(Long txId, Long unitId) {
			return DistTransInfo.txKey(txId, unitId);
		}
	}
	
	@Override
	public void expulsionTimeoutedTransactions() {
		Collection<TxMessage> cachedTxMessages = TxMessageHolder.txMessageMap.values();
		List<String> timeoutedTxKeys = new ArrayList<>();
		for (TxMessage txMessage : cachedTxMessages) {
			
			Long unitId = txMessage.getDistTx().getUnitId();
			int webIndex = EnvironmentContext.getWebIndex();
			int webCount = EnvironmentContext.getWebCount();
			// unitId取模，每台服务逐出一部分
			if ((unitId % webCount) == webIndex) {
				if ((System.currentTimeMillis() - txMessage.getBorntime()) > getTransactionMessageTimeout()) {
					timeoutedTxKeys.add(txMessage.getDistTx().getTxKey());
				}
			}
		}
		log.warn("ExplusionTimeoutedTransactions {}", timeoutedTxKeys);
		// 回滚已经超时了的事务消息
		rollback(timeoutedTxKeys);
	}
	
	@Override
	public boolean checkTransInfoInCache(DistTransInfo transInfo) {
		TxMessage txMessage = TxMessageHolder.getTxMessage(transInfo);
		return txMessage != null;
	}
	
	@Override
	public void cacheTxMessage(TxMessage txMessage) {
		if (txMessage instanceof MqTxMessage) {
			TxMessageHolder.putMessage(txMessage.getDistTx(), txMessage);
		} else {
			throw new IllegalArgumentException("only allow " + MqTxMessage.class.getName());
		}
	}

	@Override
	public void commit(List<String> txKeys) {
		for (String txKey : txKeys) {
			MqTxMessage txMessage = (MqTxMessage) TxMessageHolder.getTxMessage(txKey);
			try {
				// 提交事务
				SerializableSendResult transactionSendResult = txMessage.getLocalTransactionSendResult();
				log.info("Commit TxMessage txKey = {}, mqMsgId = {}", txKey, transactionSendResult.getMsgId());
				ProducerUtils.commitTransactionMessage(transactionSendResult);
				TxMessageHolder.removeTxMessage(txKey);
			} catch (Throwable e) {
				log.error("TxMessageCommit failed, ext = " + e.getLocalizedMessage(), e);
			}
		}
	}

	@Override
	public void rollback(List<String> txKeys) {
		for (String txKey : txKeys) {
			MqTxMessage txMessage = (MqTxMessage) TxMessageHolder.getTxMessage(txKey);
			try {
				SerializableSendResult transactionSendResult = txMessage.getLocalTransactionSendResult();
				log.info("Rollback TxMessage txKey = {}, mqMsgId = {} ", txKeys, transactionSendResult.getMsgId());
				ProducerUtils.rollbackTransactionMessage(transactionSendResult);
				TxMessageHolder.removeTxMessage(txKey);
			} catch (Throwable e) {
				log.error("Rollback TxMessage failed, ext = " + e.getLocalizedMessage(), e);
			}
		}
	}
	
	/**
	 * 事务消息超时时间，超过此时间仍未提交的事务消息将被回滚
	 * @return
	 */
	public long getTransactionMessageTimeout() {
		if (transactionMessageTimeout == null) {
			synchronized (this) {
				if (transactionMessageTimeout == null) {
					IConfiguration domain = UConfigUtils.getDomain(MqClientContants.CONFIG_DOMAIN);
					if (domain != null) {
						transactionMessageTimeout = domain.getLongValue(MqClientContants.TRANSACTION_TIMEOUT, MqClientContants.DEFAULT_TRANSACTION_TIMEOUT);
					}
				}
			}
		}
		return transactionMessageTimeout;
	}

}
