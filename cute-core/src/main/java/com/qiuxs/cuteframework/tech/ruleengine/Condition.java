package com.qiuxs.cuteframework.tech.ruleengine;

/**
 * 条件
 * @author qiuxs
 * 
 * 创建时间 ： 2018年11月27日 下午9:27:53
 *
 */
public class Condition {

	/** 属性 */
	private String prop;
	/** 操作符，比较运算符 */
	private String op;
	/** 比较值 */
	private String val;

	public String getProp() {
		return prop;
	}

	public void setProp(String prop) {
		this.prop = prop;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

}
