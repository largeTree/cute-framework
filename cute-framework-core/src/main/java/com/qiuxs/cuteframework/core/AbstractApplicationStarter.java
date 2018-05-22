package com.qiuxs.cuteframework.core;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

//SpringBoot 默认仅扫面当前包及子包下的类，如果在其他包下存在需要扫面的类 则需要手动指定
@ServletComponentScan("com.qiuxs")
@ComponentScan(basePackages = { "com.qiuxs" })
@MapperScan(basePackages = { "com.qiuxs" })
public abstract class AbstractApplicationStarter {
}
