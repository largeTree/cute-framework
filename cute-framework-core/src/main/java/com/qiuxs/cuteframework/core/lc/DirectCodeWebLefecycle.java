package com.qiuxs.cuteframework.core.lc;

import com.qiuxs.cuteframework.core.basic.code.utils.CodeUtils;
import com.qiuxs.cuteframework.core.listener.lc.IWebLifecycle;
import com.qiuxs.cuteframework.core.persistent.database.entity.IFlag;

/**
 * 初始化各个框架级别的直接编码集
 * @author qiuxs
 *
 */
public class DirectCodeWebLefecycle implements IWebLifecycle {

	@Override
	public int order() {
		return 10;
	}

	@Override
	public void lastInit() {
		CodeUtils.genDirectCode(IFlag.class);
	}

}
