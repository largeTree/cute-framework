package com.qiuxs.cuteframework.web.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 列表结果
 * @author qiuxs
 * 
 * 创建时间 ： 2018年8月17日 下午8:53:31
 *
 */
public class ListResult implements Serializable {

	private static final long serialVersionUID = 9175172716235701850L;

	private List<?> list;
	private Map<String, ? extends Number> summary;
	private Integer total;
	
	public ListResult() {
	}

	public ListResult(List<?> list) {
		this(list, null);
	}

	public ListResult(List<?> list, Integer total) {
		this(list, total, null);
	}

	public ListResult(List<?> list, Integer total, Map<String, ? extends Number> summary) {
		if (list == null) {
			list = new ArrayList<>();
		}
		this.list = list;
		if (total == null) {
			this.total = list.size();
		} else {
			this.total = total;
		}
		if (summary == null) {
			summary = new HashMap<>();
		}
		this.summary = summary;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public Map<String, ? extends Number> getSummary() {
		return summary;
	}

	public void setSummary(Map<String, ? extends Number> summary) {
		this.summary = summary;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

}
