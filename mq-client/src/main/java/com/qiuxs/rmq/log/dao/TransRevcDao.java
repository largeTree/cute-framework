package com.qiuxs.rmq.log.dao;

import com.qiuxs.cuteframework.tech.mybatis.dao.MyBatisRepository;
import com.qiuxs.rmq.log.entity.TransRevc;

@MyBatisRepository
public interface TransRevcDao {
	
	public void insert(TransRevc bean);
	
	public void get(Long txId);
	
}
