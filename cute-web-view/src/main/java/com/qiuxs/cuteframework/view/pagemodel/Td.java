package com.qiuxs.cuteframework.view.pagemodel;

import java.io.Serializable;
import java.util.List;

public class Td implements Serializable {

	private static final long serialVersionUID = 109238320294072107L;
	
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
	/** 图片地址 */
	public static final String TD_TYPE_IMAGE = "imageurl";
	/** 行内计算 */
	public static final String TD_TYPE_CALC = "calc";

	/** 列名 */
	private String name;
	/** 列字段 */
	private String field;
	/** 列类型 */
	private String type;
	/** 鼠标悬浮提示内容 */
	private String alt;
	/** 显示前缀 */
	private String prefix;
	/** 显示后缀 */
	private String suffix;
	/** 宽度 */
	private String width = "50";
	/** 高度 */
	private String height = "50";
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

	public String getAlt() {
		return alt;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
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
