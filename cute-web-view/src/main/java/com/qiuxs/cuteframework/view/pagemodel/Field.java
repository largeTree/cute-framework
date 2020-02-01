package com.qiuxs.cuteframework.view.pagemodel;

/**
 * 表单字段.
 *
 * @author qiuxs
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

	/**  标签. */
	private String label;
	
	/**  字段类型. */
	private String type = "text";
	
	/**  字段名. */
	private String name;
	
	/**  字段为选择型字段时的编码翻译器名称. */
	private String code;
	
	/**  默认值. */
	private String defval;
	
	/**  列数. */
	private int cols = 15;
	
	/**  行数. */
	private int rows = 3;
	
	/**  是否独占一行. */
	private int singleRow;
	
	/**  是否必填. */
	private int required;
	
	/** 提示语 */
	private String placeholder = "";

	/**
	 * Gets the 标签.
	 *
	 * @return the 标签
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the 标签.
	 *
	 * @param label the new 标签
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Gets the 字段类型.
	 *
	 * @return the 字段类型
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the 字段类型.
	 *
	 * @param type the new 字段类型
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the 字段名.
	 *
	 * @return the 字段名
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the 字段名.
	 *
	 * @param name the new 字段名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the 字段为选择型字段时的编码翻译器名称.
	 *
	 * @return the 字段为选择型字段时的编码翻译器名称
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets the 字段为选择型字段时的编码翻译器名称.
	 *
	 * @param code the new 字段为选择型字段时的编码翻译器名称
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Gets the 默认值.
	 *
	 * @return the 默认值
	 */
	public String getDefval() {
		return defval;
	}

	/**
	 * Sets the 默认值.
	 *
	 * @param defval the new 默认值
	 */
	public void setDefval(String defval) {
		this.defval = defval;
	}

	/**
	 * Gets the 列数.
	 *
	 * @return the 列数
	 */
	public int getCols() {
		return cols;
	}

	/**
	 * Sets the 列数.
	 *
	 * @param cols the new 列数
	 */
	public void setCols(int cols) {
		this.cols = cols;
	}

	/**
	 * Gets the 行数.
	 *
	 * @return the 行数
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * Sets the 行数.
	 *
	 * @param rows the new 行数
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * Gets the 是否独占一行.
	 *
	 * @return the 是否独占一行
	 */
	public int getSingleRow() {
		return singleRow;
	}

	/**
	 * Sets the 是否独占一行.
	 *
	 * @param singleRow the new 是否独占一行
	 */
	public void setSingleRow(int singleRow) {
		this.singleRow = singleRow;
	}

	/**
	 * Gets the 是否必填.
	 *
	 * @return the 是否必填
	 */
	public int getRequired() {
		return required;
	}

	/**
	 * Sets the 是否必填.
	 *
	 * @param required the new 是否必填
	 */
	public void setRequired(int required) {
		this.required = required;
	}

	/**
	 * Gets the 提示语.
	 *
	 * @return the 提示语
	 */
	public String getPlaceholder() {
		return placeholder;
	}

	/**
	 * Sets the 提示语.
	 *
	 * @param placeholder the new 提示语
	 */
	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}
	

}
