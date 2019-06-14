package com.hzecool.fdn.ip2region;

import java.io.Serializable;

/**
 * ip地址获取城市和信道
 * 
 * @author fankd created on 2017-2-15
 * @since
 */
public class RegionBean implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -9019498127417763914L;
	/** 城市id */
	private Integer cityId;

	/** 名称 */
	private String cityName;

	/**
	 * 省id
	 */
	private Integer provinceId;

	/**
	 * 省名称
	 */
	private String provinceName;

	/**
	 * 通信通道，移动、电信、网通
	 */
	private String isp;

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getIsp() {
		return isp;
	}

	public void setIsp(String isp) {
		this.isp = isp;
	}

	@Override
	public String toString() {
		return "RegionBean [cityId=" + cityId + ", cityName=" + cityName + ", provinceId=" + provinceId
				+ ", provinceName=" + provinceName + ", isp=" + isp + "]";
	}
	

}