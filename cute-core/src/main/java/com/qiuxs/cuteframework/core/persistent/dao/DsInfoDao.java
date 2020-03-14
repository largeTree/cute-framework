package com.qiuxs.cuteframework.core.persistent.dao;

import org.apache.ibatis.annotations.Param;

import com.qiuxs.cuteframework.tech.mybatis.dao.MyBatisRepository;

/**
 * dsInfo
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年3月10日 上午11:19:34 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
@MyBatisRepository
public interface DsInfoDao {

	/***
	 * 选择一个使用数最少的业务库
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public String switchBizDsId();
	
	/**
	 * 增加使用数
	 *  
	 * @author qiuxs  
	 * @param id
	 * @param addNum
	 * @return
	 */
	public int addUsedNumById(@Param("id") String id, @Param("addNum") Integer addNum);

}
