package com.qiuxs.cuteframework.web.common.mylog.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.core.basic.Constants.DsType;
import com.qiuxs.cuteframework.core.persistent.database.modal.BaseField;
import com.qiuxs.cuteframework.core.persistent.database.modal.PropertyWrapper;
import com.qiuxs.cuteframework.core.persistent.database.service.AbstractDataPropertyService;
import com.qiuxs.cuteframework.core.persistent.database.service.filter.IServiceFilter;
import com.qiuxs.cuteframework.core.persistent.database.service.filter.impl.IdGenerateFilter;
import com.qiuxs.cuteframework.web.common.mylog.dao.MylogDao;
import com.qiuxs.cuteframework.web.common.mylog.entity.Mylog;
import com.qiuxs.cuteframework.web.common.mylog.service.IMylogService;
/**
 * 服务类
 *
 * @author qiuxs
 *
 */
@Service
public class MylogService extends AbstractDataPropertyService<Long, Mylog, MylogDao> implements IMylogService {

	private static final String TABLE_NAME = "mylog";
	private static final String PK_FIELD = "id";

	public MylogService() {
		super(Long.class, Mylog.class, TABLE_NAME, PK_FIELD);
	}

	@Resource
	private MylogDao mylogDao;

	@Override
	protected MylogDao getDao() {
		return this.mylogDao;
	}
	
	@Override
	public String getDsType() {
		return DsType.LOG.value();
	}

	@Override
	protected void initServiceFilters(List<IServiceFilter<Long, Mylog>> serviceFilters) {
		serviceFilters.add(new IdGenerateFilter<>(TABLE_NAME));
	}

	@Override
	protected void initProps(List<PropertyWrapper<?>> props) {
		super.initProps(props);
		
		PropertyWrapper<?> prop = null;
		
		prop = new PropertyWrapper<String>(new BaseField("ip", "客户端IP地址", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<String>(new BaseField("serverId", "服务器ID", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<Long>(new BaseField("userId", "会话中用户ID", Long.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<Long>(new BaseField("globalId", "全局日志ID", Long.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<String>(new BaseField("level", "日志级别", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<String>(new BaseField("className", "类名", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<String>(new BaseField("method", "方法名", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<String>(new BaseField("msg", "日志消息文本", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<String>(new BaseField("throwable", "throwable", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<String>(new BaseField("stacktrace", "堆栈信息", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<String>(new BaseField("threadId", "线程ID", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<String>(new BaseField("errorCode", "错误代码", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<Date>(new BaseField("logTime", "日志时间", Date.class), null);
		props.add(prop);
	}

}
