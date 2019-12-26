package com.qiuxs.cuteframework.view.pagemodel;

/**
 * 表单字段
 * @author qiuxs
 *
 */
public class Field {

	public static final String TYPE_HIDDEN = "hidden";
	public static final String TYPE_SEARCH_BTN = "searchBtn";
	public static final String TYPE_TEXT = "text";
	public static final String TYPE_TEXTAREA = "textarea";
	public static final String TYPE_DATE = "date";
	public static final String TYPE_DATETIME = "datetime";
	public static final String TYPE_LIST = "list";
	public static final String TYPE_ACLIST = "aclist";

	public static final Field SEARCH_BTN = new Field() {
		@Override
		public String getType() {
			return TYPE_SEARCH_BTN;
		}
	};

	/** 标签 */
	private String label;
	/** 字段类型 */
	private String type = "text";
	/** 字段名 */
	private String name;
	/** 字段为选择型字段时的编码翻译器名称 */
	private String code;
	/** 默认值 */
	private String defval;
	/** 列数 */
	private int cols = 15;
	/** 行数 */
	private int rows = 3;
	/** 是否独占一行 */
	private int singleRow;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDefval() {
		return defval;
	}

	public void setDefval(String defval) {
		this.defval = defval;
	}

	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getSingleRow() {
		return singleRow;
	}

	public void setSingleRow(int singleRow) {
		this.singleRow = singleRow;
	}

}
