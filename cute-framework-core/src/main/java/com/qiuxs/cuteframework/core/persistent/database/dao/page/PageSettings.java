package com.qiuxs.cuteframework.core.persistent.database.dao.page;

import java.util.Map;

public class PageSettings {

	public static final String PAGE_NO = "pageNo";
	public static final String PAGE_SIZE = "pageSize";

	public static final String OFFSET = "offset";
	public static final String LIMIT = "limit";

	public static PageInfo preparePageInfo(Map<String, String> params) {
		PageInfo pageInfo = null;
		if (params != null) {
			String spageNo, spageSize, soffset, slimit;
			if ((spageNo = params.get(PAGE_NO)) != null) {
				// 按页码
				int pageNo = Integer.valueOf(spageNo);
				if (pageNo <= 0) {
					pageNo = 1;
				}
				int pageSize = Integer.valueOf((spageSize = params.get(PAGE_SIZE)) == null ? "0" : spageSize);
				pageInfo = new PageInfo();
				pageInfo.setPageNo(pageNo);
				pageInfo.setPageSize(pageSize);
				pageInfo.setOffset((pageNo - 1) * pageSize);
				pageInfo.setLimit(pageSize);
			} else if ((soffset = params.get(OFFSET)) != null) {
				// 滚动式
				int offset = Integer.valueOf(soffset);
				int limit = Integer.valueOf((slimit = params.get(LIMIT)) == null ? "0" : slimit);
				pageInfo = new PageInfo(offset, limit);
			}
		}
		// 默认使用不分页
		if (pageInfo == null) {
			pageInfo = PageInfo.NO_PAGE_INFO;
		}
		return pageInfo;
	}

}
