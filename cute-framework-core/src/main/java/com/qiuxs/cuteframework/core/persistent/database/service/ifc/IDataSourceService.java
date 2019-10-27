package com.qiuxs.cuteframework.core.persistent.database.service.ifc;

import com.qiuxs.cuteframework.core.basic.Constants.DsType;

/**
 * 支持自动切换数据源的服务类
 * @author qiuxs
 *
 */
public interface IDataSourceService {

	public default String getDsType() {
		return DsType.BIZ.value();
	}

	public default String getDsId() {
		return null;
	}

}
