package com.qiuxs.rmq.log.utils;

import org.apache.rocketmq.common.message.MessageExt;

import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;
import com.qiuxs.rmq.log.entity.MqFailed;
import com.qiuxs.rmq.log.service.IMqFailedService;

public class MqLogUtils {
	
	private static IMqFailedService mqFailedService;
	
	public static void saveMqFailed(MessageExt msg, Object body, Throwable e) {
		MqFailed mqFailed = new MqFailed();
		
	}
	
	private static IMqFailedService getMqFailedService() {
		if (mqFailedService == null) {
			mqFailedService = ApplicationContextHolder.getBean(IMqFailedService.class);
		}
		return mqFailedService;
	}
	
}
