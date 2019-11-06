package com.qiuxs.cuteframework.core;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//SpringBoot 默认仅扫面当前包及子包下的类，如果在其他包下存在需要扫面的类 则需要手动指定
@EnableTransactionManagement
@ServletComponentScan("com.qiuxs")
@ComponentScan(basePackages = { "com.qiuxs" })
@MapperScan(basePackages = { "com.qiuxs.**.dao" })
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
public abstract class AbstractApplicationStarter {

	/**
	 * 调用此方法启动服务
	 * 
	 * @param startClass
	 * @param args
	 */
	protected static final void run(Class<?> startClass, String[] args) {
		SpringApplication application = new SpringApplication(startClass);
		application.run(args);
	}

}
