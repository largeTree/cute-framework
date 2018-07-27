package com.qiuxs.cuteframework.core.persistent.database.lookup;

import com.qiuxs.cuteframework.core.context.TLVariableHolder;

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
	
	public static String getDsId() {
		String currentDsId = TLVariableHolder.getVariable(TL_DS_ID);
		return currentDsId;
	}
	
	public static String setDsId(String dsId) {
		String oldDsId = TLVariableHolder.getVariable(TL_DS_ID);
		TLVariableHolder.setVariable(TL_DS_ID, dsId);
		return oldDsId;
	}

}
