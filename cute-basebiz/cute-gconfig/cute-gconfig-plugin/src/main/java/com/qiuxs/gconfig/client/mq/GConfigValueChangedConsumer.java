package com.qiuxs.gconfig.client.mq;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.qiuxs.gconfig.client.GConfigClientUtils;
import com.qiuxs.gconfig.mq.GConfigUpdateDTO;

@Component
public class GConfigValueChangedConsumer {
	
	/**
	 * 消费消息，清理配置缓存
	 *  
	 * @author qiuxs  
	 * @param topic
	 * @param tag
	 * @param bizKey
	 * @param body
	 * @param extProps
	 */
	public void consumer(String topic, String tag, String bizKey, Object body, Map<String, String> extProps) {
		GConfigUpdateDTO dto = (GConfigUpdateDTO) body;
		GConfigClientUtils.invalidCache(dto.getDomain(), dto.getOwnerType(), dto.getOwnerId(), dto.getCode());
	}

}
