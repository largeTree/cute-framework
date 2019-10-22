package com.qiuxs.cuteframework.view.pagemodel;

import java.util.List;

public class Td {

	/** 按钮类型的列 */
	public static final String TD_TYPE_BTN = "btn";
	/** 隐藏的列 */
	public static final String TD_TYPE_HIDE = "hidden";
	/** 文本 */
	public static final String TD_TYPE_TEXT = "text";
	/** 数字 */
	public static final String TD_TYPE_NUM = "num";
	/** 日期 */
	public static final String TD_TYPE_DATE = "date";
	/** 时间 */
	public static final String TD_TYPE_DATETIME = "datetime";
	/** 翻译类型 */
	public static final String TD_TYPE_CAPTION = "caption";

	/** 列名 */
	private String name;
	/** 列字段 */
	private String field;
	/** 列类型 */
	private String type;
	/** 是否支持排序 */
	private int order;
	/** 列宽 */
	private int length;
	/** 按钮列的按钮列表 */
	private List<TdBtn> btns;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getType() {
		// 默认返回文本类型
		if (type == null) {
			return TD_TYPE_TEXT;
		}
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public List<TdBtn> getBtns() {
		return btns;
	}

	public void setBtns(List<TdBtn> btns) {
		this.btns = btns;
	}

}
