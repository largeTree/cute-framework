package com.qiuxs.cuteframework.common.biz.func.service;

import java.util.List;

import com.qiuxs.cuteframework.common.biz.func.dao.FuncDao;
import com.qiuxs.cuteframework.common.biz.func.entity.Func;
import com.qiuxs.cuteframework.core.basic.code.tree.TreeItem;
import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDataPropertyService;


/**
 * 功能菜单表服务接口
 * 
 * 创建时间 ：2019-07-11 22:36:23
 * @author qiuxs
 */
public interface IFuncService extends IDataPropertyService<String, Func, FuncDao> {

	/**
	 * 获取属性菜单结构
	 * @param parentId
	 * @param includeSub
	 * @return
	 */
	public List<TreeItem> funcTree(String parentId, boolean includeSub);

}
