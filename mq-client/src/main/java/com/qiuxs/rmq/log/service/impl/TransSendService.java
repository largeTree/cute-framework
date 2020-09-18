package com.qiuxs.rmq.log.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional
	public Long appendTransSend(Long unitId) {
		Long txId = IDGenerateUtil.getNextLongId(TABLE_NAME);
		if (unitId == null) {
			unitId = 0L;
		}
		this.tranSendDao.insert(new TransSend(txId, unitId));
		return txId;
	}

	@Override
	@Transactional
	public boolean existsTx(Long id) {
		return this.tranSendDao.get(id) != null;
	}
	
}
