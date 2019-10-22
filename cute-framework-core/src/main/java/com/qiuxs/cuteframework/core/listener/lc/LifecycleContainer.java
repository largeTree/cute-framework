package com.qiuxs.cuteframework.core.listener.lc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;

public class LifecycleContainer {

	private static List<ILifecycle> lifecycles;

	public static void loadLifecycle(ApplicationContext context) {
		String[] lifecycleNames = context.getBeanNamesForType(ILifecycle.class);
		List<ILifecycle> lifecycles = new ArrayList<>(lifecycleNames.length);
		for (String beanName : lifecycleNames) {
			lifecycles.add(context.getBean(beanName, ILifecycle.class));
		}
		lifecycles.sort((v1, v2) -> {
			return v1.order() == v2.order() ? 0 : v1.order() > v2.order() ? 1 : -1;
		});
		LifecycleContainer.lifecycles = lifecycles;
	}

	public static List<ILifecycle> getLifecycles() {
		return lifecycles;
	}
	
}
