package com.qiuxs.cuteframework.view.pagemodel;

/**
 * 查询列表配置
 * @author qiuxs
 *
 */
public class DataList {
	private String apiKey;
	private Search search;
	private Table table;

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public Search getSearch() {
		return search;
	}

	public void setSearch(Search search) {
		this.search = search;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

}
