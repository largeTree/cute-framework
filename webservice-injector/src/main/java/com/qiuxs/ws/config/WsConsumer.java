package com.qiuxs.ws.config;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import com.qiuxs.ws.anno.WSReference;
import com.qiuxs.ws.config.spring.WSReferenceBean;
import com.qiuxs.ws.filter.Filter;
import com.qiuxs.ws.filter.FilterChainBuilder;

public class WsConsumer implements BeanPostProcessor, BeanFactoryPostProcessor {

	private final ConcurrentHashMap<String, WSReferenceBean<?>> wsReferenceBeans = new ConcurrentHashMap<>();

	private List<Filter> filters;
	
	private FilterChainBuilder filterChainBuilder;
	
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		String[] filterNames = beanFactory.getBeanNamesForType(Filter.class);
		if (filterNames.length > 0) {
			List<Filter> filters = new ArrayList<>(filterNames.length);
			for (String filterName : filterNames) {
				filters.add(beanFactory.getBean(filterName, Filter.class));
			}
			filters.sort((o1, o2) -> {
				// order越小越先执行
				return o1.getOrder() == o2.getOrder() ? 0 : o1.getOrder() < o2.getOrder() ? -1 : 1;
			});
			this.filters = filters;
			this.filterChainBuilder = new FilterChainBuilder(this.filters);
		}
	}


	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		Class<? extends Object> beanClass = bean.getClass();
		Field[] fields = beanClass.getDeclaredFields();

		Method[] methods = beanClass.getMethods();
		for (Method method : methods) {
			String name = method.getName();
			// 仅公有处理setter方法
			if (name.length() > 3 && name.startsWith("set") && method.getParameterCount() == 1 && Modifier.isPublic(method.getModifiers())
					&& !Modifier.isStatic(method.getModifiers())) {
				try {
					WSReference wsReference = method.getAnnotation(WSReference.class);
					if (wsReference != null) {
						Object value = this.wsRefer(wsReference, method.getParameterTypes()[0]);
						if (value != null) {
							method.invoke(bean, value);
						}
					}
				} catch (Exception e) {
					throw new BeanInitializationException("Field to init WebService reference at method " + name + " in class " + bean.getClass().getName(), e);
				}
			}
		}

		// 注入字段值
		for (Field f : fields) {
			try {
				if (Modifier.isStatic(f.getModifiers()) || Modifier.isFinal(f.getModifiers())) {
					continue;
				}
				if (!f.isAccessible()) {
					f.setAccessible(true);
				}
				WSReference wsReference = f.getAnnotation(WSReference.class);
				if (wsReference != null) {
					Class<?> referenceClass = f.getType();
					Object value = this.wsRefer(wsReference, referenceClass);
					if (value != null) {
						f.set(bean, value);
					}
				}
			} catch (Exception e) {
				throw new BeanInitializationException("Field to init WebService reference at field " + f.getName() + " in class " + bean.getClass().getName(), e);
			}
		}
		return bean;
	}

	/**
	 * 构造WebService代理类对象
	 * @param reference
	 * @param referenceClass
	 * @return
	 */
	private Object wsRefer(WSReference reference, Class<?> referenceClass) {
		if (!referenceClass.isInterface()) {
			throw new IllegalStateException("The @WSReference property type " + referenceClass.getName() + " is not a interface.");
		}
		String interfaceName = referenceClass.getName();

		WSReferenceBean<?> referenceBean = this.wsReferenceBeans.get(interfaceName);
		if (referenceBean == null) {
			referenceBean = new WSReferenceBean<>(referenceClass);
			referenceBean.setWsConsumer(this);
			this.wsReferenceBeans.putIfAbsent(interfaceName, referenceBean);
		}
		return referenceBean.getObject();
	}
	
	/**
	 * 调用链建造器
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public FilterChainBuilder getFilterChainBuilder() {
		return this.filterChainBuilder;
	}

}
