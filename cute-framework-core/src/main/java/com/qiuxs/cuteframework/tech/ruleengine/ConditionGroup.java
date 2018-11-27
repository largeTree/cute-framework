package com.qiuxs.cuteframework.tech.ruleengine;

import java.util.ArrayList;
import java.util.List;

public class ConditionGroup {

	/** 前一组条件连接符号  && 或者 || */
	private String linkOp;
	/** 条件连接符号，&& 或者 || */
	private String condOp;
	/** 条件集 */
	private List<Condition> conds = new ArrayList<>();

	public String getLinkOp() {
		return linkOp;
	}

	public void setLinkOp(String linkOp) {
		this.linkOp = linkOp;
	}

	public String getCondOp() {
		return condOp;
	}

	public void setCondOp(String condOp) {
		this.condOp = condOp;
	}

	public List<Condition> getConds() {
		return conds;
	}

	public void setConds(List<Condition> conds) {
		this.conds = conds;
	}

}
