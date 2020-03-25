package com.qiuxs.rmq.listener;

import java.util.List;

import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;

public class OrderlyMessageListener extends MessageListener implements MessageListenerOrderly {

	/** 顺序消息消费失败时，挂起当前队列的时间 */
	private static final long SUSPEND_CURRENT_QUEUE_TIME = 3000;

	public OrderlyMessageListener(int listenerType) {
		super(listenerType);
	}

	@Override
	public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
		//调用监听器方法，成功时返回消费成功。失败时，挂起队列，等待一段时间。
		if (invokeListeners(msgs, true)) {
			return ConsumeOrderlyStatus.SUCCESS;
		} else {
			context.setSuspendCurrentQueueTimeMillis(SUSPEND_CURRENT_QUEUE_TIME);
			return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
		}
	}

}
