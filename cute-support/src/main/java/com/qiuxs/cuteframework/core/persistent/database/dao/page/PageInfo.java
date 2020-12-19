package com.qiuxs.cuteframework.core.persistent.database.dao.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.qiuxs.cuteframework.core.basic.bean.Pair;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;

/**
 * 分页对象.
 *
 * @author qiuxs
 * 
 * 创建时间 ： 2018年8月16日 下午10:08:25
 */
public class PageInfo extends RowBounds {

	/** The Constant DEFAULT_PAGESIZE. */
	private static final int DEFAULT_PAGESIZE = 20;

	/**  当前页码. */
	private int pageNo;
	
	/**  每页行数. */
	private int pageSize;

	/**  偏移量. */
	private int offset;
	
	/**  每页行数. */
	private int limit;

	/**  总行数. */
	private Integer total;
	
	/**  总页数. */
	private Integer totalPage;
	
	/**  自动统计. */
	private boolean autoStatis = true;
	
	/**  需要合计的列和字段列表. */
	private List<Pair<String, String>> sumColumnFields = new ArrayList<>();

	/**  默认分页. */
	public static final PageInfo DEFAULT_PAGE_INFO = new PageInfo(1, DEFAULT_PAGESIZE);

	/**  适用于获取一行数据的分页对象. */
	public static final PageInfo SINGLE_PAGE_INFO = new PageInfo(1, 1);

	/**
	 * Instantiates a new page info.
	 */
	public PageInfo() {
		this.pageNo = 1;
		this.pageSize = RowBounds.NO_ROW_LIMIT;
	}

	/**
	 * Instantiates a new page info.
	 *
	 * @param pageNo the page no
	 * @param pageSize the page size
	 */
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
	 * 不分页的分页对象.
	 *
	 * @return the page info
	 */
	public static PageInfo makeNoPageInfo() {
		return new PageInfo(1, RowBounds.NO_ROW_LIMIT);
	}

	/**
	 * 默认分页对象.
	 *
	 * @return the page info
	 */
	public static PageInfo makeDefaultPageInfo() {
		return new PageInfo(1, DEFAULT_PAGESIZE);
	}

	/**
	 * 构造分页对象.
	 *
	 * @author qiuxs
	 * @param pageNo the page no
	 * @param pageSize the page size
	 * @return 创建时间：2018年8月16日 下午10:10:46
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

	/**
	 * Make single page info.
	 *
	 * @return the page info
	 */
	public static PageInfo makeSinglePageInfo() {
		return new PageInfo(1, 1);
	}

	/**
	 * Gets the 当前页码.
	 *
	 * @return the 当前页码
	 */
	public int getPageNo() {
		return this.pageNo;
	}

	/**
	 * Sets the 当前页码.
	 *
	 * @param pageNo the new 当前页码
	 */
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	/**
	 * Gets the 每页行数.
	 *
	 * @return the 每页行数
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * Sets the 每页行数.
	 *
	 * @param pageSize the new 每页行数
	 */
	public void setPageSize(int pageSize) {
		if (pageSize == 0) {
			pageSize = RowBounds.NO_ROW_LIMIT;
		}
		this.pageSize = pageSize;
	}

	/**
	 * Gets the 总行数.
	 *
	 * @return the 总行数
	 */
	public Integer getTotal() {
		return this.total;
	}

	/**
	 * Sets the 总行数.
	 *
	 * @param total the new 总行数
	 */
	public void setTotal(Integer total) {
		this.total = total;
		this.totalPage = (int) Math.ceil((total / pageSize));
	}

	/**
	 * Gets the 总页数.
	 *
	 * @return the 总页数
	 */
	public Integer getTotalPage() {
		return totalPage;
	}

	/**    
	 * @see org.apache.ibatis.session.RowBounds#getOffset()  
	 */
	public int getOffset() {
		return this.offset;
	}

	/**
	 * Sets the 偏移量.
	 *
	 * @param offset the new 偏移量
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**    
	 * @see org.apache.ibatis.session.RowBounds#getLimit()  
	 */
	public int getLimit() {
		return this.limit;
	}

	/**
	 * Sets the 每页行数.
	 *
	 * @param limit the new 每页行数
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * Gets the sumrow.
	 *
	 * @return the sumrow
	 */
	public Map<String, ? extends Number> getSumrow() {
		return null;
	}

	/**
	 * Move to next page.
	 *
	 * @return the int
	 */
	public int moveToNextPage() {
		if (pageNo < totalPage) {
			pageNo++;
		}
		return pageNo;
	}

	/**
	 * Move to pre page.
	 *
	 * @return the int
	 */
	public int moveToPrePage() {
		if (pageNo > 1) {
			pageNo--;
		}
		return pageNo;
	}

	/**
	 * Checks if is 自动统计.
	 *
	 * @return the 自动统计
	 */
	public boolean isAutoStatis() {
		return autoStatis;
	}

	/**
	 * Sets the 自动统计.
	 *
	 * @param autoStatis the new 自动统计
	 */
	public void setAutoStatis(boolean autoStatis) {
		this.autoStatis = autoStatis;
	}
	
	/**
	 * 添加合计列
	 *
	 * @param column the column
	 * @param fieldName the field name
	 * @return the page info
	 */
	public PageInfo addSumColumn(String column, String fieldName) {
		this.addSumColumn(new Pair<String, String>(column, fieldName));
		return this;
	}

	/**
	 * 添加一个合计列
	 *  
	 * @author qiuxs  
	 * @param item
	 * @return 
	 */
	public PageInfo addSumColumn(Pair<String, String> item) {
		if (StringUtils.isBlank(item.getV1())) {
			throw new IllegalArgumentException("需要合计的列名不能为空");
		}
		if (StringUtils.isBlank(item.getV2())) {
			throw new IllegalArgumentException("合计列所对应的字段名不能为空");
		}
		this.sumColumnFields.add(item);
		return this;
	}
	
	/**
	 * Gets the 需要合计的列和字段列表.
	 *
	 * @return the 需要合计的列和字段列表
	 */
	public List<Pair<String, String>> getSumColumnFields() {
		return this.sumColumnFields;
	}

}
