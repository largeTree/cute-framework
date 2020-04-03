package com.qiuxs.rmq.log.dao;

import com.qiuxs.cuteframework.tech.mybatis.dao.MyBatisRepository;
import com.qiuxs.rmq.log.entity.TransSend;

@MyBatisRepository
public interface TransSendDao {

	public void insert(TransSend bean);

	public TransSend get(Long txId);

}
