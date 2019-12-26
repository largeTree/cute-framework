package com.qiuxs.cuteframework.core.persistent.database.lookup;

import java.util.List;

import com.qiuxs.cuteframework.core.basic.utils.reflect.ReflectUtils;
import com.qiuxs.cuteframework.core.log.Console;
import com.qiuxs.cuteframework.core.persistent.database.dao.IBasicDao;

/**
 * 数据源注册中心
 * @author qiuxs
 *
 */
public class DsTypeRegister {

	/**
	 * 注册数据源类型
	 * @param dao
	 * @param dsId
	 * @param dsType
	 */
	public static void register(IBasicDao dao, Class<?> rootDaoClass, String dsId, String dsType, Class<?> pojoClass) {
		Class<? extends IBasicDao> daoClass = dao.getClass();
		if (daoClass.getName().contains("$")) {
			List<Class<?>> ifcs = ReflectUtils.getInterfaces(daoClass, rootDaoClass);
			for (Class<?> ifc : ifcs) {
				if (rootDaoClass.isAssignableFrom(ifc)) {
					String nameSpace = ifc.getCanonicalName();
					Console.log.debug("nameSpace = {}, dsType = {}, dsId = ", nameSpace, dsType, dsId);
					DataSourceContext.putDsType(nameSpace, dsType);
					if (dsId != null) {
						DataSourceContext.putDsId(nameSpace, dsId);
					}
					if (pojoClass != null) {
						DataSourceContext.putPojoClass(nameSpace, pojoClass);
					}
				}
			}
		}
	}

}
