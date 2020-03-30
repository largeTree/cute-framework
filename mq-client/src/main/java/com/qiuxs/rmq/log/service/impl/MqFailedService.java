package com.qiuxs.rmq.log.service.impl;

import java.util.List;
import javax.annotation.Resource;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import com.qiuxs.cuteframework.core.persistent.database.modal.PropertyWrapper;
import com.qiuxs.cuteframework.core.persistent.database.modal.BaseField;
import com.qiuxs.cuteframework.core.persistent.database.service.AbstractDataPropertyUKService;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.persistent.database.service.filter.IServiceFilter;
import com.qiuxs.cuteframework.core.persistent.database.service.filter.impl.IdGenerateFilter;
import com.qiuxs.rmq.log.dao.MqFailedDao;
import com.qiuxs.rmq.log.entity.MqFailed;
import com.qiuxs.rmq.log.service.IMqFailedService;
/**
 * MQ消息消费失败记录服务类
 *
 * @author qiuxs
 *
 */
@Service
public class MqFailedService extends AbstractDataPropertyUKService<Long, MqFailed, MqFailedDao> implements IMqFailedService {

	private static final String TABLE_NAME = "mq_failed";
	private static final String PK_FIELD = "id";

	public MqFailedService() {
		super(Long.class, MqFailed.class, TABLE_NAME, PK_FIELD);
	}

	@Resource
	private MqFailedDao mqFailedDao;

	@Override
	protected MqFailedDao getDao() {
		return this.mqFailedDao;
	}
	
	public MqFailed getByUk(String msgId) {
		return super.getByUkInner("msgId", msgId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteByUk(String msgId) {
		return super.deleteByUkInner("msgId", msgId);
	}
	
	public boolean isExistByUk(String msgId) {
		return super.isExistByUkInner("msgId", msgId);
	}
	
	public boolean isExistOtherByUk(Long pk, String msgId) {
		return super.isExistOtherByUkInner(pk, "msgId", msgId);
	}
	
	protected void createInner(MqFailed bean) {
		if (!this.isExistByUk(bean.getMsgId())) {
			this.getDao().insert(bean);
		} else {
			ExceptionUtils.throwLoginException("dup_records");
		}
	}

	@Override
	protected void initServiceFilters(List<IServiceFilter<Long, MqFailed>> serviceFilters) {
		serviceFilters.add(new IdGenerateFilter<>(TABLE_NAME));
	}

	@Override
	protected void initProps(List<PropertyWrapper<?>> props) {
		super.initProps(props);
		
		PropertyWrapper<?> prop = null;
		
		prop = new PropertyWrapper<String>(new BaseField("msgId", "消息ID", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<String>(new BaseField("topic", "消息主题", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<String>(new BaseField("tags", "消息标签", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<String>(new BaseField("bizKey", "消息业务主键", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<String>(new BaseField("bizBody", "业务数据", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<String>(new BaseField("stacktrace", "调用栈", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<String>(new BaseField("extProp", "扩展属性", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<Integer>(new BaseField("reconsumeTimes", "重新消费次数", Integer.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<Date>(new BaseField("bornTime", "消息生成时间", Date.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<String>(new BaseField("serverId", "服务器", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<Long>(new BaseField("userId", "用户ID", Long.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<Long>(new BaseField("unitId", "单元ID", Long.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<Long>(new BaseField("globalId", "GlobalId", Long.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<Date>(new BaseField("createdDate", "新增时间", Date.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<Date>(new BaseField("updatedDate", "修改时间", Date.class), null);
		props.add(prop);
	}

}
