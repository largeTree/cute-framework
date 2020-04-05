package com.qiuxs.cuteframework.core.persistent.unit.lc;

import com.qiuxs.cuteframework.core.listener.lc.IWebLifecycle;
import com.qiuxs.cuteframework.core.persistent.unit.DsUnitUtil;

public class DsUnitLifecycle implements IWebLifecycle {

	@Override
	public int order() {
		return 0;
	}

	@Override
	public void lastInit() {
		DsUnitUtil.initDsUnit();
	}
	
}
