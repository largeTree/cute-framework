package com.qiuxs.cuteframework.core.basic.utils;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;

/**
 * 扩展Bean工具
 * @author qiuxs
 * 
 * 创建时间 ： 2018年7月26日 下午10:09:06
 *
 */
public class BeanUtil extends BeanUtils {

	private static Logger log = LogManager.getLogger(BeanUtil.class);

	/**
	 * 复制对象属性，忽略为Null的值
	 * @author qiuxs
	 *
	 * @param src
	 * @param target
	 * @param ignoreFields
	 *
	 * 创建时间：2018年7月26日 下午10:09:19
	 */
	public static void copyBeanIgnoreNullValue(Object src, Object target, String... ignoreFields) {
		if (src == null || target == null) {
			return;
		}
		PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(src.getClass());
		Set<String> setIgnoreFields = null;
		if (ignoreFields != null && ignoreFields.length > 0) {
			setIgnoreFields = new HashSet<>();
			for (String fieldName : ignoreFields) {
				setIgnoreFields.add(fieldName);
			}
		}
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			Method readMethod = propertyDescriptor.getReadMethod();
			Method writeMethod = propertyDescriptor.getWriteMethod();
			String fieldName = propertyDescriptor.getName();
			try {
				Object value = readMethod.invoke(src);
				if (value == null || setIgnoreFields.contains(fieldName)) {
					continue;
				}
				writeMethod.invoke(target, value);
			} catch (ReflectiveOperationException e) {
				log.warn("复制字段失败：" + fieldName, e);
			}
		}
	}

	/**
	 * 复制source的属性值到新建的相同类型的对象并返回.<br/>
	 *  
	 * @author fengdg  
	 * @param source
	 * @return
	 */
	public static <T extends Serializable> T deepCopyBean(T source) {
		return SerializationUtils.clone(source);
	}

	/**
	 * 复制source的属性值到新建的targetClass对象并返回.<br/>
	 * 1. targetClass必须包含默认构造函数<br/>
	 * 2. 采用apache的BeanUtils<br/>
	 * <br/>
	 * 备注：Apache BeanUtils和Spring BeanUtils的比较<br/>
	 *  1. apache对static class不能正确的拷贝属性值 <br/>
	 *  2. spring对source为EcEntity，target为Serializable的不能正确的拷贝id值<br/>
	 * 
	 * @author fengdg
	 * @param source
	 * @param targetClass
	 * @return 
	 */
	public static <T> T copyBean(Object source, Class<T> targetClass) {
		if (source == null || targetClass == null) {
			return null;
		}
		//新建targetClass对象
		T t = null;
		try {
			t = targetClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			log.error("ex: " + e.getMessage(), e);
			return null;
		}
		//复制属性
		try {
			org.apache.commons.beanutils.BeanUtils.copyProperties(t, source);
		} catch (IllegalAccessException | InvocationTargetException e) {
			log.debug("ex: " + e.getMessage(), e);
		}
		return t;
	}

	/**
	 * 复制source的属性值到新建的相同类型的对象并返回.<br/>
	 *  
	 * @author fengdg  
	 * @param source
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T copyBean(T source) {
		return (T) copyBean(source, source.getClass());
	}

	/**
	 * 复制source的属性值到target
	 *  
	 * @author fengdg  
	 * @param source
	 * @param target
	 */
	public static void copyProperties(Object source, Object target) {
		try {
			org.apache.commons.beanutils.BeanUtils.copyProperties(target, source);
		} catch (IllegalAccessException | InvocationTargetException e) {
			log.error("ex: " + e.getMessage(), e);
		}
	}

	public static void copyPropertiesInclude(Object src, Object target, Collection<String> include) {
		copyProperties(target, src, include, null);
	}

	/**
	 * 复制对象属性，支持map类型,只复制指定的属性
	 * @param target
	 * @param src
	 * @param include
	 * @throws Exception
	 */
	public static void copyPropertiesInclude(Object src, Object target, String... include) {
		Set<String> set_include = new HashSet<String>();
		for (String name : include) {
			set_include.add(name);
		}
		//		copyProperties(target, src , set_include, null);
		copyPropertiesInclude(src, target, set_include);
	}

	public static void copyPropertiesExclude(Object src, Object target, Collection<String> exclude) {
		copyProperties(target, src, null, exclude);
	}

	/**
	 * 复制对象属性，支持map类型,排除指定的属性
	 * @param src
	 * @param target
	 * @param exclude
	 */
	public static void copyPropertiesExclude(Object src, Object target, String... exclude) {
		Set<String> set_exclude = new HashSet<String>();
		for (String name : exclude) {
			set_exclude.add(name);
		}
		//		copyProperties(target, src , null, set_exclude);
		copyPropertiesExclude(src, target, set_exclude);
	}

	/**
	 * 取对象属性名列表
	 * @author lsh  
	 * @param bean 对象
	 * @return 对象属性名列表
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static List<String> getPropNames(Object bean) {
		List<String> propNames = new ArrayList<String>();

		//Map类型无法通过PropertyUtils获取属性列表
		if (bean instanceof Map) {
			Map mapVal = (Map) bean;
			for (Iterator<String> it = mapVal.keySet().iterator(); it.hasNext();) {
				propNames.add(it.next());
			}
		} else {
			PropertyDescriptor[] srcProps = PropertyUtils.getPropertyDescriptors(bean);
			//propNames = new String[arrProps.length];
			for (PropertyDescriptor propertyDescriptor : srcProps) {
				//只复制定义了getXXX和setXXX方法的属性
				if (propertyDescriptor.getReadMethod() != null && propertyDescriptor.getWriteMethod() != null) {
					propNames.add(propertyDescriptor.getName());
				}
			}
		}

		return propNames;
	}

	private static void copyProperties(Object target, Object src, Collection<String> include, Collection<String> exclude) {
		try {
			//源类属性集
			List<String> srcPropNames = getPropNames(src);
			//			List<String> srcPropNames = new ArrayList<String>();
			//			//Map类型无法通过PropertyUtils获取属性列表
			//			if(src instanceof Map){
			//				Map mapVal = (Map)src;
			//				for(Iterator<String> it = mapVal.keySet().iterator();it.hasNext();){
			//					srcPropNames.add(it.next());
			//				}
			//			}else{
			//				PropertyDescriptor[]  srcProps = PropertyUtils.getPropertyDescriptors(src);
			//				//propNames = new String[arrProps.length];
			//				for (PropertyDescriptor propertyDescriptor : srcProps) {
			//					//只复制定义了getXXX和setXXX方法的属性
			//					if(propertyDescriptor.getReadMethod()!= null && propertyDescriptor.getWriteMethod() != null){
			//						srcPropNames.add(propertyDescriptor.getName());
			//					}
			//				}
			//			}

			//目标类属性集
			List<String> targetPropNames = getPropNames(target);
			//			List<String> targetPropNames = new ArrayList<String>();
			//			PropertyDescriptor[]  targetProps = PropertyUtils.getPropertyDescriptors(target);
			//			for (PropertyDescriptor propertyDescriptor : targetProps) {
			//				if(propertyDescriptor.getReadMethod()!= null && propertyDescriptor.getWriteMethod() != null){
			//					targetPropNames.add(propertyDescriptor.getName());
			//				}
			//			}

			for (String key : srcPropNames) {
				if (!targetPropNames.contains(key)) {
					continue;
				}
				if (include != null && !include.contains(key) || exclude != null && exclude.contains(key)) {
					continue;
				}
				Object val = PropertyUtils.getProperty(src, key);
				PropertyUtils.setProperty(target, key, val);

			}
		} catch (Exception e) {
			throw new RuntimeException("copyProperties error", e);
		}
	}

	/**
	 * 不同对象的相同属性赋值
	 * 
	 * @author caoyz
	 * @param source
	 *            源数对象
	 * @param dest
	 *            目标对象
	 * @param exclude
	 *            需要排除的属性
	 */
	public static void assignmentProperty(Object source, Object dest, String exclude) {
		Set<String> excludeSet = new HashSet<String>();
		if (StringUtils.isNotEmpty(exclude)) {
			excludeSet.add(exclude);
		}
		copyPropertiesExclude(source, dest, excludeSet);
		//		try {
		//			// 获取属性
		//			BeanInfo sourceBean = Introspector.getBeanInfo(source.getClass(), java.lang.Object.class);
		//			PropertyDescriptor[] sourceProperty = sourceBean.getPropertyDescriptors();
		//
		//			BeanInfo destBean = Introspector.getBeanInfo(dest.getClass(), java.lang.Object.class);
		//			PropertyDescriptor[] destProperty = destBean.getPropertyDescriptors();
		//
		//			for (int i = 0; i < sourceProperty.length; i++) {
		//				for (int j = 0; j < destProperty.length; j++) {
		//					if (sourceProperty[i].getName().equals(destProperty[j].getName())
		//							&& !destProperty[j].getName().equals(exclude)) {
		//						// 调用source的getter方法和dest的setter方法
		//						destProperty[j].getWriteMethod().invoke(dest, sourceProperty[i].getReadMethod().invoke(source));
		//						break;
		//					}
		//				}
		//			}
		//		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
		//				| IntrospectionException e) {
		//			throw new RuntimeException(e);
		//		}
	}

	/**
	 * 判断两个相同类型的对象是否相等
	 *  
	 * @author fengdg  
	 * @param bean1
	 * @param bean2
	 * @return
	 */
	public static <T> boolean isEquals(T bean1, T bean2) {
		if (bean1 == null) {
			return (bean2 == null);
		} else if (bean2 == null) {
			return (bean1 == null);
		} else {
			return bean1.equals(bean2);
		}
	}

	public static <T> boolean isNotEquals(T bean1, T bean2) {
		return !isEquals(bean1, bean2);
	}

	/**
	 * 读取可选属性。
	 * -支持通过getter方法获取private的属性值；（但针对内部类有问题）
	 * 
	 * @author lsh  
	 * @param bean 待读取属性的Bean对象
	 * @param name 属性名
	 * @return 属性值。属性不存在或读取失败时，返回null。
	 */
	public static Object getPropertyOpt(Object bean, String name) {
		if (PropertyUtils.isReadable(bean, name)) {
			try {
				return PropertyUtils.getProperty(bean, name);
			} catch (Throwable t) {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 执行方法调用，支持调用bean存在基类而方法在基类中的情况
	 * @param bean
	 * @param methodName
	 * @param keys
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> invokeActionMethod(Object bean, String methodName, Object... keys) {
		Method method = null; //调用action的方法对象
		try {
			//确定参数个数
			Class<?> cls = bean.getClass();
			Class<?>[] paramClss = null;
			if (keys != null && keys.length > 0) {
				paramClss = new Class<?>[keys.length];
				for (int ii = 0; ii < keys.length; ii++) {
					paramClss[ii] = keys[ii].getClass();
				}
			}

			method = findFactMethod(cls, methodName, paramClss);
			if (method == null)
				throw new RuntimeException(cls.getSimpleName() + "找不到方法定义:" + methodName);

			//生成action方法的参数值
			//Object[] params = new Object[1];
			//params[0] = keys;

			return (List<Object>) method.invoke(bean, keys);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			Throwable te = e.getTargetException();
			if (te instanceof RuntimeException) {
				throw (RuntimeException) te;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * 递归找到真正的方法名
	 * @param cls
	 * @param paramCls
	 * @param methodName
	 * @return
	 */
	private static Method findFactMethod(Class<?> cls, String methodName, Class<?>... paramClss) {
		Method method = null;
		try {
			method = cls.getMethod(methodName, paramClss);//位于基类里，注意
			if (method != null)
				return method;
			else
				return null;
		} catch (NoSuchMethodException e) {
			if (paramClss != null && paramClss.length > 0) {
				Class<?> parentCls = paramClss[0].getSuperclass();
				if (parentCls != null) {
					Class<?>[] newParam = mergeParam(parentCls, paramClss);
					method = findFactMethod(cls, methodName, newParam);
					if (method != null)
						return method;
				}

				Class<?>[] inters = (paramClss[0]).getInterfaces();
				if (inters != null) {
					for (Class<?> inter : inters) {
						Class<?>[] newParam = mergeParam(inter, paramClss);
						method = findFactMethod(cls, methodName, newParam);
						if (method != null)
							return method;
					}
				}
			}
			return null;
		}
	}

	/**
	 * 用第一个参数替换后生成新的参数数组
	 * @param first
	 * @param paramClss
	 * @return
	 */
	private static Class<?>[] mergeParam(Class<?> first, Class<?>... paramClss) {
		int paramSize = paramClss.length;
		Class<?>[] newParam = new Class<?>[paramSize];
		newParam[0] = first;
		for (int ii = 1; ii < paramSize; ii++)
			newParam[ii] = paramClss[ii];
		return newParam;
	}

	//取对象差异属性的比较方式

	/** 普通方式：如果两个对象属性值不同，将第一个对象的属性值写到目标对象的相同属性上。 */
	public static final int BEAN_DIFF_METHOD_COMMON = 0;

	/** 增量方式：如果两个对象属性值不同，将第一个对象的属性值减去第二对象属性值的差值写到目标对象的增加"Var"后缀的属性上。
	 * 如bean1.num=10，bean2.num=8，则设置target.numVar=10-8=2 */
	public static final int BEAN_DIFF_METHOD_VAR = 1;

	/**  取差值方式的目标属性名后缀 */
	public static final String BEAN_DIFF_VAR_METHOD_SUBFIX = "Var";

	/**
	 * 比较两个对象，把有差异的属性值写到目标对象。
	 *  根据比较方式，确定写到目标对象的内容。
	 * @author lsh  
	 * @param bean1 待比较对象1
	 * @param bean2 待比较对象2
	 * @param target 目标对象
	 * @param compareMethodMap 比较方式。key=源表属性名，val=比较方式，见BEAN_DIFF_METHOD_*
	 * @param exclude 需要排除的属性。可为null。
	 */
	public static <T, T1> void beanDiffTo(T bean1, T bean2, T1 target, Map<String, Integer> compareMethodMap, Collection<String> exclude) {
		List<String> srcBeanPropNames; //源bean属性名
		if (bean1 != null) {
			srcBeanPropNames = getPropNames(bean1);
		} else if (bean2 != null) {
			srcBeanPropNames = getPropNames(bean2);
		} else { //同时为null时不作处理
			return;
		}

		Set<String> targetBeanPropNames = new HashSet<>(getPropNames(target)); //目标bean属性名

		try {
			for (String key : srcBeanPropNames) {
				if (exclude != null && exclude.contains(key)) {
					continue;
				}

				//取属性值
				Object val1 = null;
				Object val2 = null;
				if (bean1 != null) {
					val1 = PropertyUtils.getProperty(bean1, key);
				}
				if (bean2 != null) {
					val2 = PropertyUtils.getProperty(bean2, key);
				}

				//比较属性是否相等，不相等时，设置到目标
				if (!isEquals(val1, val2)) {
					Integer diffMethod = compareMethodMap.get(key);
					if (diffMethod == null) {
						diffMethod = BEAN_DIFF_METHOD_COMMON;
					}

					//根据不同的处理差异方式，生成目标字段名和值
					String targetField;
					Object val;
					switch (diffMethod) {
					case BEAN_DIFF_METHOD_VAR: //取差值方式
						targetField = key + BEAN_DIFF_VAR_METHOD_SUBFIX;
						val = NumberUtils.subtractNumricalValue((Number) val1, (Number) val2);
						break;
					default: //默认使用普通方式
						targetField = key;
						val = val1;
						break;
					}

					//设置值到目标字段
					if (targetBeanPropNames.contains(targetField)) {
						PropertyUtils.setProperty(target, key, val);
					}
				}
			}
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new RuntimeException(e);
		}

	}

}
