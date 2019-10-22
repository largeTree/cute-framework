package com.qiuxs.cuteframework.view.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = CuteViewConfiguration.PREFIX)
public class CuteViewConfiguration {

	protected static final String PREFIX = "view";

}
