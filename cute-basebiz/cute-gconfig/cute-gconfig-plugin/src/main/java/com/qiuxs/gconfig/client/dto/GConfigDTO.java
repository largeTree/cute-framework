package com.qiuxs.gconfig.client.dto;

import java.io.Serializable;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class GConfigDTO.
 */
public class GConfigDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2434766040480392073L;

	/**  参数代码. */
	private String code;

	/**  配置域. */
	private String domian;

	/**  参数值. */
	private String val;
	
	/**  输入类型. */
	private int inputType;

	/**  参数可选值. */
	private List<GConfigOptions> opts;

	/**
	 * Gets the 参数代码.
	 *
	 * @return the 参数代码
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets the 参数代码.
	 *
	 * @param code the new 参数代码
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Gets the 配置域.
	 *
	 * @return the 配置域
	 */
	public String getDomian() {
		return domian;
	}

	/**
	 * Sets the 配置域.
	 *
	 * @param domian the new 配置域
	 */
	public void setDomian(String domian) {
		this.domian = domian;
	}

	/**
	 * Gets the 参数值.
	 *
	 * @return the 参数值
	 */
	public String getVal() {
		return val;
	}

	/**
	 * Sets the 参数值.
	 *
	 * @param val the new 参数值
	 */
	public void setVal(String val) {
		this.val = val;
	}

	/**
	 * Gets the 输入类型.
	 *
	 * @return the 输入类型
	 */
	public int getInputType() {
		return inputType;
	}

	/**
	 * Sets the 输入类型.
	 *
	 * @param inputType the new 输入类型
	 */
	public void setInputType(int inputType) {
		this.inputType = inputType;
	}

	/**
	 * Gets the 参数可选值.
	 *
	 * @return the 参数可选值
	 */
	public List<GConfigOptions> getOpts() {
		return opts;
	}

	/**
	 * Sets the 参数可选值.
	 *
	 * @param opts the new 参数可选值
	 */
	public void setOpts(List<GConfigOptions> opts) {
		this.opts = opts;
	}

}
