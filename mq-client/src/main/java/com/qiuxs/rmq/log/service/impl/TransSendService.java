package com.qiuxs.rmq.log.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.core.persistent.util.IDGenerateUtil;
import com.qiuxs.rmq.log.dao.TransSendDao;
import com.qiuxs.rmq.log.entity.TransSend;
import com.qiuxs.rmq.log.service.ITransSendService;

@Service
public class TransSendService implements ITransSendService {

	private static final String TABLE_NAME = "trans_send";

	@Resource
	private TransSendDao tranSendDao;

	@Override
	public Long appendTransSend(Long unitId) {
		Long txId = IDGenerateUtil.getNextLongId(TABLE_NAME);
		this.tranSendDao.insert(new TransSend(txId, unitId));
		return txId;
	}

	@Override
	public boolean existsTx(Long id) {
		return this.tranSendDao.get(id) != null;
	}
	
}
