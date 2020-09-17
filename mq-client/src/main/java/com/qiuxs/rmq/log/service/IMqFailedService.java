package com.qiuxs.rmq.log.service;

import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDataPropertyService;
import com.qiuxs.rmq.log.entity.MqFailed;


/**
 * MQ消息消费失败记录服务接口
 * 
 * 创建时间 ：2020-03-30 14:25:46
 * @author qiuxs
 */
public interface IMqFailedService extends IDataPropertyService<Long, MqFailed> {

	public MqFailed getByUk(String msgId);

	public int deleteByUk(String msgId);

}
