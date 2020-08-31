package com.qiuxs.cuteframework.core.persistent.database.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.qiuxs.cuteframework.core.basic.code.provider.CodeOption;

/**
 * 查询定义
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年8月28日 下午1:56:47 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public interface ICodeOptionsDaoProvider<C> {
	
	public List<CodeOption<C>> searchOptions(@Param("searchToken") String searchToken);
	
}
