package com.qiuxs.rmq.log.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qiuxs.rmq.log.dao.TransRevcDao;
import com.qiuxs.rmq.log.entity.TransRevc;
import com.qiuxs.rmq.log.service.ITransRevcService;

@Service
public class TransRevcService implements ITransRevcService {
	
	@Resource
	private TransRevcDao transRevsDao;

	@Override
	public void appendTrans(Long id, Long unitId) {
		this.transRevsDao.insert(new TransRevc(id, unitId));
	}

	@Override
	public boolean existsTx(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

}
