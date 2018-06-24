package com.qiuxs.cuteframework.core.persistent.database.dao.page;

import org.apache.ibatis.session.RowBounds;

public class PageInfo extends RowBounds {

	public PageInfo() {
	}

	public PageInfo(int offset, int limit) {
		super(offset, limit);
	}

}
