package com.qiuxs.cuteframework.core.basic.utils.dingtalk;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dingtalk.chatbot.SendResult;
import com.dingtalk.chatbot.message.TextMessage;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;

public class DingTalkUtils {
	
	private static Logger log = LoggerFactory.getLogger(DingTalkUtils.class);

	private static final Map<String, MyDingtalkChatbotClient> TOKEN_MAP = new HashMap<>();

	public static void sendTextMsg(String token, String text) {
		if (StringUtils.isBlank(token)) {
			ExceptionUtils.throwLogicalException("DingDing Token is empty");
		}
		MyDingtalkChatbotClient chatbotClient = TOKEN_MAP.get(token);
		if (chatbotClient == null) {
			chatbotClient = new MyDingtalkChatbotClient(token);
			TOKEN_MAP.put(token, chatbotClient);
		}
		SendResult sendResult = chatbotClient.send(new TextMessage(text));
		logResult(sendResult);
	}
	
	private static void logResult(SendResult sendResult) {
		if (sendResult != null && !sendResult.isSuccess()) {
			log.warn("钉钉消息发送失败：" + sendResult.getErrorMsg());
		}
	}

}
