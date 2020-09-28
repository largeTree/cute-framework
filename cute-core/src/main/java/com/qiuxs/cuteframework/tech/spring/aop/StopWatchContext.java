package com.qiuxs.cuteframework.tech.spring.aop;

import org.springframework.util.StopWatch;

import com.qiuxs.cuteframework.core.context.TLVariableHolder;

public class StopWatchContext {

	private static final String SW_TL_KEY = "cuttent_thread_stop_watch";

	public static StopWatch init() {
		StopWatch sw = new StopWatch();
		TLVariableHolder.setVariable(SW_TL_KEY, sw);
		return sw;
	}
	
	public static StopWatch get(boolean autoCreate) {
		StopWatch sw = TLVariableHolder.getVariable(SW_TL_KEY);
		if (sw == null && autoCreate) {
			sw = new StopWatch();
			TLVariableHolder.setVariable(SW_TL_KEY, sw);
		}
		return sw;
	}

}
