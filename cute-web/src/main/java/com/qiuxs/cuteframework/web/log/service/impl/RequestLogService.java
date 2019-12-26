package com.qiuxs.cuteframework.web.log.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.core.basic.Constants.DsType;
import com.qiuxs.cuteframework.core.basic.code.DirectCodeCenter;
import com.qiuxs.cuteframework.core.basic.code.utils.CodeUtils;
import com.qiuxs.cuteframework.core.persistent.database.modal.BaseField;
import com.qiuxs.cuteframework.core.persistent.database.modal.PropertyWrapper;
import com.qiuxs.cuteframework.core.persistent.database.service.AbstractDataPropertyService;
import com.qiuxs.cuteframework.core.persistent.database.service.filter.IServiceFilter;
import com.qiuxs.cuteframework.core.persistent.database.service.filter.impl.IdGenerateFilter;
import com.qiuxs.cuteframework.web.log.dao.RequestLogDao;
import com.qiuxs.cuteframework.web.log.entity.RequestLog;
import com.qiuxs.cuteframework.web.log.service.IRequestLogService;
/**
 * 请求日志记录服务类
 *
 * @author qiuxs
 *
 */
@Service
public class RequestLogService extends AbstractDataPropertyService<Long, RequestLog, RequestLogDao> implements IRequestLogService {

	private static final String TABLE_NAME = "request_log";
	private static final String PK_FIELD = "id";

	public RequestLogService() {
		super(Long.class, RequestLog.class, TABLE_NAME, PK_FIELD);
		CodeUtils.genDirectCode(RequestLog.class);
	}

	@Resource
	private RequestLogDao apiRequestLogDao;

	@Override
	protected RequestLogDao getDao() {
		return this.apiRequestLogDao;
	}
	
	@Override
	public String getDsType() {
		return DsType.LOG.value();
	}
	
	/***
	 * 目的是去除事务标记
	 */
	@Override
	public void save(RequestLog bean) {
		super.save(bean);
	}

	@Override
	protected void initServiceFilters(List<IServiceFilter<Long, RequestLog>> serviceFilters) {
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
		
		prop = new PropertyWrapper<Integer>(new BaseField("status", "请求状态", Integer.class), DirectCodeCenter.getDirectCodeHouse(RequestLog.DOMAIN_STATUS));
		props.add(prop);
		
		prop = new PropertyWrapper<Integer>(new BaseField("globalId", "全局流水号", Long.class), null);
		props.add(prop);
	}

}
