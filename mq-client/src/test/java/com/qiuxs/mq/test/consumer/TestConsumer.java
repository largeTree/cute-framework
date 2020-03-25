package com.qiuxs.mq.test.consumer;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class TestConsumer {

	public void consumer(String topic, String tags, String key, Object body, Map<String, String> extProps) {

	}

	public void consumerB(String topic, String tags, String key, Object body, Map<String, String> extProps) {

	}

}
