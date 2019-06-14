package com.hzecool.fdn.utils.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.apache.log4j.Logger;

import com.hzecool.fdn.FdnCommonLogger;
import com.hzecool.fdn.bean.Pair;
import com.hzecool.fdn.exception.utils.ExceptionUtil;
import com.hzecool.fdn.utils.CollectionUtils;
import com.hzecool.fdn.utils.date.DateFormatUtils;

/**
 * 反射工具类.
 * 提供调用getter/setter方法, 访问私有变量, 调用私有方法, 获取泛型类型Class, 被AOP过的真实类等工具函数.
 * 
 * Type和反射+泛型有关的接口类型,直接子接口有：ParameterizedType，GenericArrayType，TypeVariable和WildcardType四种类型的接口
 * 	--ParameterizedType: 表示一种参数化的类型，比如Collection<T>， SomeClass<T>,其中raw类型是Collection和SomeClass
 * 	--GenericArrayType: 表示一种元素类型是数组类型
 * -- TypeVariable: 是各种类型变量的公共父接口，例如上面的T
 * -- WildcardType: 代表一种通配符类型表达式，比如?, ? extends Number, ? super Integer【wildcard是一个单词：就是“通配符”】
 * 
 * Type直接实现子类 :Class类
 */
public class Reflections {
	private static final String SETTER_PREFIX = "set";

	private static final String GETTER_PREFIX = "get";

	private static final String CGLIB_CLASS_SEPARATOR = "$$";

	private static Logger logger = FdnCommonLogger.log;

	/**
	 * 调用Getter方法.
	 */
	public static Object invokeGetter(Object obj, String propertyName) {
		String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(propertyName);
		return invokeMethod(obj, getterMethodName, new Class[] {}, new Object[] {});
	}

	/**
	 * 调用Setter方法, 仅匹配方法名。
	 * 可以忽略存取权限，但不能转类型
	 */
	public static void invokeSetter(Object obj, String propertyName, Object value) {
		String setterMethodName = SETTER_PREFIX + StringUtils.capitalize(propertyName);
		invokeMethodByName(obj, setterMethodName, new Object[] { value });
	}

	/**
	 * 设置属性值.
	 * 可以转类型，但只能用public方法
	 *  
	 * @author fengdg  
	 * @param bean
	 * @param name
	 * @param value
	 */
	public static void setProperty(Object bean, String name, Object value) {
		if (bean == null || value == null) {
			return;
		}
		Field field = getField(bean.getClass(), name);
		if (field == null) {
			return;
		}
		//Date类型直接设置属性
		if (!field.getType().isAssignableFrom(value.getClass()) && field.getType().equals(Date.class)) {
			Date date = DateFormatUtils.parse(value.toString());
			setFieldValue(bean, name, date);
			return;
		}
		try {
			BeanUtils.setProperty(bean, name, value);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("ex:" + e.getMessage());
		}
	}


	/**
	 * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
	 */
	public static Object getFieldValue(final Object obj, final String fieldName) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}

		Object result = null;
		try {
			result = field.get(obj);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常{}", e);
		}
		return result;
	}

	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 */
	public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}

		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常:{}", e);
		}
	}

	/**
	 * 直接调用对象方法, 无视private/protected修饰符.
	 * 用于一次性调用的情况，否则应使用getAccessibleMethod()函数获得Method后反复调用.
	 * 同时匹配方法名+参数类型，
	 */
	public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,
			final Object[] args) {
		Method method = getAccessibleMethod(obj, methodName, parameterTypes);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
		}

		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 直接调用对象方法, 无视private/protected修饰符，
	 * 用于一次性调用的情况，否则应使用getAccessibleMethodByName()函数获得Method后反复调用.
	 * 只匹配函数名，如果有多个同名函数调用第一个。
	 */
	public static Object invokeMethodByName(final Object obj, final String methodName, final Object[] args) {
		Method method = getAccessibleMethodByName(obj, methodName);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
		}

		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.
	 * 
	 * 如向上转型到Object仍无法找到, 返回null.
	 */
	public static Field getAccessibleField(final Object obj, final String fieldName) {
		Validate.notNull(obj, "object can't be null");
		Validate.notBlank(fieldName, "fieldName can't be blank");
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				makeAccessible(field);
				return field;
			} catch (NoSuchFieldException e) {// NOSONAR
				// Field不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
	 * 如向上转型到Object仍无法找到, 返回null.
	 * 匹配函数名+参数类型。
	 * 
	 * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
	 */
	public static Method getAccessibleMethod(final Object obj, final String methodName,
			final Class<?>... parameterTypes) {
		Validate.notNull(obj, "object can't be null");
		Validate.notBlank(methodName, "methodName can't be blank");

		for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
			try {
				Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
				makeAccessible(method);
				return method;
			} catch (NoSuchMethodException e) {
				// Method不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
	 * 如向上转型到Object仍无法找到, 返回null.
	 * 只匹配函数名。
	 * 
	 * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
	 */
	public static Method getAccessibleMethodByName(final Object obj, final String methodName) {
		Validate.notNull(obj, "object can't be null");
		Validate.notBlank(methodName, "methodName can't be blank");

		for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
			Method[] methods = searchType.getDeclaredMethods();
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					makeAccessible(method);
					return method;
				}
			}
		}
		return null;
	}

	/**
	 * 改变private/protected的方法为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
	 */
	public static void makeAccessible(Method method) {
		if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
				&& !method.isAccessible()) {
			method.setAccessible(true);
		}
	}

	/**
	 * 改变private/protected的成员变量为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
	 */
	public static void makeAccessible(Field field) {
		if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier
				.isFinal(field.getModifiers())) && !field.isAccessible()) {
			field.setAccessible(true);
		}
	}
	
	/**
	 * 获取ifc的所有实现了rootIfc的接口集
	 *  
	 * @author fengdg  
	 * @param clazz
	 * @param rootIfc
	 * @return
	 */
	public static Collection<Class<?>> getInterfaces(Class<?> clazz, Class<?> rootIfc) {
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
	//==========================================开始： 关于泛型部分 update by zhangyz 20170624=================================

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
					ParameterizedType paramType = (ParameterizedType)genType;
					Class<?> rawClass = (Class<?>)paramType.getRawType();
					TypeVariable<?>[] typeParams = rawClass.getTypeParameters();
					Type[] typeArgs = paramType.getActualTypeArguments();
					
					for (int i = 0; i < typeParams.length; i++) {
			            final Type typeArg = typeArgs[i];
			            typeVarAssigns.put(typeParams[i], typeVarAssigns.containsKey(typeArg) ? typeVarAssigns
			                    .get(typeArg) : typeArg);
			        }
					if (rawClass.equals(gdClass)) {
						List<Class<?>> result = new ArrayList<Class<?>>();
						for (int i = 0; i < typeParams.length; i++) {
							Type type = typeVarAssigns.get(typeParams[i]);
							if (type instanceof Class) {
								result.add((Class<?>)type);
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
			logger.debug("is not ParameterizedType");
			return (Class<T>)Object.class;
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
			logger.warn("Index: " + index + ", Size of Parameterized Type: "
					+ params.length);
			return Object.class;
		}
		Type paramType = params[index];
		if (paramType instanceof Class)
			return (Class<?>) paramType;
		else {
			logger.warn(" not set the actual class on superclass generic parameter");
			return Object.class;
		}
	}

	public static Type[] getFieldActualTypeArguments(Field field) {
		Type fieldType = field.getGenericType();
		if (fieldType instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType)fieldType;
			Type[] types = pt.getActualTypeArguments();
			return types;
		}
		return null;
	}
	
	/**
	 * 获取field的具体类型，注意该field可能位于某个父类中，而参数中的factClazz为具体子类。
	 * 并且若field是集合类、数组类类型，则返回的是其元素的类型。
	 * 并且支持field自己类型或元素类型是泛型，则进一步返回的是泛型的具体类型，定义泛型的位置一般位于factClazz的父类中。
	 * 例如，有一个父类，其中定义了泛型T
	 * public class HibernateDao<T> {
	 * 	private T user;
	 *  private Integer status;
	 *  
	 *  public T getUser() {
	 *  	return user;
	 *  }
	 * 	publiv void setUser(T user) {
	 * 		this.user = user;
	 * 	}
	 * };
	 * 
	 * //然后定义了一个子类，并且实例化了父类的泛型T为User类型。
	 * public class UserDao extends HibernateDao<User> {
	 * 	.....
	 * }
	 * 
	 * @param field HibernateDao中的user属性，其类型为T
	 * @param factClazz 指UserDao，最终子类。
	 * @author zhangyz
	 * @return 返回field的具体类型或其单个元素的类型,如果是泛型但未定义返回Object.class。
	 */
	@SuppressWarnings("rawtypes")
	public static Class getFieldGenricType(Field field, Class<?> factClazz) {
		FieldAtomType atomType = getFieldAtomType(field.getGenericType(), factClazz);
		return atomType.getFieldType();
	}

	/**
	 * 获取一个泛型的最终类型，如果本身是集合或数组类的，那么也会告知该信息。
	 * @param genericType 泛型对象
	 * @param factClazz 该泛型对象所处的最终子类。参见getFieldGenricType描述。
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public static FieldAtomType getFieldAtomType(Type genericType, Class<?> factClazz) {
		FieldAtomType atomType = null;
		if (genericType instanceof Class) {
			Class cls = (Class)genericType;
			if (cls.isArray()) {
				//数组类型，支持其单元素类型为泛型
				Class subCls = cls.getComponentType();
				atomType = new FieldAtomType(subCls, 2);
			}
			else
				atomType = new FieldAtomType(cls);
		}
		else if (genericType instanceof ParameterizedType) {//多级泛型，如List<>
			ParameterizedType pt = (ParameterizedType)genericType;
			Type rawType = pt.getRawType();
			if (rawType instanceof Class) {
				Class rawClass = (Class)rawType;
				if (Collection.class.isAssignableFrom(rawClass) ) {
					//是集合类多级泛型
					int fSetType = 1;
					Type subType = pt.getActualTypeArguments()[0];
					if (subType instanceof Class) {
						atomType = new FieldAtomType((Class)subType, fSetType);
					}
					else {
						FieldAtomType subAtomType = getFieldAtomType(subType, factClazz);
						atomType = genCurAtomType(fSetType, subAtomType);
					}
				}
				else {
					//不是Collection类的，作为普通类型
					atomType = new FieldAtomType(rawClass);
				}
			}
			else {
				//不知道什么分支
				atomType = new FieldAtomType(Object.class);
			}
		}
		else if (genericType instanceof GenericArrayType) {//数组泛型,如 T[]
			int fSetType = 2;
			GenericArrayType gat = (GenericArrayType)genericType;
			Type subType = gat.getGenericComponentType();
			if (subType instanceof Class) {
				atomType = new FieldAtomType((Class)subType, fSetType);
			}
			else {
				FieldAtomType subAtomType = getFieldAtomType(subType, factClazz);
				atomType = genCurAtomType(fSetType, subAtomType);
			}
		}
		else if (genericType instanceof TypeVariable) {//泛型擦拭对象，需要upper转型   
			atomType = getTypeVariableGenericType((TypeVariable)genericType, factClazz);
		}
		else {//兜底
			atomType = new FieldAtomType(Object.class);
		}

		return atomType;		
	}
	
	/**
	 * 针对一个TypeVariable，获取具体的泛型定义。
	 * @param tv
	 * @param factClazz 该参数值得推敲 TODO--
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	private static FieldAtomType getTypeVariableGenericType(TypeVariable fieldTv, Class<?> factClazz) {
		String tName = fieldTv.getName();
		GenericDeclaration gd = fieldTv.getGenericDeclaration();//这个属性泛型是在哪里定义的，一般是当前类
		TypeVariable[] tvs = gd.getTypeParameters();
		for (int ii = 0; ii < tvs.length; ii++) {
			TypeVariable gdtv = tvs[ii];
			if (gdtv.getName().equals(tName) && (gd instanceof Class)) {//泛型原型是在类里定义的，不是方法等
				ParameterizedType paramType = findFactGenericSuperClsInterface((Class)gd, factClazz);
				if (paramType != null) {
					Type[] params = paramType.getActualTypeArguments();
					Type theParam = params[ii];//ii对应上面索引
					FieldAtomType atomType = getFieldAtomType(theParam, factClazz);
					return atomType;
				}
				break;
			}
		}
		return new FieldAtomType(Object.class);
	}

	@SuppressWarnings({ "rawtypes" })
	private static FieldAtomType genCurAtomType(int fSetType, FieldAtomType subAtomType) {
		if (subAtomType.isSetType())
			return new FieldAtomType(subAtomType, fSetType);
		else {
			//上次是TypeVariable类型,归并一下；不要搞成嵌套了
			Class subCls = subAtomType.getFieldType();
			return new FieldAtomType(subCls, fSetType);
		}
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
					ParameterizedType paramType = (ParameterizedType)genType;
					Class rawClass = (Class)paramType.getRawType();
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
				ParameterizedType paramType = (ParameterizedType)genType;
				Class rawClass = (Class)paramType.getRawType();
				if (rawClass.equals(gdClass)) {
					return paramType;
				}
				factClazz = factClazz.getSuperclass();
				/*if (factClazz != null) {
					//此次应该也加上更全面,未测试
					ParameterizedType pt = findFactGenericSuperInterface(gdClass, factClazz);
					if (pt != null)
						return pt;
				}*/
			}
			else {
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
	
	//==========================================结束： 关于泛型部分=================================
	
	public static Class<?> getUserClass(Object instance) {
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
	 * 将反射时的checked exception转换为unchecked exception.
	 */
	public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
		if ((e instanceof IllegalAccessException) || (e instanceof IllegalArgumentException)
				|| (e instanceof NoSuchMethodException)) {
			return new IllegalArgumentException(e);
		} else if (e instanceof InvocationTargetException) {
			return new RuntimeException(((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		return new RuntimeException("Unexpected Checked Exception.", e);
	}

    /******************************************************************************/
    /***********************************Field*************************************/
    /******************************************************************************/
	/**
     * 获取clazz所有字段，列表形式.
	 * <li>范围：含所有的存取级别；包括类字段和实例字段</li>
	 *  
	 * @author fengdg  
	 * @param clazz
	 * @return
	 */
    public static Map<String, List<Field>> getFieldMap(final Class<?> clazz) {
    	Map<String, List<Field>> fieldMap = new HashMap<String, List<Field>>();
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			Field[] fields = superClass.getDeclaredFields();
			for (Field field : fields) {
	    		List<Field> fieldList = fieldMap.get(field.getName());
	    		if (fieldList == null) {
	    			fieldList = new ArrayList<Field>();
	    			fieldMap.put(field.getName(), fieldList);
				}
	    		fieldList.add(field);
			}
		}
		return fieldMap;
	}
    
    /**
     * 获取clazz所有字段，平铺形式.
	 * <li>范围：含所有的存取级别；包括类字段和实例字段</li>
     * <li>平铺：子类字段覆盖同名父类字段</li>
     *  
     * @author fengdg  
     * @param clazz
     * @return
     */
    public static Map<String, Field> getFieldFlatMap(final Class<?> clazz) {
    	Map<String, Field> fieldMap = new HashMap<String, Field>();
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			Field[] fields = superClass.getDeclaredFields();
			for (Field field : fields) {
				//子类优先
				if (!fieldMap.containsKey(field.getName())) {
					fieldMap.put(field.getName(), field);
				}
			}
		}
		return fieldMap;
	}
    
	/**
     * 获取clazz所有字段，平铺形式.
	 * <li>范围：含所有的存取级别；包括类字段和实例字段</li>
     * <li>平铺：子类字段覆盖同名父类字段</li>
	 *  
	 * @author fengdg  
	 * @param clazz
	 * @return
	 * @deprecated
	 * @see getFieldFlatMap(final Class<?> clazz)
	 */
    public static Map<String, Field> getAllFieldMap(final Class<?> clazz) {
    	return getFieldFlatMap(clazz);
	}
	
	/**
	 * 根据字段名返回Field，不存在返回null.
	 * <li>范围：含所有的存取级别；包括类字段和实例字段</li>
	 *  
	 * @author fengdg  
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static Field getField(Class<?> clazz, String fieldName ) {
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				return field;
			} catch (NoSuchFieldException | SecurityException e) {
//				logger.warn(clazz.getName() + " don't have field " + fieldName);
			}
		}
		return null;
	}
	
	/**
	 * 判断类型中是否存在某字段，每次都会重新组装fieldMap，不适合放在循环中使用
	 *  
	 * @author qiuxs  | fengdg
	 * @param name
	 * @param clz
	 * @return
	 * @deprecated
	 * @see containsFiled(Class<?> clz, String name)
	 */
	public static boolean containsFiled(String name, Class<?> clz) {
		return containsFiled(clz, name);
	}
	
	public static boolean containsFiled(Class<?> clz, String name) {
		return getField(clz, name) != null;
	}
	
	/**
     * 获取clazz所有字段的类型Map，平铺形式.
	 * <li>范围：含所有的存取级别；包括类字段和实例字段</li>
     * <li>平铺：子类字段覆盖同名父类字段</li>
	 *  
	 * @author fengdg  
	 * @param clazz
	 * @return
	 */
	public static Map<String, Class<?>> getFieldTypeMap(Class<?> clazz) {
		Map<String, Class<?>> fieldTypeMap = new HashMap<String, Class<?>>();
		Map<String, Field> fieldMap = getFieldFlatMap(clazz);
		for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
			fieldTypeMap.put(entry.getKey(), entry.getValue().getType());
		}
		return fieldTypeMap;
	}
	
	public static Map<String, Type[]> getFieldActualTypeArgumentsMap(Class<?> clazz) {
		Map<String, Type[]> fieldTypeMap = new HashMap<String, Type[]>();
		Map<String, Field> fieldMap = getFieldFlatMap(clazz);
		for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
			Field field = entry.getValue();
			fieldTypeMap.put(entry.getKey(), getFieldActualTypeArguments(field));
		}
		return fieldTypeMap;
	}
    /******************************************************************************/
    /***********************************Method*************************************/
    /******************************************************************************/
    /**
	 * 获取clazz的所有方法，列表形式.
	 * <li>范围：含所有的存取级别；包括类方法和实例方法</li>
     *  
     * @author fengdg  
     * @param clazz
     * @return
     */
    public static Map<String, List<Method>> getMethodMap(final Class<?> clazz) {
    	Map<String, List<Method>> methodMap = new HashMap<String, List<Method>>();
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			Method[] methods = superClass.getDeclaredMethods();
			for (Method method : methods) {
	    		List<Method> methodList = methodMap.get(method.getName());
	    		if (methodList == null) {
	    			methodList = new ArrayList<Method>();
	    			methodMap.put(method.getName(), methodList);
				}
	    		methodList.add(method);
			}
		}
		return methodMap;
	}
    
    /**
	 * 获取clazz的所有方法，平铺形式，重载和重写方法只有一个.
	 * <li>范围：含所有的存取级别；包括类方法和实例方法</li>
	 * <li>平铺：重载(Overload方法名相同，参数不同)方法会相互覆盖；重写（Override）方法用子类的</li>
     * 
     * @author fengdg  
     * @param clazz
     * @return
     */
    public static Map<String, Method> getMethodFlatMap(final Class<?> clazz) {
    	Map<String, Method> methodMap = new HashMap<String, Method>();
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			Method[] methods = superClass.getDeclaredMethods();
			for (Method method : methods) {
				//子类优先
				if (!methodMap.containsKey(method.getName())) {
					methodMap.put(method.getName(), method);
				}
			}
		}
		return methodMap;
	}

	/**
	 * 获取clazz的methodName(**)方法，返回多个重载方法中的任一个.
	 * <li>范围：含所有的存取级别；包括类方法和实例方法</li>
	 * <li>模糊：忽略参数</li>
	 *  
	 * @author fengdg  
	 * @param clazz
	 * @param methodName
	 * @return
	 */
	public static Method getMethodWithoutParam(Class<?> clazz, String methodName) {
		Map<String, Method> methodFlatMap = getMethodFlatMap(clazz);
		return methodFlatMap.get(methodName);
	}
	
    /**
     * 获取clazz的methodName(parameterTypes)方法.
	 * <li>范围：含所有的存取级别；包括类方法和实例方法</li>
     * <li>精确：完全匹配参数</li>
     *  
     * @author fengdg  
     * @param clazz
     * @param methodName
     * @param parameterTypes
     * @return
     */
	public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Method method = superClass.getDeclaredMethod(methodName, parameterTypes);
				return method;
			} catch (SecurityException | NoSuchMethodException e) {
				logger.warn(clazz.getName() + " don't have method " + methodName);
			}
		}
		return null;
	}
	
	/**
	 * 获取Method，不存在方法时返回null
	 *  
	 * @author fengdg  
	 * @param object
	 * @param methodName
	 * @param args
	 * @return
	 */
	public static Method getMethod(Object object, String methodName, Object... args) throws NoSuchMethodException {
        args = ArrayUtils.nullToEmpty(args);
        final Class<?>[] parameterTypes = ClassUtils.toClass(args);
        final Method method = MethodUtils.getMatchingAccessibleMethod(object.getClass(),
                methodName, parameterTypes);
        if (method == null) {
            throw new NoSuchMethodException("No such accessible method: "
                    + methodName + "() on object: "
                    + object.getClass().getName());
        }
        return method;
	}
	
	/*******************************公用方法***********************************/
    /**
     * 获取clazz所有的public方法
	 * <li>范围：public方法；包括类方法和实例方法</li>
     *  
     * @author fengdg  
     * @param clazz
     * @return
     */
    public static Map<String, List<Method>> getPublicMethodMap(final Class<?> clazz) {
    	Map<String, List<Method>> methodMap = new HashMap<String, List<Method>>();
    	Method[] methods = clazz.getMethods();
    	for (Method method : methods) {
    		List<Method> methodList = methodMap.get(method.getName());
    		if (methodList == null) {
    			methodList = new ArrayList<Method>();
    			methodMap.put(method.getName(), methodList);
			}
    		methodList.add(method);
		}
		return methodMap;
	}	
    
	/**
	 * 获取clazz的methodName(**)方法，返回多个重载方法中的任一个.
	 * <li>范围：public方法；包括类方法和实例方法</li>
	 * <li>模糊：忽略参数</li>
	 *  
	 * @author fengdg  
	 * @param clazz
	 * @param methodName
	 * @return
	 */
	public static Method getPublicMethodWithoutParam(Class<?> clazz, String methodName) {
		Map<String, List<Method>> methodPublicMap = getPublicMethodMap(clazz);
		List<Method> methodList = methodPublicMap.get(methodName);
		if (methodList == null || methodList.size() == 0) {
			return null;
		} else {
			return methodList.get(0);
		}
	}
	
    /**
     * 获取clazz的methodName(parameterTypes)方法.
	 * <li>范围：public方法；包括类方法和实例方法</li>
     * <li>精确：完全匹配参数</li>
     *  
     * @author fengdg  
     * @param clazz
     * @param methodName
     * @param parameterTypes
     * @return
     */
	public static Method getPublicMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
		try {
			return clazz.getMethod(methodName, parameterTypes);
		} catch (SecurityException | NoSuchMethodException e) {
			logger.debug(clazz.getName() + " don't have method " + methodName);
		}
		return null;
	}	
	
	/**
	 * 获取getter方法名的Map
	 * <li>范围：public方法；包括类方法和实例方法</li>
	 * 
	 * @author fengdg  
	 * @param clazz
	 * @return
	 */
	public static Map<String, String> getGetterMethodNameMap(Class<?> clazz) {
		Map<String, String> propGetterMap = new HashMap<String, String>(); 
		Map<String, Field> fieldMap = getFieldFlatMap(clazz);
		for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
			String propName = entry.getKey();
//			if (Modifier.isStatic(entry.getValue().getModifiers())) {continue;}
			propGetterMap.put(propName, getGetterMethodName(clazz, propName));
		}
		return propGetterMap;
	}
    
	/**
	 * 获取getter方法名
	 * <li>范围：public方法；包括类方法和实例方法</li>
	 * 
	 * 
	 * @author fengdg  
	 * @param propertyName
	 * @return
	 */
	public static String getGetterMethodName(Class<?> clazz, String propertyName ) {
		String getterName = GETTER_PREFIX + StringUtils.capitalize(propertyName);
		Method method = getPublicMethod(clazz, getterName);
		if (method == null) {
			getterName = GETTER_PREFIX + propertyName;
			method = getPublicMethod(clazz, getterName);
		}
		if (method != null) {
			return method.getName();
		}
		return null;
	}
	
	/**
	 * 获取setter方法名的Map
	 * <li>范围：public方法；包括类方法和实例方法</li>
	 * 
	 * @author fengdg  
	 * @param clazz
	 * @return
	 */
	public static Map<String, String> getSetterMethodNameMap(Class<?> clazz) {
		Map<String, String> propSetterMap = new HashMap<String, String>(); 
		Map<String, Field> fieldMap = getFieldFlatMap(clazz);
		for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
			String propName = entry.getKey();
			Field field = entry.getValue();
//			if (Modifier.isStatic(field.getModifiers())) {continue;}
			propSetterMap.put(propName, getSetterMethodName(clazz, propName, field.getType()));
		}
		return propSetterMap;
	}
    
	/**
	 * 获取setter方法名
	 * <li>范围：public方法；包括类方法和实例方法</li>
	 * 
	 * @author fengdg  
	 * @param propertyName
	 * @return
	 */
	public static String getSetterMethodName(Class<?> clazz, String propertyName) {
		String setterName = SETTER_PREFIX + StringUtils.capitalize(propertyName);
		Method method = getPublicMethodWithoutParam(clazz, setterName);
		if (method == null) {
			setterName = SETTER_PREFIX + propertyName;
			method = getPublicMethodWithoutParam(clazz, setterName);
		}
		if (method != null) {
			return method.getName();
		}
		return null;
	}
	/**
	 * 获取setter方法名
	 *  
	 * @author fengdg  
	 * @param propertyName
	 * @return
	 */
	public static String getSetterMethodName(Class<?> clazz, String propertyName, Class<?> propType ) {
		String setterName = SETTER_PREFIX + StringUtils.capitalize(propertyName);
		try {
			clazz.getMethod(setterName, propType);
			return setterName;
		} catch (NoSuchMethodException | SecurityException e) {
			setterName = SETTER_PREFIX + propertyName;
			try {
				clazz.getMethod(setterName, propType);
				return setterName;
			} catch (NoSuchMethodException | SecurityException e1) {
				logger.debug(clazz.getName() + " don't have method " + setterName);
			}
			logger.debug(clazz.getName() + " don't have method " + setterName);
		}
		return "";
	}
	
	/**
	 * 获取方法签名 
	 *  
	 * @author fengdg  
	 * @param method
	 * @return
	 */
    public static String getMethodSign(Method method) {
    	String clazzName = method.getClass().getName();
    	return clazzName + method.toGenericString();
    }
    
    /**
     * 简易方法签名
     *  
     * @author fengdg  
     * @param target
     * @param methodName
     * @param parameterTypes
     * @return
     */
    public static String getMethodSignLite(Object target, String methodName, Class<?>[] parameterTypes) {
    	return getMethodSignLite(target.getClass(), methodName, parameterTypes);
    }
    
    /**
     * 简易方法签名
     *  
     * @author fengdg  
     * @param cls
     * @param methodName
     * @param parameterTypes
     * @return
     */
    public static String getMethodSignLite(Class<?> targetClass, String methodName, Class<?>[] parameterTypes) {
    	StringBuilder sb = new StringBuilder();
    	if (targetClass != null) {
			sb.append(targetClass.getName());
		}
    	if (methodName != null) {
			sb.append("#").append(methodName);
		}
    	return sb.toString();
    }

    public static Object getNullValue(Class<?> type) {

        if (boolean.class.equals(type)) {
            return false;
        } else if (byte.class.equals(type)) {
            return 0;
        } else if (short.class.equals(type)) {
            return 0;
        } else if (int.class.equals(type)) {
            return 0;
        } else if (long.class.equals(type)) {
            return 0;
        } else if (float.class.equals(type)) {
            return 0;
        } else if (double.class.equals(type)) {
            return 0;
        }

        return null;
    }
    

    public static Class<?> getDeclaringType(Class<?> aClass, String methodName, Class<?>[] parameterTypes) {

        Method method = null;


        Class<?> findClass = aClass;

        do {
            Class<?>[] clazzes = findClass.getInterfaces();

            for (Class<?> clazz : clazzes) {

                try {
                    method = clazz.getDeclaredMethod(methodName, parameterTypes);
                } catch (NoSuchMethodException e) {
                    method = null;
                }

                if (method != null) {
                    return clazz;
                }
            }

            findClass = findClass.getSuperclass();

        } while (!findClass.equals(Object.class));

        return aClass;
    }
    
    
    @SuppressWarnings("rawtypes")
	public static Class classForName(String className) {
    	try {
			Class cls = Class.forName(className);
			return cls;
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("className=" + className, e);
		}
    }
    
    @SuppressWarnings("unchecked")
	public static <T> T newInstance(Class<T> clazz, Object... args) {
    	List<Pair<Class<?>, Object>> pairs = new ArrayList<Pair<Class<?>, Object>>();
    	if (args == null || args.length == 0) {
    		
    	} else {
    		for (Object arg : args) {
    			if (arg == null) {
    				ExceptionUtil.throwLogicalException("null_param_unsupport");
    				return null;
    			}
    			pairs.add(new Pair<Class<?>, Object>(arg.getClass(), arg));
    		}
		}
    	return (T) newInstanceWithParamType(clazz, pairs.toArray(new Pair[0]));
    }
    
    public static <T> T newInstanceWithParamType(Class<T> clazz, @SuppressWarnings("unchecked") Pair<Class<?>, ?>... args) {
    	if (args == null || args.length == 0) {
			try {
				return clazz.newInstance();
			} catch (Throwable e) {
				FdnCommonLogger.log.error("ex : " + e.getMessage(), e);
			}
			return null;
		}
    	
    	Class<?>[] paramClazzArray = new Class<?>[args.length];
    	Object[] paramArray = new Object[args.length];
    	
    	for (int i = 0; i < args.length ; i++) {
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
			FdnCommonLogger.log.error("ex : " + e.getMessage(), e);
		}
		return null;
    }

    public static void main(String[] args) throws NoSuchMethodException {
    	List<String> props = new ArrayList<>();
    	System.out.println(props.getClass());
//    	Class<?> clazz1 = Reflections.getSuperClassGenricType(props.getClass(), 0);
//    	System.out.println(clazz1);
//    	Class<?> clazz2 = Reflections.getInterfaceGenericType(List.class, props.getClass(), 0);
//    	System.out.println(clazz2);
    	Map<TypeVariable<?>, Type> reMap = TypeUtils.getTypeArguments(props.getClass(), List.class);
    	System.out.println(reMap);

//    	Long tse = newInstance(Long.class, "123");
//    	Method method = getMethod(tse, "longValue", null);
//    	System.out.println(method.getClass().getName());
//		Map<String, String> map = new HashMap<String, String>();
//		Type genType = map.getClass().getGenericSuperclass();
//		Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
//		for (Type type : params) {
//			System.out.println(type);
//		}
//		Class<?> clz = getSuperClassGenricType(map.getClass(), 1);
//		System.out.println(clz);
	}
}
