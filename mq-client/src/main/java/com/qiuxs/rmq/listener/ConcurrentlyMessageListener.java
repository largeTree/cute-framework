package com.qiuxs.rmq.listener;

import java.util.List;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

public class ConcurrentlyMessageListener extends MessageListener implements MessageListenerConcurrently {

	public ConcurrentlyMessageListener(int listenerType) {
		super(listenerType);
	}

	@Override
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
		if (super.invokeListeners(msgs, false)) {
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		} else {
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
	}

}
