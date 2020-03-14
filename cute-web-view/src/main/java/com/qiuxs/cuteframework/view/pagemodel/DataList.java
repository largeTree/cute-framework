package com.qiuxs.cuteframework.view.pagemodel;

import java.util.List;

/**
 * 查询列表配置.
 *
 * @author qiuxs
 */
public class DataList {

	/**  查询接口号. */
	private String apiKey;

	/**  列表上方按钮. */
	private List<ListButton> buttons;

	/**  搜索条件. */
	private Search search;

	/**  表格. */
	private Table table;

	/**
	 * Gets the 查询接口号.
	 *
	 * @return the 查询接口号
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * Sets the 查询接口号.
	 *
	 * @param apiKey the new 查询接口号
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * Gets the 列表上方按钮.
	 *
	 * @return the 列表上方按钮
	 */
	public List<ListButton> getButtons() {
		return buttons;
	}

	/**
	 * Sets the 列表上方按钮.
	 *
	 * @param buttons the new 列表上方按钮
	 */
	public void setButtons(List<ListButton> buttons) {
		this.buttons = buttons;
	}

	/**
	 * Gets the 搜索条件.
	 *
	 * @return the 搜索条件
	 */
	public Search getSearch() {
		return search;
	}

	/**
	 * Sets the 搜索条件.
	 *
	 * @param search the new 搜索条件
	 */
	public void setSearch(Search search) {
		this.search = search;
	}

	/**
	 * Gets the 表格.
	 *
	 * @return the 表格
	 */
	public Table getTable() {
		return table;
	}

	/**
	 * Sets the 表格.
	 *
	 * @param table the new 表格
	 */
	public void setTable(Table table) {
		this.table = table;
	}

}
