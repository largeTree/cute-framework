package com.qiuxs.rmq.lc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qiuxs.cuteframework.core.listener.lc.IWebLifecycle;
import com.qiuxs.rmq.ConsumerUtils;
import com.qiuxs.rmq.ProducerUtils;

public class MqWebLifecycle implements IWebLifecycle {

	private static Logger log = LogManager.getLogger(MqWebLifecycle.class);
	
	@Override
	public int order() {
		return 2;
	}

	@Override
	public void lastInit() {
		try {
			ProducerUtils.init();
		} catch (Exception e) {
			log.error("init MqProducerUtil failed, ext = " + e.getLocalizedMessage(), e);
		}
		try {
			ConsumerUtils.init();
		} catch (Exception e) {
			log.error("init MqConsumerUtils failed, ext = " + e.getLocalizedMessage(), e);
		}
	}
	
	@Override
	public void lastDestory() {
		try {
			ProducerUtils.destory();
		} catch (Exception e) {
			log.error("destory ProducerUtil failed, ext = " + e.getLocalizedMessage(), e);
		}
		try {
			ConsumerUtils.destory();
		} catch (Exception e) {
			log.error("destory ConsumerUtils failed, ext = " + e.getLocalizedMessage(), e);
		}
	}
	
}
