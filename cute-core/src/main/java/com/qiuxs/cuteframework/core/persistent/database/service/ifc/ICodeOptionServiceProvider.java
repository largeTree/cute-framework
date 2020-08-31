package com.qiuxs.cuteframework.core.persistent.database.service.ifc;

import java.util.List;

import com.qiuxs.cuteframework.core.basic.code.provider.CodeOption;
import com.qiuxs.cuteframework.core.basic.code.provider.ICodeTranslatable;

/**
 * 服务编码接口
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年8月28日 下午1:53:31 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public interface ICodeOptionServiceProvider<C> extends ICodeTranslatable<C>{

	/**
	 * 根据searchToken查询编码集
	 *  
	 * @author qiuxs  
	 * @param searchToken
	 * @return
	 */
	public List<CodeOption<C>> searchOptions(String searchToken);
	
}
