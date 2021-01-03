package com.qiuxs.cuteframework.tech.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;

import com.qiuxs.cuteframework.core.basic.config.UConfigUtils;
import com.qiuxs.cuteframework.core.basic.utils.ClassPathResourceUtil;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;

public class Log4j2ConfigurationFactory extends ConfigurationFactory {
	
	private static final String CONFIG_DOMAIN = "log4j2";

	private static Log4j2ConfigurationFactory instance = new Log4j2ConfigurationFactory();

	public static Log4j2ConfigurationFactory getInstance() {
		return instance;
	}

	@Override
	protected String[] getSupportedTypes() {
		return null;
	}
	
	@Override
	protected ConfigurationSource getInputFromResource(String resource, ClassLoader loader) {
		String location = UConfigUtils.getFirstAvailableLocation(CONFIG_DOMAIN);
		if (StringUtils.isEmpty(location)) {//兼容
			location = "classpath:/log4j2.xml";
		}		
		if (location.startsWith("classpath")) {
			try {
				return new ConfigurationSource(ClassPathResourceUtil.getSingleResource(location).getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			File file = new File(location);
			try {
				return new ConfigurationSource(new FileInputStream(file), file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public Configuration getConfiguration(LoggerContext loggerContext, ConfigurationSource source) {
		ConfigurationFactory.resetConfigurationFactory();
		Configuration configuration = ConfigurationFactory.getInstance().getConfiguration(loggerContext, source);
		ConfigurationFactory.setConfigurationFactory(instance);
		return configuration;
	}

}
