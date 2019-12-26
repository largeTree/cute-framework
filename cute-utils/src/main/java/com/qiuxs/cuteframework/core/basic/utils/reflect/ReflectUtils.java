package com.qiuxs.cuteframework.core.basic.utils.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qiuxs.cuteframework.core.basic.bean.Pair;
import com.qiuxs.cuteframework.core.basic.utils.CollectionUtils;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;

/**
 * 反射相关工具
 * @author qiuxs
 * 
 * 创建时间 ： 2018年7月26日 下午10:21:35
 *
 */
public class ReflectUtils {

	private static final String CGLIB_CLASS_SEPARATOR = "$$";

	private static final Logger log = LogManager.getLogger(ReflectUtils.class);

	/**
	 * 获取factClazz实现gdInterface接口（可以间接实现）时所用的实际泛型类型
	 *  
	 * @author fengdg  
	 * @param gdInterface
	 * @param factClazz
	 * @param index
	 * @return
	 */
	public static Class<?> getInterfaceGenericType(Class<?> gdInterface, Class<?> factClazz, int index) {
		Map<TypeVariable<?>, Type> typeVarAssigns = new HashMap<TypeVariable<?>, Type>();
		List<Class<?>> list = getInterfaceGenericType(gdInterface, factClazz, typeVarAssigns);
		if (list == null || index >= list.size()) {
			return null;
		} else {
			return list.get(index);
		}
	}

	private static List<Class<?>> getInterfaceGenericType(Class<?> gdClass, Class<?> factClazz, Map<TypeVariable<?>, Type> typeVarAssigns) {
		//		Map<TypeVariable<?>, Type> typeVarAssigns = new HashMap<TypeVariable<?>, Type>();
		Type[] types = factClazz.getGenericInterfaces();

		if (CollectionUtils.isNotEmpty(types)) {
			for (Type genType : types) {
				if (genType instanceof ParameterizedType) {
					ParameterizedType paramType = (ParameterizedType) genType;
					Class<?> rawClass = (Class<?>) paramType.getRawType();
					TypeVariable<?>[] typeParams = rawClass.getTypeParameters();
					Type[] typeArgs = paramType.getActualTypeArguments();

					for (int i = 0; i < typeParams.length; i++) {
						final Type typeArg = typeArgs[i];
						typeVarAssigns.put(typeParams[i], typeVarAssigns.containsKey(typeArg) ? typeVarAssigns.get(typeArg) : typeArg);
					}
					if (rawClass.equals(gdClass)) {
						List<Class<?>> result = new ArrayList<Class<?>>();
						for (int i = 0; i < typeParams.length; i++) {
							Type type = typeVarAssigns.get(typeParams[i]);
							if (type instanceof Class) {
								result.add((Class<?>) type);
							} else {
								result.add(Object.class);
							}
						}
						return result;
					}
				}
			}
		}
		//直接实现的接口
		Class<?>[] ifcs = factClazz.getInterfaces();
		if (CollectionUtils.isNotEmpty(ifcs)) {
			for (Class<?> ifc : ifcs) {
				List<Class<?>> result = getInterfaceGenericType(gdClass, ifc, typeVarAssigns);
				if (result != null) {
					return result;
				}
			}
		}

		return null;
	}

	/**
	 * 通过反射, 获得Class定义中声明的泛型参数的类型, 注意泛型必须定义在直接父类处
	 * 如无法找到, 返回Object.class.
	 * eg.
	 * public class UserDao extends HibernateDao<User>
	 * 
	 * @param clazz The class to introspect
	 * @return the first generic declaration, or Object.class if cannot be determined
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getSuperClassGenricType(final Class<?> clazz, int... index) {
		Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			log.debug("is not ParameterizedType");
			return (Class<T>) Object.class;
		}
		int factIndex = 0;
		if (index != null && index.length > 0)
			factIndex = index[0];

		return (Class<T>) getGenricType(((ParameterizedType) genType), factIndex);
	}

	/**
	 * 通过反射, 获得Class定义中声明的泛型参数的类型, 注意泛型必须定义在名为gdClass的父类处（可能有多层继承）
	 * @param clazz
	 * @param gdClass 泛型定义宣称处
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getSuperClassGenricType(final Class<?> clazz, final Class<?> gdClass) {
		ParameterizedType pt = findFactGenericSuperClsInterface(gdClass, clazz);
		if (pt == null)
			return null;
		return (Class<T>) getGenricType(pt, 0);
	}

	/**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
	 * 如无法找到, 返回Object.class.
	 * 
	 * 如public UserDao extends HibernateDao<User,Long>
	 * 
	 * @param clazz clazz The class to introspect
	 * @param index the Index of the generic ddeclaration,start from 0.
	 * @return the index generic declaration, or Object.class if cannot be determined
	 */
	private static Class<?> getGenricType(ParameterizedType pt, final int index) {
		Type[] params = pt.getActualTypeArguments();

		if ((index >= params.length) || (index < 0)) {
			log.warn("Index: " + index + ", Size of Parameterized Type: " + params.length);
			return Object.class;
		}
		Type paramType = params[index];
		if (paramType instanceof Class)
			return (Class<?>) paramType;
		else {
			log.warn(" not set the actual class on superclass generic parameter");
			return Object.class;
		}
	}

	/** 
	 * 根据泛型宣称定义位置，从当前子类factClazz开始找到真正定义泛型所在的基类;因为有可能有多层继承。
	 * @param gd 泛型宣称定义位置，此次要求是类，不能是方法等
	 * @param factClazz 当前子类
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static ParameterizedType findFactGenericSuperclass(Class gdClass, Class<?> factClazz) {
		do {
			Type genType = factClazz.getGenericSuperclass();
			if (genType == null)
				break;
			if (genType instanceof ParameterizedType) {
				ParameterizedType paramType = (ParameterizedType) genType;
				Class rawClass = (Class) paramType.getRawType();
				if (rawClass.equals(gdClass)) {
					return paramType;
				}
				factClazz = factClazz.getSuperclass();
			} else {
				factClazz = factClazz.getSuperclass();
				//break;
			}
		} while (factClazz != null);
		return null;
	}

	/** 
	 * 根据泛型宣称定义位置，从当前子类factClazz开始找到真正定义泛型所在的接口或基类;因为有可能有多层继承。
	 * @param gd 泛型宣称定义位置，此次要求是类或接口，不能是方法等
	 * @param factClazz 当前子类
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static ParameterizedType findFactGenericSuperClsInterface(Class gdClass, Class<?> factClazz) {
		ParameterizedType pt = findFactGenericSuperclass(gdClass, factClazz);
		if (pt == null)
			return findFactGenericSuperInterface(gdClass, factClazz);
		else
			return pt;
	}

	/** 
	 * 根据泛型宣称定义位置，从当前子类factClazz开始找到真正定义泛型所在的接口;因为有可能有多层继承。
	 * @param gd 泛型宣称定义位置，此次要求是接口类，不能是方法等
	 * @param factClazz 当前子类
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static ParameterizedType findFactGenericSuperInterface(Class gdClass, Class<?> factClazz) {
		Type[] types = factClazz.getGenericInterfaces();
		if (CollectionUtils.isNotEmpty(types)) {
			for (Type genType : types) {
				if (genType instanceof ParameterizedType) {
					ParameterizedType paramType = (ParameterizedType) genType;
					Class rawClass = (Class) paramType.getRawType();
					if (rawClass.equals(gdClass)) {
						return paramType;
					}
				}
			}
		}
		//直接实现的接口
		Class<?>[] ifcs = factClazz.getInterfaces();
		if (CollectionUtils.isNotEmpty(ifcs)) {
			for (Class<?> ifc : ifcs) {
				ParameterizedType pt = findFactGenericSuperInterface(gdClass, ifc);
				if (pt != null) {
					return pt;
				}
			}
		}
		//最终出口
		return null;
	}

	/**
	 * 判断类型是否为基础类型包装类
	 * @author qiuxs
	 *
	 * @param type
	 * @return
	 *
	 * 创建时间：2018年7月25日 下午9:34:12
	 */
	public static boolean isPrimitivePackagingClass(Class<?> type) {
		if (type.isAssignableFrom(Boolean.class) || type.isAssignableFrom(Byte.class) || type.isAssignableFrom(Character.class) || type.isAssignableFrom(Double.class) || type.isAssignableFrom(Float.class) || type.isAssignableFrom(Integer.class) || type.isAssignableFrom(Long.class) || type.isAssignableFrom(Short.class)) {
			return true;
		}
		return false;
	}

	/**
	 * 是否简单类型，包含基础数据类型及对应包装类和String
	 * @author qiuxs
	 *
	 * @param type
	 * @return
	 *
	 * 创建时间：2018年8月11日 下午5:14:12
	 */
	public static boolean isSimpleType(Class<?> type) {
		return type.isPrimitive() || isPrimitivePackagingClass(type) || type.isAssignableFrom(String.class);
	}

	/**
	 * 获取
	 * 
	 * 2019年6月15日 下午8:51:50
	 * @auther qiuxs
	 * @param instance
	 * @return
	 */
	public static Class<?> getCGLIBTargetClass(Object instance) {
		Validate.notNull(instance, "Instance must not be null");
		Class<?> clazz = instance.getClass();
		if ((clazz != null) && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
			Class<?> superClass = clazz.getSuperclass();
			if ((superClass != null) && !Object.class.equals(superClass)) {
				return superClass;
			}
		}
		return clazz;
	}

	/**
	 * 根据类的全名获取类对象
	 * 
	 * 2019年6月15日 下午8:50:14
	 * @auther qiuxs
	 * @param className
	 * @return
	 */
	public static Class<?> classForName(String className) {
		try {
			Class<?> cls = Class.forName(className);
			return cls;
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("className=" + className, e);
		}
	}

	/**
	 * 创建指定类型的实例，调用有参构造函数
	 * 
	 * 2019年6月15日 下午8:50:51
	 * @auther qiuxs
	 * @param clazz
	 * @param args
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(Class<T> clazz, Object... args) {
		List<Pair<Class<?>, Object>> pairs = new ArrayList<Pair<Class<?>, Object>>();
		if (args == null || args.length == 0) {

		} else {
			for (Object arg : args) {
				if (arg == null) {
					ExceptionUtils.throwLogicalException("null_param_unsupport");
					return null;
				}
				pairs.add(new Pair<Class<?>, Object>(arg.getClass(), arg));
			}
		}
		return (T) newInstanceWithParamType(clazz, pairs.toArray(new Pair[0]));
	}

	/**
	 * 根据参数列表匹配的构造函数实例化对象
	 * 
	 * 2019年6月15日 下午8:51:20
	 * @auther qiuxs
	 * @param clazz
	 * @param args
	 * @return
	 */
	public static <T> T newInstanceWithParamType(Class<T> clazz, @SuppressWarnings("unchecked") Pair<Class<?>, ?>... args) {
		if (args == null || args.length == 0) {
			try {
				return clazz.newInstance();
			} catch (Throwable e) {
				log.error("ex : " + e.getMessage(), e);
			}
			return null;
		}

		Class<?>[] paramClazzArray = new Class<?>[args.length];
		Object[] paramArray = new Object[args.length];

		for (int i = 0; i < args.length; i++) {
			Pair<Class<?>, ?> arg = args[i];
			paramClazzArray[i] = arg.getV1();
			paramArray[i] = arg.getV2();
		}
		Constructor<T> constructor;
		try {
			constructor = clazz.getConstructor(paramClazzArray);
			if (constructor != null) {
				return constructor.newInstance(paramArray);
			}
		} catch (Throwable e) {
			log.error("ex : " + e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 获取clazz的所有实现了rootIfc的接口集
	 * @param clazz
	 * @param rootIfc
	 */
	public static List<Class<?>> getInterfaces(Class<?> clazz, Class<?> rootIfc) {
		List<Class<?>> ifcList = new ArrayList<>();
		Class<?>[] ifcs = clazz.getInterfaces();
		for (Class<?> ifc : ifcs) {
			if (rootIfc.isAssignableFrom(ifc) && !rootIfc.equals(ifc)) {
				ifcList.add(ifc);
				ifcList.addAll(getInterfaces(ifc, rootIfc));
			}
		}
		return ifcList;
	}

}
