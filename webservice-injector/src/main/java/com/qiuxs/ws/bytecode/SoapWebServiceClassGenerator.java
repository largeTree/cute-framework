package com.qiuxs.ws.bytecode;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import com.qiuxs.ws.filter.FilterChainBuilder;
import com.qiuxs.ws.filter.invoker.Invocation;
import com.qiuxs.ws.filter.invoker.Invoker;
import com.qiuxs.ws.porxy.AbstractWsInvoker;

public class SoapWebServiceClassGenerator<T> {

	private final Class<T> wsInterfaceClass;
	private FilterChainBuilder filterChainBuilder;

	private T wsRef;

	public SoapWebServiceClassGenerator(Class<T> wsInterfaceClass) {
		this.wsInterfaceClass = wsInterfaceClass;
	}

	private void init() {
		WebService webService = this.wsInterfaceClass.getAnnotation(WebService.class);
		if (webService == null) {
			throw new IllegalStateException("The @WSReference property Class " + this.wsInterfaceClass.getName() + " has no @WebService annotation.");
		}
		String name = webService.name();
		String targetNamespace = webService.targetNamespace();
		String webServiceClientName = name.replace("Soap", "");
		String wsdlLocation = webService.wsdlLocation();

		T wsRef = null;
		try {
			wsRef = Service.create(new URL(wsdlLocation), new QName(targetNamespace, webServiceClientName)).getPort(this.wsInterfaceClass);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e.getLocalizedMessage(), e);
		}

		// TODO Proxy 
		if (this.filterChainBuilder != null) {
			this.wsRef = this.createPorxy(wsRef);
		} else {
			this.wsRef = wsRef;
		}
	}

	private T createPorxy(T target) {
		ClassGenerator cg = ClassGenerator.newInstance();
		String className = this.wsInterfaceClass.getName() + "$$Proxy";
		cg.setClassName(className)
				.setSuperClass(AbstractWsInvoker.class)
				.addInterface(this.wsInterfaceClass);
		
		// 添加执行目标字段
		StringBuilder tFieldSb = new StringBuilder();
		tFieldSb.append("private final ").append(this.wsInterfaceClass.getName()).append(" target;");
		cg.addField(tFieldSb.toString());
		// 添加构造函数
		StringBuilder cSb = new StringBuilder();
		cSb.append("public ").append(this.wsInterfaceClass.getSimpleName()).append("$$Proxy(")
				.append(this.wsInterfaceClass.getName()).append(" target,")
				.append(FilterChainBuilder.class.getName()).append(" filterChainBuilder")
				.append(") {")
				.append("super(filterChainBuilder);")
				.append(" this.target = target; }");
		cg.addConstructor(cSb.toString());
		
		Method[] methods = this.wsInterfaceClass.getDeclaredMethods();
		for (Method m : methods) {
			String returnType = m.getReturnType().getName();
			StringBuilder mSb = new StringBuilder();
			mSb.append("public ").append(returnType).append(" ").append(m.getName()).append("(");
			Class<?>[] parameterTypes = m.getParameterTypes();
			int pLength = parameterTypes.length;
			if (pLength > 0) {
				for (int i = 0; i < pLength; i++) {
					Class<?> pType = parameterTypes[i];
					mSb.append(pType.getName()).append(" arg").append(i).append(", ");
				}
				// 去除最后的逗号和空格
				mSb.setLength(mSb.length() - 2);
			}
			mSb.append(") {");
			mSb.append(Method.class.getName()).append(" method = ").append(ReflectionUtils.class.getName()).append(".findMethod(this.target.getClass(), \"").append(m.getName()).append("\", ");
			mSb.append("new Class[] { ");
			if (pLength > 0) {
				for (int i = 0; i < pLength; i++) {
					Class<?> pType = parameterTypes[i];
					mSb.append(pType.getName()).append(".class, ");
				}
				mSb.setLength(mSb.length() - 2);
			}
			mSb.append(" });");
			mSb.append(Invocation.class.getName()).append(" invocation = new ").append(Invocation.class.getName()).append("(this.target, method, method.getParameterTypes(), new Object[] { ");
			if (pLength > 0) {
				for (int i = 0; i < pLength; i++) {
					mSb.append("arg").append(i).append(", ");
				}
				mSb.setLength(mSb.length() - 2);
			}
			mSb.append(" });");
			mSb.append(Invoker.class.getName()).append(" invoker = super.buildChain();");
			mSb.append("return (").append(returnType).append(") invoker.invoke(invocation);");
			mSb.append("}");
			cg.addMethod(mSb.toString());
		}
		@SuppressWarnings("unchecked")
		Class<T> clz = (Class<T>) cg.toClass();
		
		try {
			Constructor<T> ctor = clz.getConstructor(this.wsInterfaceClass, FilterChainBuilder.class);
			return BeanUtils.instantiateClass(ctor, target, this.filterChainBuilder);
		} catch (NoSuchMethodException | SecurityException e) {
			// 不可能抛出的异常
		}
		return null;
	}

	public void setFilterChainBuilder(FilterChainBuilder filterChainBuilder) {
		this.filterChainBuilder = filterChainBuilder;
	}

	public synchronized T getWsRef() {
		if (this.wsRef == null) {
			this.init();
		}
		return this.wsRef;
	}

}
