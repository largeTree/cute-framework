package com.hzecool.fdn.ip2region;


/**
 * data block class
 * 
 * @author chenxin<chenxin619315@gmail.com>
 */
public class DataBlock
{
	/**
	 * city id
	 */
	private int city_id;

	/**
	 * region address
	 */
	private String region;
	// 中国|华东|浙江省|杭州市|电信
	private String[] arrreg;

	/**
	 * region ptr in the db file
	 */
	private int dataPtr;

	/**
	 * construct method
	 * 
	 * @param city_id
	 * @param region
	 *            region string
	 * @param ptr
	 *            data ptr
	 */
	public DataBlock(int city_id, String region, int dataPtr)
	{
		this.city_id = city_id;
		this.region = region;
		this.dataPtr = dataPtr;
		this.arrreg = this.region.split("\\|");
	}

	public DataBlock(int city_id, String region)
	{
		this(city_id, region, 0);
	}

	public int getCityId()
	{
		return city_id;
	}

	public DataBlock setCityId(int city_id)
	{
		this.city_id = city_id;
		return this;
	}

	public String getRegion()
	{
		return region;
	}

	public DataBlock setRegion(String region)
	{
		this.region = region;
		this.arrreg = this.region.split("\\|");
		return this;
	}

	public int getDataPtr()
	{
		return dataPtr;
	}

	public DataBlock setDataPtr(int dataPtr)
	{
		this.dataPtr = dataPtr;
		return this;
	}

	public String getCountry() {
		return "0".equals(this.arrreg[0]) ? "" : this.arrreg[0];
	}

	/**
	 * 省份为空，返回国家
	 * @return
	 */
	public String getProvinceOrCountry() {
		return "0".equals(this.arrreg[2]) ? getCountry() : this.arrreg[2];
	}

	public String getCity() {
		return "0".equals(this.arrreg[3]) ? "" : this.arrreg[3];
	}

	public String getIsp() {
		return "0".equals(this.arrreg[4]) ? "" : this.arrreg[4];
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append(city_id).append('|').append(region).append('|').append(dataPtr);
		return sb.toString();
	}

}
