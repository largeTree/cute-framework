package com.qiuxs.cuteframework.core.basic.i18n;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.core.basic.utils.ListUtils;

@Configuration
@ConfigurationProperties(prefix = "msg")
@Component
public class MessageResourceHolder {

	private static Logger log = LogManager.getLogger(MessageResourceHolder.class);
	private static final String[] DEFAULT_LANGS = new String[] { "cn", "en" };

	private List<Map<String, String>> locations;

	private Map<String, Map<String, String>> langMsgs;

	@PostConstruct
	private void init() {
		if (this.langMsgs == null) {
			this.langMsgs = new HashMap<>();
		}
		for (String lang : DEFAULT_LANGS) {
			try {
				Properties properties = PropertiesLoaderUtils.loadAllProperties("msg/msg_" + lang + ".properties");
				Map<String, String> msgs = this.langMsgs.get(lang);
				if (msgs == null) {
					msgs = new HashMap<>();
					this.langMsgs.put(lang, msgs);
				}
				this.fillMsgMap(properties, msgs);
			} catch (IOException e) {
				log.error("init msg properties error ext=" + e.getLocalizedMessage(), e);
			}
		}

		if (!ListUtils.isNullOrEmpty(this.locations)) {
			for (Map<String, String> location : this.locations) {
				for (Iterator<Map.Entry<String, String>> iter = location.entrySet().iterator(); iter.hasNext();) {
					Entry<String, String> entry = iter.next();
					try {
						Properties properties = PropertiesLoaderUtils.loadAllProperties(entry.getValue());
						String lang = entry.getKey();
						Map<String, String> msgs = this.langMsgs.get(lang);
						if (msgs == null) {
							msgs = new HashMap<>();
							this.langMsgs.put(lang, msgs);
						}
						this.fillMsgMap(properties, msgs);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void fillMsgMap(Properties props, Map<String, String> msgMap) {
		for (Iterator<Map.Entry<Object, Object>> iter = props.entrySet().iterator(); iter.hasNext();) {
			Map.Entry<Object, Object> entry = iter.next();
			msgMap.put(entry.getKey().toString(), entry.getValue().toString());
		}
	}

	public List<Map<String, String>> getLocations() {
		return locations;
	}

	public void setLocations(List<Map<String, String>> locations) {
		this.locations = locations;
	}

	public Map<String, Map<String, String>> getLangMsgs() {
		return langMsgs;
	}

	public void setLangMsgs(Map<String, Map<String, String>> langMsgs) {
		this.langMsgs = langMsgs;
	}

	public String getMessage(String lang, String msgKey) {
		Map<String, String> msgs = this.langMsgs.get(lang);
		if (msgs == null) {
			return null;
		}
		String msg = msgs.get(msgKey);
		return msg;
	}

}
