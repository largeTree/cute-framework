package com.qiuxs.cuteframework.web.log.service.impl;

import java.util.List;
import javax.annotation.Resource;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.core.persistent.database.modal.PropertyWrapper;
import com.qiuxs.cuteframework.core.persistent.database.modal.BaseField;
import com.qiuxs.cuteframework.core.persistent.database.service.AbstractDataPropertyService;
import com.qiuxs.cuteframework.core.persistent.database.service.filter.IServiceFilter;
import com.qiuxs.cuteframework.core.persistent.database.service.filter.impl.IdGenerateFilter;
import com.qiuxs.cuteframework.web.log.dao.ApiRequestLogDao;
import com.qiuxs.cuteframework.web.log.entity.ApiRequestLog;
import com.qiuxs.cuteframework.web.log.service.IApiRequestLogService;
/**
 * 请求日志记录服务类
 *
 * @author qiuxs
 *
 */
@Service
public class ApiRequestLogService extends AbstractDataPropertyService<Long, ApiRequestLog, ApiRequestLogDao> implements IApiRequestLogService {

	private static final String TABLE_NAME = "api_request_log";

	public ApiRequestLogService() {
		super(Long.class, ApiRequestLog.class, TABLE_NAME);
	}

	@Resource
	private ApiRequestLogDao apiRequestLogDao;

	@Override
	protected ApiRequestLogDao getDao() {
		return this.apiRequestLogDao;
	}

	@Override
	protected void initServiceFilters(List<IServiceFilter<Long, ApiRequestLog>> serviceFilters) {
		serviceFilters.add(new IdGenerateFilter<>(TABLE_NAME));
	}

	@Override
	protected void initProps(List<PropertyWrapper<?>> props) {
		super.initProps(props);
		
		PropertyWrapper<?> prop = null;
		
		prop = new PropertyWrapper<String>(new BaseField("apiKey", "接口号", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<String>(new BaseField("serverId", "服务器id", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<String>(new BaseField("reqIp", "请求ip", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<String>(new BaseField("reqUrl", "requestUrl", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<Date>(new BaseField("reqStartTime", "请求开始时间", Date.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<Date>(new BaseField("reqEndTime", "请求结束时间", Date.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<Integer>(new BaseField("status", "请求状态", Integer.class), null);
		props.add(prop);
	}

}
