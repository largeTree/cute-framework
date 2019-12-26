package com.qiuxs.cuteframework.core.basic.utils.ip2region;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;

import com.qiuxs.cuteframework.core.basic.bean.Pair;
import com.qiuxs.cuteframework.core.basic.utils.ClassPathResourceUtil;
import com.qiuxs.cuteframework.core.basic.utils.ip2region.constants.RegionConstants;

/**
 * 
 * 功能描述: Ip2Region外观类
 *  
 * @author qiuxs   
 * @version 1.0.0
 * @since 2017年2月21日 下午4:15:45
 */
public class Ip2RegionFacade {

	private static final Logger log = LogManager.getLogger(Ip2RegionFacade.class);

	private static DbSearcher searcher;
	/**[规范省名,规范省编码]*/
	private static Map<String, Integer> provinceNameMap = new HashMap<String, Integer>();

	static {
		//初始化DbSearcher
		try {
			Resource resource = ClassPathResourceUtil.getSingleResource("ip2region/ip2region.db");
			if (resource != null) {
				//new File(path)和resource.getFile()不能用于jar中文件，因而引入临时文件
				File tempFile = new File(FileUtils.getTempDirectoryPath() + "ip2region.db");
				tempFile.deleteOnExit();
				FileUtils.copyToFile(resource.getInputStream(), tempFile);
				searcher = new DbSearcher(new DbConfig(), tempFile);
			} else {
				throw new RuntimeException("No ip2region.db in classpath!");
			}
		} catch (IOException | DbMakerConfigException e) {
			throw new RuntimeException(e);
		}
		///填充[规范省名,规范省编码]
		provinceNameMap.put("北京市", 11);
		provinceNameMap.put("天津市", 12);
		provinceNameMap.put("河北省", 13);
		provinceNameMap.put("山西省", 14);
		provinceNameMap.put("内蒙古自治区", 15);

		provinceNameMap.put("辽宁省", 21);
		provinceNameMap.put("吉林省", 22);
		provinceNameMap.put("黑龙江省", 23);

		provinceNameMap.put("上海市", 31);
		provinceNameMap.put("江苏省", 32);
		provinceNameMap.put("浙江省", 33);
		provinceNameMap.put("安徽省", 34);
		provinceNameMap.put("福建省", 35);
		provinceNameMap.put("江西省", 36);
		provinceNameMap.put("山东省", 37);

		provinceNameMap.put("河南省", 41);
		provinceNameMap.put("湖北省", 42);
		provinceNameMap.put("湖南省", 43);
		provinceNameMap.put("广东省", 44);
		provinceNameMap.put("广西壮族自治区", 45);
		provinceNameMap.put("海南省", 46);

		provinceNameMap.put("重庆市", 50);
		provinceNameMap.put("四川省", 51);
		provinceNameMap.put("贵州省", 52);
		provinceNameMap.put("云南省", 53);
		provinceNameMap.put("西藏自治区", 54);

		provinceNameMap.put("陕西省", 61);
		provinceNameMap.put("甘肃省", 62);
		provinceNameMap.put("青海省", 63);
		provinceNameMap.put("宁夏回族自治区", 64);
		provinceNameMap.put("新疆维吾尔自治区", 65);

		provinceNameMap.put("台湾省", 71);
		provinceNameMap.put("香港特别行政区", 81);
		provinceNameMap.put("澳门特别行政区", 82);
	}

	/**
	 * 通过ip获取区域
	 *  
	 * @author fengdg  
	 * @param ip
	 * @return
	 */
	public static RegionBean getRegion(String ip) {
		try {
			DataBlock db = searcher.memorySearch(ip);
			RegionBean regionBean = new RegionBean();
			regionBean.setCityId(db.getCityId());
			regionBean.setCityName(db.getCity());
			regionBean.setProvinceName(db.getProvinceOrCountry());
			regionBean.setProvinceId(provinceNameMap.get(db.getProvinceOrCountry()));
			regionBean.setIsp(db.getIsp());
			return regionBean;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 根据IP获取省市
	 * 
	 * 2019年6月15日 下午9:43:06
	 * @auther qiuxs
	 * @param ip
	 * @return
	 */
	public static Pair<Integer, Integer> getProvCodeCityCode(String ip) {
		try {
			DataBlock db = searcher.memorySearch(ip);
			String province = db.getProvinceOrCountry();
			Integer provinceCode = RegionConstants.getProvCode(province);
			if (provinceCode == null) {//省不存在
				return null;
			}
			String city = db.getCity();
			Integer cityCode = RegionConstants.getCityCode(city);
			return new Pair<Integer, Integer>(provinceCode, cityCode);
		} catch (IOException e) {
			log.error("ex: " + e.getMessage());
		}
		return null;
	}

	public static void main(String[] args) {
		String ip = "36.24.24.150";
		RegionBean rb = getRegion(ip);
		System.out.println(rb);
	}
}
