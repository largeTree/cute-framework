package com.qiuxs.cuteframework.web.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.qiuxs.cuteframework.core.basic.ex.ErrorCodeConstants;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.JsonUtils;
import com.qiuxs.cuteframework.core.persistent.dao.IBaseDao;
import com.qiuxs.cuteframework.core.persistent.dao.page.PageInfo;
import com.qiuxs.cuteframework.core.persistent.entity.IEntity;
import com.qiuxs.cuteframework.core.persistent.service.AbstractDataService;

/**
 * 
 * 功能描述: 抽象数据控制器，提供基础的增删改查方法<br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2018年4月20日 下午10:31:55 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public abstract class AbstractDataController<PK extends Serializable, T extends IEntity<PK>, D extends IBaseDao<PK, T>, S extends AbstractDataService<PK, T, D>> extends AbstractPropertyController<PK, T, S> {

	@PostMapping("/create")
	public String create(@RequestParam(name = "jsonParam") String jsonParam) {
		T bean = JsonUtils.parseObject(jsonParam, this.getService().getPojoClass());
		this.getService().save(bean);
		return super.responseVal(bean.getId());
	}

	@DeleteMapping("/delete")
	public String delete(@RequestParam("id") PK id) {
		this.getService().deleteById(id);
		return super.responseSuccess();
	}

	@PostMapping("/update")
	public String update(@RequestParam(name = "jsonParam") String jsonParam) {
		T newBean = JsonUtils.parseObject(jsonParam, this.getService().getPojoClass());
		if (newBean.getId() == null) {
			ExceptionUtils.throwLogicalException(ErrorCodeConstants.UPDATE_NO_ID, "id is required");
		}
		this.getService().update(newBean);
		return super.responseVal(newBean.getId());
	}

	@GetMapping("/list")
	public String list(@RequestParam Map<String, String> params) {
		PageInfo pageInfo = super.preparePageInfo(params);
		List<T> list = this.getService().findByMap(new HashMap<>(params), pageInfo);
		return super.responseRes(list);
	}
}
