package com.qiuxs.ws.bytecode;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.qiuxs.ws.utils.ReflectUtils;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

/**
 * 类生成器
 * @author qiuxs
 *
 */
public class ClassGenerator {

	public static interface DC {
	}

	private static final AtomicLong CLASS_NAME_COUNTER = new AtomicLong(0);

	private static final String SIMPLE_NAME_TAG = "<init>";

	private static final Map<ClassLoader, ClassPool> POOL_MAP = new ConcurrentHashMap<>();

	public static ClassGenerator newInstance() {
		return new ClassGenerator(getClassPool(Thread.currentThread().getContextClassLoader()));
	}

	public static ClassGenerator newInstance(ClassLoader loader) {
		return new ClassGenerator(getClassPool(loader));
	}

	public static ClassPool getClassPool(ClassLoader loader) {
		if (loader == null)
			return ClassPool.getDefault();

		ClassPool pool = POOL_MAP.get(loader);
		if (pool == null) {
			pool = new ClassPool(true);
			pool.appendClassPath(new LoaderClassPath(loader));
			POOL_MAP.put(loader, pool);
		}
		return pool;
	}

	public static boolean isDynamicClass(Class<?> cl) {
		return ClassGenerator.DC.class.isAssignableFrom(cl);
	}

	private ClassPool pool;

	private CtClass ctClass;

	private String ctClassName, ctSuperClassName;

	private Set<String> ctInterfaces;

	private List<String> ctFields, ctConstructors, ctMethods;

	private Map<String, Method> ctCopyMethods; // <方法描述, 方法实例>

	private Map<String, Constructor<?>> ctCopyConstructors; // <构造函数描述, 构造函数实例>

	private boolean ctDefaultConstructor = false; // 是否包含默认构造函数

	private ClassGenerator() {
	}

	private ClassGenerator(ClassPool pool) {
		this.pool = pool;
	}

	public String getClassName() {
		return ctClassName;
	}

	public ClassGenerator setClassName(String name) {
		ctClassName = name;
		return this;
	}

	public ClassGenerator addInterface(String interfaceName) {
		if (ctInterfaces == null)
			ctInterfaces = new HashSet<String>();
		ctInterfaces.add(interfaceName);
		return this;
	}

	public ClassGenerator addInterface(Class<?> clz) {
		return addInterface(clz.getName());
	}

	public ClassGenerator setSuperClass(String className) {
		ctSuperClassName = className;
		return this;
	}

	public ClassGenerator setSuperClass(Class<?> clz) {
		ctSuperClassName = clz.getName();
		return this;
	}

	public ClassGenerator addField(String code) {
		if (ctFields == null)
			ctFields = new ArrayList<String>();
		ctFields.add(code);
		return this;
	}

	public ClassGenerator addField(String name, int mod, Class<?> type) {
		return addField(name, mod, type, null);
	}

	public ClassGenerator addField(String name, int mod, Class<?> type, String def) {
		StringBuilder sb = new StringBuilder();
		sb.append(ReflectUtils.modifier(mod)).append(' ').append(ReflectUtils.getName(type)).append(' ');
		sb.append(name);
		if (def != null && def.length() > 0) {
			sb.append('=');
			sb.append(def);
		}
		sb.append(';');
		return addField(sb.toString());
	}

	public ClassGenerator addMethod(String code) {
		if (ctMethods == null)
			ctMethods = new ArrayList<String>();
		ctMethods.add(code);
		return this;
	}

	public ClassGenerator addMethod(String name, int mod, Class<?> rt, Class<?>[] pts, String body) {
		return addMethod(name, mod, rt, pts, null, body);
	}

	public ClassGenerator addMethod(String name, int mod, Class<?> rt, Class<?>[] pts, Class<?>[] ets, String body) {
		StringBuilder sb = new StringBuilder();
		sb.append(ReflectUtils.modifier(mod)).append(' ').append(ReflectUtils.getName(rt)).append(' ').append(name);
		sb.append('(');
		for (int i = 0; i < pts.length; i++) {
			if (i > 0)
				sb.append(',');
			sb.append(ReflectUtils.getName(pts[i]));
			sb.append(" arg").append(i);
		}
		sb.append(')');
		if (ets != null && ets.length > 0) {
			sb.append(" throws ");
			for (int i = 0; i < ets.length; i++) {
				if (i > 0)
					sb.append(',');
				sb.append(ReflectUtils.getName(ets[i]));
			}
		}
		sb.append('{').append(body).append('}');
		return addMethod(sb.toString());
	}

	public ClassGenerator addMethod(Method m) {
		addMethod(m.getName(), m);
		return this;
	}

	public ClassGenerator addMethod(String name, Method m) {
		String desc = name + ReflectUtils.getDescWithoutMethodName(m);
		addMethod(':' + desc);
		if (ctCopyMethods == null)
			ctCopyMethods = new ConcurrentHashMap<String, Method>(8);
		ctCopyMethods.put(desc, m);
		return this;
	}

	public ClassGenerator addConstructor(String code) {
		if (ctConstructors == null)
			ctConstructors = new LinkedList<String>();
		ctConstructors.add(code);
		return this;
	}

	public ClassGenerator addConstructor(int mod, Class<?>[] pts, String body) {
		return addConstructor(mod, pts, null, body);
	}

	public ClassGenerator addConstructor(int mod, Class<?>[] pts, Class<?>[] ets, String body) {
		StringBuilder sb = new StringBuilder();
		sb.append(ReflectUtils.modifier(mod)).append(' ').append(SIMPLE_NAME_TAG);
		sb.append('(');
		for (int i = 0; i < pts.length; i++) {
			if (i > 0)
				sb.append(',');
			sb.append(ReflectUtils.getName(pts[i]));
			sb.append(" arg").append(i);
		}
		sb.append(')');
		if (ets != null && ets.length > 0) {
			sb.append(" throws ");
			for (int i = 0; i < ets.length; i++) {
				if (i > 0)
					sb.append(',');
				sb.append(ReflectUtils.getName(ets[i]));
			}
		}
		sb.append('{').append(body).append('}');
		return addConstructor(sb.toString());
	}

	public ClassGenerator addConstructor(Constructor<?> c) {
		String desc = ReflectUtils.getDesc(c);
		addConstructor(":" + desc);
		if (ctCopyConstructors == null)
			ctCopyConstructors = new ConcurrentHashMap<String, Constructor<?>>(4);
		ctCopyConstructors.put(desc, c);
		return this;
	}

	public ClassGenerator addDefaultConstructor() {
		ctDefaultConstructor = true;
		return this;
	}

	public ClassPool getClassPool() {
		return pool;
	}

	public Class<?> toClass() {
		return toClass(getClass().getClassLoader(), getClass().getProtectionDomain());
	}

	public Class<?> toClass(ClassLoader loader, ProtectionDomain pd) {
		if (ctClass != null)
			ctClass.detach();
		long id = CLASS_NAME_COUNTER.getAndIncrement();
		try {
			CtClass ctSuperClass = ctSuperClassName == null ? null : pool.get(ctSuperClassName);
			if (ctClassName == null)
				ctClassName = (ctSuperClassName == null || javassist.Modifier.isPublic(ctSuperClass.getModifiers()) ? ClassGenerator.class.getName() : ctSuperClassName + "$sc") + id;
			ctClass = pool.makeClass(ctClassName);
			if (ctSuperClassName != null)
				ctClass.setSuperclass(ctSuperClass);
			ctClass.addInterface(pool.get(DC.class.getName())); // add dynamic class tag.
			if (ctInterfaces != null)
				for (String cl : ctInterfaces)
					ctClass.addInterface(pool.get(cl));
			if (ctFields != null)
				for (String code : ctFields)
					ctClass.addField(CtField.make(code, ctClass));
			if (ctMethods != null) {
				for (String code : ctMethods) {
					if (code.charAt(0) == ':')
						ctClass.addMethod(CtNewMethod.copy(getCtMethod(ctCopyMethods.get(code.substring(1))), code.substring(1, code.indexOf('(')), ctClass, null));
					else
						ctClass.addMethod(CtNewMethod.make(code, ctClass));
				}
			}
			if (ctDefaultConstructor)
				ctClass.addConstructor(CtNewConstructor.defaultConstructor(ctClass));
			if (ctConstructors != null) {
				for (String code : ctConstructors) {
					if (code.charAt(0) == ':') {
						ctClass.addConstructor(CtNewConstructor.copy(getCtConstructor(ctCopyConstructors.get(code.substring(1))), ctClass, null));
					} else {
						String[] sn = ctClass.getSimpleName().split("\\$+"); // inner class name include $.
						ctClass.addConstructor(CtNewConstructor.make(code.replaceFirst(SIMPLE_NAME_TAG, sn[sn.length - 1]), ctClass));
					}
				}
			}
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream("D:\\javaclassses\\" + this.ctClassName);
				byte[] bytecode = ctClass.toBytecode();
				fos.write(bytecode);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return ctClass.toClass(loader, pd);
		} catch (RuntimeException e) {
			throw e;
		} catch (NotFoundException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (CannotCompileException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public void release() {
		if (ctClass != null)
			ctClass.detach();
		if (ctInterfaces != null)
			ctInterfaces.clear();
		if (ctFields != null)
			ctFields.clear();
		if (ctMethods != null)
			ctMethods.clear();
		if (ctConstructors != null)
			ctConstructors.clear();
		if (ctCopyMethods != null)
			ctCopyMethods.clear();
		if (ctCopyConstructors != null)
			ctCopyConstructors.clear();
	}

	private CtClass getCtClass(Class<?> c) throws NotFoundException {
		return pool.get(c.getName());
	}

	private CtMethod getCtMethod(Method m) throws NotFoundException {
		return getCtClass(m.getDeclaringClass()).getMethod(m.getName(), ReflectUtils.getDescWithoutMethodName(m));
	}

	private CtConstructor getCtConstructor(Constructor<?> c) throws NotFoundException {
		return getCtClass(c.getDeclaringClass()).getConstructor(ReflectUtils.getDesc(c));
	}
}
