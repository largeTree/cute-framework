package com.qiuxs.cuteframework.core.persistent.database.dao.page;

import java.util.Map;

import org.apache.ibatis.session.RowBounds;

/**
 * 分页对象
 * 
 * @author qiuxs
 * 
 * 创建时间 ： 2018年8月16日 下午10:08:25
 *
 */
public class PageInfo extends RowBounds {

	private static final int DEFAULT_PAGESIZE = 20;

	/** 当前页码 */
	private int pageNo;
	/** 每页行数 */
	private int pageSize;

	/** 偏移量 */
	private int offset;
	/** 每页行数 */
	private int limit;

	/** 总行数 */
	private Integer total;
	/** 总页数 */
	private Integer totalPage;

	/** 默认分页 */
	public static final PageInfo DEFAULT_PAGE_INFO = new PageInfo(1, DEFAULT_PAGESIZE);

	/** 适用于获取一行数据的分页对象 */
	public static final PageInfo SINGLE_PAGE_INFO = new PageInfo(1, 1);

	public PageInfo() {
		this.pageNo = 1;
		this.pageSize = RowBounds.NO_ROW_LIMIT;
	}

	public PageInfo(int pageNo, int pageSize) {
		if (pageNo <= 0) {
			pageNo = 1;
		}
		this.offset = (pageNo - 1) * pageSize;
		this.setLimit(pageSize);
		this.setOffset(this.offset);
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	/**
	 * 不分页的分页对象
	 * @return
	 */
	public static PageInfo makeNoPageInfo() {
		return new PageInfo(1, RowBounds.NO_ROW_LIMIT);
	}

	/**
	 * 默认分页对象
	 * @return
	 */
	public static PageInfo makeDefaultPageInfo() {
		return new PageInfo(1, DEFAULT_PAGESIZE);
	}

	/**
	 * 构造分页对象
	 * @author qiuxs
	 *
	 * @param pageNo
	 * @param pageSize
	 * @return
	 *
	 * 创建时间：2018年8月16日 下午10:10:46
	 */
	public PageInfo makePageInfo(int pageNo, int pageSize) {
		if (pageNo <= 0) {
			pageNo = 1;
		}
		if (pageSize <= 0) {
			pageSize = DEFAULT_PAGESIZE;
		}
		int offset = (pageNo - 1) * pageSize;
		PageInfo pageInfo = new PageInfo(offset, pageSize);
		pageInfo.setPageNo(pageNo);
		pageInfo.setPageSize(pageSize);
		return pageInfo;
	}

	public static PageInfo makeSinglePageInfo() {
		return new PageInfo(1, 1);
	}

	public int getPageNo() {
		return this.pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		if (pageSize == 0) {
			pageSize = RowBounds.NO_ROW_LIMIT;
		}
		this.pageSize = pageSize;
	}

	public Integer getTotal() {
		return this.total;
	}

	public void setTotal(Integer total) {
		this.total = total;
		this.totalPage = (int) Math.ceil((total / pageSize));
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public int getOffset() {
		return this.offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return this.limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public Map<String, ? extends Number> getSumrow() {
		return null;
	}

	public int moveToNextPage() {
		if (pageNo < totalPage) {
			pageNo++;
		}
		return pageNo;
	}

	public int moveToPrePage() {
		if (pageNo > 1) {
			pageNo--;
		}
		return pageNo;
	}

}
