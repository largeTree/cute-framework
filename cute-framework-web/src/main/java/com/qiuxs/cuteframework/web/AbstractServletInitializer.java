package com.qiuxs.cuteframework.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@ServletComponentScan("com.qiuxs")
@ComponentScan(basePackages = { "com.qiuxs" })
@MapperScan(basePackages = { "com.qiuxs.**.dao" })
@EnableAutoConfiguration
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
public abstract class AbstractServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(this.getClass());
	}

	protected static void run(Class<?> clz, String[] args) {
		SpringApplication.run(clz, args);
	}

}
