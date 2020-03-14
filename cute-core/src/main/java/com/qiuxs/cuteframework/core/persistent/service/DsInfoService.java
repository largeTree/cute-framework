package com.qiuxs.cuteframework.core.persistent.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qiuxs.cuteframework.core.persistent.dao.DsInfoDao;

/**
 * 
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年3月10日 上午11:07:10 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
@Service
public class DsInfoService {
	
	@Resource
	private DsInfoDao dsInfoDao;
	
	/**
	 * 获取一个使用数最少的业务库
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public String switchBizDsId() {
		return this.dsInfoDao.switchBizDsId();
	}
	
	/**
	 * 增加使用数
	 *  
	 * @author qiuxs  
	 * @param dsId
	 * @param addNum
	 */
	@Transactional
	public void addUsedNum(String dsId, Integer addNum) {
		this.dsInfoDao.addUsedNumById(dsId, addNum);
	}
	
}
