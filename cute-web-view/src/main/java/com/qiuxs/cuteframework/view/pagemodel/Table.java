package com.qiuxs.cuteframework.view.pagemodel;

import java.io.Serializable;
import java.util.List;

public class Table implements Serializable {
	
	private static final long serialVersionUID = -4377852980098027279L;
	
	/** 列表的列配置列表 */
	private List<Td> tds;
	/** 需要展开才能看到的列 */
	private List<Td> expands;

	public List<Td> getTds() {
		return tds;
	}

	public void setTds(List<Td> tds) {
		this.tds = tds;
	}

	public List<Td> getExpands() {
		return expands;
	}

	public void setExpands(List<Td> expands) {
		this.expands = expands;
	}

}
