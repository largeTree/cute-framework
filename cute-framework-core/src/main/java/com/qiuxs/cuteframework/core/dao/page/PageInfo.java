package com.qiuxs.cuteframework.core.dao.page;

import org.apache.ibatis.session.RowBounds;

public class PageInfo extends RowBounds {

	public PageInfo(int offset, int limit) {
		super(offset, limit);
	}

}
