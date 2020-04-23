package com.qiuxs.cuteframework.core.basic.utils.dingtalk;

import java.util.HashMap;
import java.util.Map;

import com.dingtalk.chatbot.message.TextMessage;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;

public class DingTalkUtils {

	public static final Map<HookKey, MyDingtalkChatbotClient> MAP_CLIENT = new HashMap<>();
	static {
		MAP_CLIENT.put(HookKey.FSQH_ALL, new MyDingtalkChatbotClient(HookKey.FSQH_ALL.token()));
		MAP_CLIENT.put(HookKey.LOG_NOTICE, new MyDingtalkChatbotClient(HookKey.LOG_NOTICE.token()));
	}
	
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
		chatbotClient.send(new TextMessage(text));
	}
	
	/**
	 * 发送文本消息
	 * @author qiuxs
	 *
	 * @param key
	 * @param text
	 *
	 * 创建时间：2018年10月24日 下午10:23:05
	 */
	public static void sendTextMsg(HookKey key, String text) {
		MyDingtalkChatbotClient client = MAP_CLIENT.get(key);
		if (client == null) {
			throw new RuntimeException("Unsupported HookKey");
		}
		TextMessage msg = new TextMessage(text);
		client.send(msg);
	}

	public enum HookKey {
		/** 浮世清欢总群 */
		FSQH_ALL("fsqh", "2a83bae7a4d8814658d7cbb7797ad214e7bb9a0f89195eade963de06134b9e5a"),
		/** 日志通知 */
		LOG_NOTICE("log", "e4c13865b7cc0822178f1b8a574a6922aa4f9e01b9ce0396c8c0bc7fa4067d20");
		private String val;
		private String token;

		HookKey(String value, String token) {
			this.val = value;
			this.token = token;
		}

		public String key() {
			return this.val;
		}

		public String token() {
			return this.token;
		}
	}

}
