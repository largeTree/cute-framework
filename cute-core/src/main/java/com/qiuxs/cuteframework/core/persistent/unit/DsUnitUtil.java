package com.qiuxs.cuteframework.core.persistent.unit;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qiuxs.cuteframework.core.basic.utils.MapUtils;
import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;
import com.qiuxs.cuteframework.core.persistent.database.lookup.DataSourceContext;
import com.qiuxs.cuteframework.core.persistent.unit.entity.DsUnit;
import com.qiuxs.cuteframework.core.persistent.unit.service.impl.DsUnitService;

/**
 * 
 * 功能描述: 单元和数据库初始化关系<br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年3月10日 上午9:48:58 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class DsUnitUtil {

	private static Logger log = LogManager.getLogger(DsUnitUtil.class);
	
	public static boolean hasDsUnit;

	private static DsUnitService dsUnitService;

	/**
	 * 初始化数据关系
	 *  
	 * @author qiuxs
	 */
	public static void initDsUnit() {
		DsUnitService dsUnitService = getDsUnitService();

		hasDsUnit = dsUnitService.hasDsUnit();

		if (!hasDsUnit) {
			return;
		}

		List<DsUnit> dsUnits = dsUnitService.getAll();
		DataSourceContext.refreshAllDsUnit(dsUnits);
	}

	/**
	 * 刷新数据源对应关系
	 *  
	 * @author qiuxs  
	 * @param unitId
	 */
	public static void refreshUnitDs(Long unitId) {
		DsUnitService dsUnitService = getDsUnitService();
		if (hasDsUnit) {
			DsUnit dsUnit = dsUnitService.findByMapSingle(MapUtils.genMap("unitId", unitId));
			if (dsUnit != null) {
				DataSourceContext.refreshDsUnit(dsUnit);
			} else {
				log.warn("RefreshUnitDs failed, unitId = " + unitId);
			}
		}
	}
	
	private static DsUnitService getDsUnitService() {
		if (dsUnitService == null) {
			dsUnitService = ApplicationContextHolder.getBean(DsUnitService.class);
		}
		return dsUnitService;
	}
}
