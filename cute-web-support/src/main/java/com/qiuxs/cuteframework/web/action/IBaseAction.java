package com.qiuxs.cuteframework.web.action;

import com.qiuxs.cuteframework.web.bean.ActionResult;
import com.qiuxs.cuteframework.web.bean.ReqParam;

public interface IBaseAction extends IAction {
	/**
	 * 基础查询列表
	 * 
	 * 2019年3月22日 下午9:19:51
	 * @auther qiuxs
	 * @param params
	 * @param jsonData
	 * @return
	 */
	ActionResult list(ReqParam params, String jsonData);
	
	/**
	 * 根据ID获取单个bean
	 * 2019年3月22日 下午9:05:23
	 * qiuxs
	 */
	ActionResult get(ReqParam params);

	/**
	 * 保存bean
	 * 
	 * 2019年3月22日 下午9:10:39
	 * @auther qiuxs
	 * @param params
	 * @param jsonData
	 * @return
	 */
	ActionResult save(ReqParam params, String jsonData);

	/**
	 * 根据ID删除bean
	 * 
	 * 2019年3月22日 下午9:14:48
	 * @auther qiuxs
	 * @param params
	 * @return
	 */
	ActionResult delete(ReqParam params);

	/**
	 * 根据ID停用一行记录
	 * 
	 * 2019年3月22日 下午9:16:41
	 * @auther qiuxs
	 * @param params
	 * @return
	 */
	ActionResult disable(ReqParam params);

	/**
	 * 根据ID启用一行记录
	 * 
	 * 2019年3月22日 下午9:17:40
	 * @auther qiuxs
	 * @param params
	 * @return
	 */
	ActionResult enable(ReqParam params);
}
