package com.qiuxs.cuteframework.core.basic.i18n;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.qiuxs.cuteframework.core.basic.utils.ListUtils;

public class MessageResourceHolder {

	private static Logger log = LogManager.getLogger(MessageResourceHolder.class);
	private static final String[] DEFAULT_LANGS = new String[] { "cn", "en" };

	private static List<Map<String, String>> locations;

	private static Map<String, Map<String, String>> langMsgs;
	
	static {
		init();
	}
	
	private static void init() {
		if (langMsgs == null) {
			langMsgs = new HashMap<>();
		}
		for (String lang : DEFAULT_LANGS) {
			try {
				Properties properties = PropertiesLoaderUtils.loadAllProperties("msg/msg_" + lang + ".properties");
				Map<String, String> msgs = langMsgs.get(lang);
				if (msgs == null) {
					msgs = new HashMap<>();
					langMsgs.put(lang, msgs);
				}
				fillMsgMap(properties, msgs);
			} catch (IOException e) {
				log.error("init msg properties error ext=" + e.getLocalizedMessage(), e);
			}
		}

		if (!ListUtils.isNullOrEmpty(locations)) {
			for (Map<String, String> location : locations) {
				for (Iterator<Map.Entry<String, String>> iter = location.entrySet().iterator(); iter.hasNext();) {
					Entry<String, String> entry = iter.next();
					try {
						Properties properties = PropertiesLoaderUtils.loadAllProperties(entry.getValue());
						String lang = entry.getKey();
						Map<String, String> msgs = langMsgs.get(lang);
						if (msgs == null) {
							msgs = new HashMap<>();
							langMsgs.put(lang, msgs);
						}
						fillMsgMap(properties, msgs);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private static void fillMsgMap(Properties props, Map<String, String> msgMap) {
		for (Iterator<Map.Entry<Object, Object>> iter = props.entrySet().iterator(); iter.hasNext();) {
			Map.Entry<Object, Object> entry = iter.next();
			msgMap.put(entry.getKey().toString(), entry.getValue().toString());
		}
	}

	public static List<Map<String, String>> getLocations() {
		return locations;
	}

	public static void setLocations(List<Map<String, String>> locations) {
		MessageResourceHolder.locations = locations;
	}

	public static Map<String, Map<String, String>> getLangMsgs() {
		return langMsgs;
	}

	public static void setLangMsgs(Map<String, Map<String, String>> langMsgs) {
		MessageResourceHolder.langMsgs = langMsgs;
	}

	public static String getMessage(String lang, String msgKey) {
		Map<String, String> msgs = langMsgs.get(lang);
		if (msgs == null) {
			return null;
		}
		String msg = msgs.get(msgKey);
		return msg;
	}

}
