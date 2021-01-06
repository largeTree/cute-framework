package com.qiuxs.rmq.microsvc;

import org.apache.rocketmq.common.message.Message;

import com.qiuxs.cuteframework.tech.microsvc.MicroSvcContext;

public class MqMicroSvcContext extends MicroSvcContext {

	// private Message msg;

	public MqMicroSvcContext(Message msg) {
		super(msg.getProperties());
		// this.msg = msg;
	}

}
