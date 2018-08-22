package com.qiuxs.cuteframework.core.basic.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 抽象主从表DTO
 * 
 * @author qiuxs
 * 
 * 创建时间 ： 2018年8月12日 下午8:00:37
 *
 * @param <主表泛型>
 * @param <从表泛型>
 */
public abstract class AbstractMainDetailsCombDTO<MT, DT> implements Serializable {

	private static final long serialVersionUID = -5091285387056225933L;

	private MT main;
	private List<DT> details;

	public MT getMain() {
		return main;
	}

	public void setMain(MT main) {
		this.main = main;
	}

	public List<DT> getDetails() {
		return details;
	}

	public void setDetails(List<DT> details) {
		this.details = details;
	}

}
