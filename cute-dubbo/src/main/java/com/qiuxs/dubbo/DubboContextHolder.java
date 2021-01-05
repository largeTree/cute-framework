package com.qiuxs.dubbo;

import java.lang.Thread.State;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.common.store.DataStore;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.config.spring.ReferenceBean;
import org.apache.dubbo.config.spring.ServiceBean;
import org.apache.dubbo.config.spring.beans.factory.annotation.ReferenceAnnotationBeanPostProcessor;
import org.apache.dubbo.remoting.Constants;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.service.EchoService;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.basic.utils.CollectionUtils;
import com.qiuxs.cuteframework.core.basic.utils.ThreadUtils;
import com.qiuxs.cuteframework.core.basic.utils.reflect.FieldUtils;
import com.qiuxs.cuteframework.core.basic.utils.reflect.MethodUtils;
import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;
import com.qiuxs.cuteframework.core.context.EnvironmentContext;

public class DubboContextHolder {

	public static void setAttachment(String key, String value) {
		RpcContext rpcContext = RpcContext.getContext();
		rpcContext.setAttachment(key, value);
	}

	public static String getAttachment(String key) {
		RpcContext rpcContext = RpcContext.getContext();
		return rpcContext.getAttachment(key);
	}

	private static Set<ServiceBean<?>> serviceBeans = new HashSet<>();

	public static void addServiceBean(ServiceBean<?> serviceBean) {
		serviceBeans.add(serviceBean);
	}

	public static void destoryAll() {
		serviceBeans.forEach(item -> {
			try {
				DubboLogger.logger.info("destory serviceBean => " + item.getInterface());
				item.destroy();
			} catch (Exception e) {
				DubboLogger.logger
						.error("destory " + item.getInterface() + ", failed, ext = " + e.getLocalizedMessage(), e);
			}
		});
	}

	/********************** 提供者端 *************************/
	public static Set<ServiceConfig<?>> getServiceConfigs() {
		ConfigurableApplicationContext xwac = (ConfigurableApplicationContext) ApplicationContextHolder.getApplicationContext();
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) xwac.getBeanFactory();
		String[] beanNamesForType = beanFactory.getBeanNamesForType(ServiceBean.class);
		Set<ServiceConfig<?>> serviceConfigs = new HashSet<>();
		for (String name : beanNamesForType) {
			serviceConfigs.add(beanFactory.getBean(name, ServiceBean.class));
		}
		return serviceConfigs;
	} 
	
	/**
	 * 暴露dubbo服务
	 *  
	 * @author qiuxs
	 */
	public static void exportServiceConfig() {
		Set<ServiceConfig<?>> serviceConfigs = getServiceConfigs();
		if (CollectionUtils.isNotEmpty(serviceConfigs)) {
			for (ServiceConfig<?> serviceConfig : serviceConfigs) {
				String refName = "";
				if (serviceConfig.getRef() != null) {
					refName = serviceConfig.getRef().getClass().getName();
				}
				DubboLogger.logger.info(serviceConfig.getInterface() + ": " + serviceConfig + "; ref: " + refName);
				Boolean export = serviceConfig.getExport();
		        if (export != null && !export.booleanValue()) {
		            MethodUtils.invokeMethod(serviceConfig, "doExport", new Class[] {}, new Object[] {});
		        }
			}
		}
	} 

	/***************************** 调用者端 *****************************/
	private static Map<String, ReferenceBean<?>> referenceConfigs = null;

	@SuppressWarnings("unchecked")
	public static Map<String, ReferenceBean<?>> getReferenceConfigs() {
		if (referenceConfigs == null) {
			ConfigurableApplicationContext xwac = (ConfigurableApplicationContext) ApplicationContextHolder
					.getApplicationContext();
			DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) xwac.getBeanFactory();
			List<BeanPostProcessor> beanPostProcessors = beanFactory.getBeanPostProcessors();
			for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
				if (beanPostProcessor instanceof ReferenceAnnotationBeanPostProcessor) {
					ReferenceAnnotationBeanPostProcessor annotationBean = (ReferenceAnnotationBeanPostProcessor) beanPostProcessor;
					try {
						referenceConfigs = (Map<String, ReferenceBean<?>>) org.apache.commons.lang3.reflect.FieldUtils
								.readField(annotationBean, "referenceBeanCache", true);
					} catch (IllegalAccessException e) {
						DubboLogger.logger.error("ext = " + e.getLocalizedMessage(), e);
					}
				}
			}
		}
		return referenceConfigs;
	}

	/*************** 获取Bean ********************/
	public static <T> T getBeanFromDubboSpring(Class<T> interfaceClass) {
		T result = getBean(interfaceClass);
		if (result == null) {
			return ApplicationContextHolder.getBean(interfaceClass);
		} else {
			return result;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> interfaceClass) {
		return (T) getBean(interfaceClass.getName());
	}

	public static Object getBean(String interfaceName) {
		return getBean("", interfaceName, "");
	}

	public static Object getBean(String group, String interfaceName, String verion) {
		String key = group + "/" + interfaceName + ":" + verion;
		ReferenceBean<?> referenceConfig = getReferenceConfigs().get(key);
		if (referenceConfig != null) {
			return referenceConfig.get();
		} else {
			return null;
		}
	}

	/***********************************/
	/**
	 * 取可选dubbo服务。dubbo服务存在时，返回dubbo服务bean。否则返回null。
	 * 
	 * @author lsh
	 * @param beanName  dubbo服务的bean名称
	 * @param dubboBean dubbo服务当前bean。该bean不为null时，说明已存在dubbo服务bean，将直接返回该bean。
	 * @return dubboBean不为null时，返回dubboBean。否则通过beanName取dubbo
	 *         bean。dubbo的服务提供者不存在时，将返回null。
	 */
	public static <T> T getDubboServiceOpt(String beanName, T dubboBean) {
		if (dubboBean == null) { // 尚未得到记录器，先取记录器
			@SuppressWarnings("unchecked")
			T dubboBeanTmp = (T) ApplicationContextHolder.getBeanQuietly(beanName);
			if (dubboBeanTmp == null) {
				DubboLogger.logger.debug("Bean名称[{}]不存在", beanName);
				return null;
			}

			// 回声测试可用性
			try {
				EchoService echoService = (EchoService) dubboBeanTmp;
				echoService.$echo("OK"); // 不抛出异常时，说明可用
				return dubboBeanTmp;
			} catch (Throwable t) { // 发生异常时，说明服务不可用。仅记录调试日志
				DubboLogger.logger.debug("Dubbo服务[{}]不存在", beanName);
				return null;
			}
		}
		return dubboBean;
	}

	/**
	 * 线程池拍个快照
	 * 
	 * @author fengdg
	 */
	public static Map<String, List<Thread>> getThreads(State threadState) {
		Map<String, List<Thread>> portThreadsMap = new HashMap<>();
		DataStore dataStore = ExtensionLoader.getExtensionLoader(DataStore.class).getDefaultExtension();
		Map<String, Object> portExecutorMap = dataStore.get(Constants.EXECUTOR_SERVICE_COMPONENT_KEY);
		for (String port : portExecutorMap.keySet()) {
			List<Thread> threads = new ArrayList<>();
			ThreadPoolExecutor es = (ThreadPoolExecutor) portExecutorMap.get(port);
			@SuppressWarnings("unchecked")
			Set<? extends AbstractQueuedSynchronizer> workers = (Set<? extends AbstractQueuedSynchronizer>) com.qiuxs.cuteframework.core.basic.utils.reflect.FieldUtils
					.getFieldValueQuietly(es, "workers");
			for (AbstractQueuedSynchronizer worker : workers) {
				Thread thread = (Thread) com.qiuxs.cuteframework.core.basic.utils.reflect.FieldUtils
						.getFieldValueQuietly(worker, "thread");
				State state = thread.getState();
				if (threadState == null) {
					threads.add(thread);
				} else if (state == threadState) {
					threads.add(thread);
				}
			}
			portThreadsMap.put(port, threads);
		}
		return portThreadsMap;
	}

	/**
	 * 获取线程池堆栈
	 * 
	 * @author fengdg
	 */
	public static JSONObject getThreadPoolInfoMap(State state, boolean extFlag) {

		Map<Long, Thread> threadMap = new HashMap<>();
		Map<Long, StackTraceElement[]> stackTracesMap = new HashMap<>();

		// 尽早快照
		Map<String, List<Thread>> portThreadsMap = DubboContextHolder.getThreads(state);
		for (String port : portThreadsMap.keySet()) {
			List<Thread> threads = portThreadsMap.get(port);
			for (Thread thread : threads) {
				if (thread == null) {
					continue;
				}
				threadMap.put(thread.getId(), thread);
				stackTracesMap.put(thread.getId(), thread.getStackTrace());// 尽早快照
			}
		}

		// 再处理额外信息
		JSONObject result = new JSONObject();
		result.put("count", threadMap.size());
		for (Long threadId : threadMap.keySet()) {
			JSONObject threadJson = new JSONObject();
			if (threadId == null) {
				continue;
			}

			Thread thread = threadMap.get(threadId);
			if (thread == null) {
				continue;
			}

			result.put(String.valueOf(threadId), threadJson);

			threadJson.put("name", thread.getName());
			threadJson.put("state", thread.getState());

			StackTraceElement[] stackTraces = stackTracesMap.get(threadId);
			if (stackTraces == null) {
				continue;
			}
			String stackTrace = ThreadUtils.getStackTrace(stackTraces);
			threadJson.put("stackTrace", stackTrace);

			if (extFlag) {
				long startMs = Calendar.getInstance().getTimeInMillis();
				RpcContext context = getRpcContext(thread);
				if (context != null) {
					threadJson.put("context", context.getAttachments());
				}
				threadJson.put("extElapsed", Calendar.getInstance().getTimeInMillis() - startMs);
			}
		}

		return result;
	}

	/**
	 * 获取指定线程的RpcContext
	 * 
	 * @author fengdg
	 * @param thread
	 * @return
	 */
	public static RpcContext getRpcContext(Thread thread) {
		Object threadLocalMap = FieldUtils.getFieldValueQuietly(thread, "threadLocals");
		if (threadLocalMap != null) {
			Object table = FieldUtils.getFieldValueQuietly(threadLocalMap, "table");
			for (int i = 0, length = Array.getLength(table); i < length; ++i) {
				final Object entry = Array.get(table, i);
				if (entry != null) {
					// ThreadLocal里取不到信息，从value取
					Object value = FieldUtils.getFieldValueQuietly(entry, "value");
					if (value != null && value instanceof RpcContext) {
						return (RpcContext) value;
					}
				}
			}
		}
		return null;
	}

	public static String getThreadPoolInfo(State state, boolean extFlag) {
		JSONObject threadTraceMap = getThreadPoolInfoMap(state, extFlag);
		StringBuilder sb = new StringBuilder(EnvironmentContext.getAppName()).append(" Thread Pool StackTrace: ")
				.append(threadTraceMap.toJSONString());
		return sb.toString();
	}
}
