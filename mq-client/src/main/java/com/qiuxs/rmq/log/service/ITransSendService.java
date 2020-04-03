package com.qiuxs.rmq.log.service;

public interface ITransSendService {
	
	public Long appendTransSend(Long unitId);

	public boolean existsTx(Long id);
	
}
