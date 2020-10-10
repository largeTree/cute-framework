package com.qiuxs.cuteframework.tech.spring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.context.StopWatchContext;

@Aspect
@Component
public class StopWatchAspectj {

	private static Logger log = LoggerFactory.getLogger(StopWatchAspectj.class);

	@Around("@annotation(sw)")
	public Object stopWatch(ProceedingJoinPoint point, StopWatcher sw) throws Throwable {
		StopWatch stopWatch = StopWatchContext.get(true);
		String name = sw.value();
		if (StringUtils.isBlank(name)) {
			name = point.getSignature().toShortString();
		}
		stopWatch.start(name);
		try {
			return point.proceed();
		} finally {
			stopWatch.stop();
			log.info("{} costMs = {}", name, stopWatch.getLastTaskTimeMillis());
		}
	}

}
