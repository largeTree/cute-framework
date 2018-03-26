package com.qiuxs.cuteframework.core.basic.config.uconfig.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.qiuxs.cuteframework.core.basic.bean.IMergeable;
import com.qiuxs.cuteframework.core.basic.config.uconfig.IConfiguration;
import com.qiuxs.cuteframework.core.basic.config.uconfig.ex.UConfigException;
import com.qiuxs.cuteframework.core.basic.utils.MapUtils;
import com.qiuxs.cuteframework.core.basic.utils.io.FileUtils;

public class XMLConfiguration implements IConfiguration, IMergeable<XMLConfiguration> {

	private Map<String, String> configItems = new HashMap<String, String>();

	public XMLConfiguration(String location) throws IOException {
		InputStream in = FileUtils.readAsInputStream(location);
		this.load(in);
	}

	public XMLConfiguration(InputStream in) throws IOException {
		this.load(in);
	}

	private void load(InputStream in) throws IOException {
		SAXReader reader = new SAXReader();
		try {
			Document config = reader.read(in);
			Element rootElement = config.getRootElement();
			@SuppressWarnings("unchecked")
			Iterator<Element> elementIterator = rootElement.elementIterator();
			while (elementIterator.hasNext()) {
				Element item = elementIterator.next();
				this.configItems.put(item.getName(), item.getTextTrim());
			}
		} catch (DocumentException e) {
			throw new UConfigException("配置文件格式错误：" + e.getLocalizedMessage(), e);
		}
	}

	@Override
	public Object get(String key) {
		return this.configItems.get(key);
	}

	@Override
	public String getString(String key) {
		return MapUtils.getString(configItems, key);
	}

	@Override
	public String getString(String key, String defaultValue) {
		String val = MapUtils.getString(configItems, key);
		return val == null ? defaultValue : val;
	}

	@Override
	public String getStringMust(String key) {
		return MapUtils.getStringMust(configItems, key);
	}

	@Override
	public Integer getInteger(String key) {
		return MapUtils.getInteger(configItems, key);
	}

	@Override
	public int getIntValue(String key, int defaultValue) {
		Integer val = this.getInteger(key);
		return val == null ? defaultValue : val.intValue();
	}

	@Override
	public Integer getIntegerMust(String key) {
		return MapUtils.getIntegerMust(configItems, key);
	}

	@Override
	public Long getLong(String key) {
		return MapUtils.getLong(configItems, key);
	}

	@Override
	public long getLongValues(String key, long defaultValue) {
		Long val = this.getLong(key);
		return val == null ? defaultValue : val.longValue();
	}

	@Override
	public Long getLongMust(String key) {
		return MapUtils.getLongMust(configItems, key);
	}

	@Override
	public void merge(XMLConfiguration margeable) {

	}

}
