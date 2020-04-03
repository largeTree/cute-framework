package com.qiuxs.rmq.log.service;

public interface ITransRevcService {
	
	public void appendTrans(Long id, Long unitId);
	
	public boolean existsTx(Long id);
	
}
