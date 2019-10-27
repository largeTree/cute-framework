package com.qiuxs.cuteframework.core.persistent.database.lookup;

import java.util.HashMap;
import java.util.Map;

import com.qiuxs.cuteframework.core.basic.Constants.DsType;
import com.qiuxs.cuteframework.core.basic.bean.UserLite;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.context.TLVariableHolder;
import com.qiuxs.cuteframework.core.context.UserContext;

/**
 * 数据源上下文
 * 
 * @author qiuxs
 * 
 * 创建时间 ： 2018年7月27日 下午11:17:24
 *
 */
public class DataSourceContext {

	private static final String TL_DS_ID = "_tl_current_ds_id";

	/**数据源切换的开关：用于手动切换*/
	private static final String TLK_DS_SWITCH = "tlDsSwitch";

	/**<namespace, dsType>*/
	private static Map<String, String> dsTypeMap = new HashMap<String, String>();
	/**<namespace, dsId>*/
	private static Map<String, String> dsIdMap = new HashMap<String, String>();
	/**dao对应的pojo（可以不是entity，而是dto）<namespace, pojo>*/
	private static Map<String, Class<?>> pojoClassMap = new HashMap<>();
	/** 单元ID和业务库的对应关系 */
	private static Map<Long, String> unitDsMap = new HashMap<>();

	public static String getDsId() {
		String currentDsId = TLVariableHolder.getVariable(TL_DS_ID);
		return currentDsId;
	}

	public static String setUpDs(String dsId) {
		String oldDsId = TLVariableHolder.getVariable(TL_DS_ID);
		TLVariableHolder.setVariable(TL_DS_ID, dsId);
		return oldDsId;
	}

	public static String putDsType(String nameSpace, String dsType) {
		if (StringUtils.isNotBlank(nameSpace)) {
			return dsTypeMap.put(nameSpace, dsType);
		}
		return null;
	}

	public static String putDsId(String nameSpace, String dsId) {
		if (StringUtils.isNotBlank(nameSpace)) {
			return dsIdMap.put(nameSpace, dsId);
		}
		return null;
	}

	public static Class<?> putPojoClass(String nameSpace, Class<?> pojoClass) {
		if (StringUtils.isNotBlank(nameSpace)) {
			return pojoClassMap.put(nameSpace, pojoClass);
		}
		return null;
	}

	public static String getDsType(String nameSpace) {
		return dsTypeMap.get(nameSpace);
	}

	/**
	 * 设置数据源切换为手动，默认是自动
	 *  
	 * @author qiuxs  
	 * @param onOff
	 * 线程变量统一清理
	 */
	public static void setDsSwitchManual() {
		TLVariableHolder.setVariable(TLK_DS_SWITCH, false);
	}

	/**
	 * 设置数据源切换为自动
	 *  
	 * @author qiuxs  
	 * @param onOff
	 * 线程变量统一清理
	 */
	public static void setDsSwitchAuto() {
		TLVariableHolder.setVariable(TLK_DS_SWITCH, true);
	}

	/**
	 * 是否自动切换数据源
	 * @author qiuxs
	 * @return
	 * 		true：数据源自动切换
	 * 		false：手动切换数据源
	 */
	public static boolean isDsSwitchAuto() {
		Boolean switchFlag = (Boolean) TLVariableHolder.getVariable(TLK_DS_SWITCH);
		return switchFlag == null ? true : switchFlag.booleanValue();
	}

	/**
	 * 获取数据源ID
	 * @param dsType
	 * @return
	 */
	public static String getDsIdByDsType(String dsType) {
		if (dsType == null || DsType.UNKNOWN.value().equals(dsType)) {
			return null;
		} else if (DsType.BIZ.value().equals(dsType)) {
			return getBizDsId();
		} else {
			return DynamicDataSource.getDynamicDataSource().getNonBizDsId(dsType);
		}
	}

	/**
	 * 获取业务库id
	 * @return
	 */
	private static String getBizDsId() {
		UserLite userLite = UserContext.getUserLiteOpt();
		if (userLite != null) {
			Long unitId = userLite.getUnitId();
			if (unitId != null) {
				String dsId = unitDsMap.get(unitId);
				if (dsId != null) {
					return dsId;
				}
			}
		}
		return DynamicDataSource.getDynamicDataSource().getEntryDb();
	}

	/**
	 * 通过dao层的nameSpace获取数据源
	 * @param nameSpace
	 * @return
	 */
	public static String getDsIdByNameSpace(String nameSpace) {
		String dsType = dsTypeMap.get(nameSpace);
		if (DsType.DIRECT.value().equals(dsType)) {
			return dsIdMap.get(nameSpace);
		} else {
			return getDsIdByDsType(dsType);
		}
	}

}
