package com.qiuxs.rmq.listener;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qiuxs.cuteframework.core.basic.bean.Pair;
import com.qiuxs.cuteframework.core.basic.utils.JsonUtils;
import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;
import com.qiuxs.cuteframework.core.context.UserContext;
import com.qiuxs.cuteframework.core.tx.IMQTxService;
import com.qiuxs.cuteframework.core.tx.TxConfrimUtils;
import com.qiuxs.cuteframework.tech.microsvc.disttx.DistTransInfo;
import com.qiuxs.rmq.MqClientContants;
import com.qiuxs.rmq.MqTxMessage;
import com.qiuxs.rmq.SerializableSendResult;
import com.qiuxs.rmq.log.service.impl.TransSendService;

public class LocalTransactionListener extends TxConfrimUtils implements TransactionListener {
	
	private static Logger log = LoggerFactory.getLogger(LocalTransactionListener.class);
	
	/**  生产者组名. */
	private final String groupName;
	
	/**  生产者实例. */
	private final String instanceName;
	
	/**  mq事务消息服务. */
	private IMQTxService mqTxService;
	
	/** 简单事务消息表 */
	private TransSendService transSendService;
	
	/**
	 * 构造本地事务监听器
	 *
	 * @param groupName the group name
	 * @param instanceName the instance name
	 */
	public LocalTransactionListener(String groupName, String instanceName, TransSendService transSendService) {
		this.groupName = groupName;
		this.instanceName = instanceName;
		this.mqTxService = ApplicationContextHolder.getBean(IMQTxService.class);
		this.transSendService = transSendService;
		log.info("Construct an LocalTransactionListener {} {}", groupName, instanceName);
	}

	/**
	 * 本地事务执行，保存消息到redis，存成功则返回unknow，等待数据库事务提交或回滚
	 * 存失败则返回rollback，回滚事务消息
	 *
	 * @param msg the msg
	 * @param arg the arg
	 * @return the local transaction state
	 */
	@Override
	public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
		@SuppressWarnings("unchecked")
		Pair<SerializableSendResult, DistTransInfo> pair = (Pair<SerializableSendResult, DistTransInfo>) arg;
		SerializableSendResult sendResult = pair.getV1();
		DistTransInfo distTx = pair.getV2();
		this.mqTxService.cacheTxMessage(new MqTxMessage(distTx, sendResult));
		return LocalTransactionState.UNKNOW;
	}

	/**
	 * 事务消息超时没有进行提交或回滚，broker将发送消息回来检查事务是否完成
	 *
	 * @param msg the msg
	 * @return the local transaction state
	 */
	@Override
	public LocalTransactionState checkLocalTransaction(MessageExt msg) {
		String distTx = msg.getProperty(MqClientContants.MSG_PROP_SUB_DIST_TX);
		log.warn("Check LocalTransaction {}", distTx);
		
		DistTransInfo transInfo = JsonUtils.parseObject(distTx, DistTransInfo.class);
		
		UserContext.mockUser(transInfo.getUnitId());
		
		if (this.mqTxService.checkTransInfoInCache(transInfo)) {
			return LocalTransactionState.UNKNOW;
		}
		
		// 缓存中已不在的情况下，检查事务ID是否被存入数据库，如没被存入，则回滚消息
		if (this.transSendService.existsTx(transInfo.getTxId())) {
			return LocalTransactionState.COMMIT_MESSAGE;
		} else {
			return LocalTransactionState.ROLLBACK_MESSAGE;
		}
	}
	
	/**
	 * Gets the 生产者组名.
	 *
	 * @return the 生产者组名
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * Gets the 生产者实例.
	 *
	 * @return the 生产者实例
	 */
	public String getInstanceName() {
		return instanceName;
	}

}
