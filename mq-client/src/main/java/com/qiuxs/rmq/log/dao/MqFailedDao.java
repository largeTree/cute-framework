package com.qiuxs.rmq.log.dao;

import com.qiuxs.cuteframework.core.persistent.database.dao.IBaseUKDao;
import com.qiuxs.cuteframework.tech.mybatis.dao.MyBatisRepository;
import com.qiuxs.rmq.log.entity.MqFailed;

/**
 * MQ消息消费失败记录Dao接口
 * 
 * 创建时间 ：2020-03-30 14:25:46
 * @author qiuxs
 *
 */
@MyBatisRepository
public interface MqFailedDao extends IBaseUKDao<Long, MqFailed> {

}
