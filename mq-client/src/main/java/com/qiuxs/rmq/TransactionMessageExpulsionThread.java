package com.qiuxs.rmq;

import com.qiuxs.cuteframework.core.tx.IMQTxService;

public class TransactionMessageExpulsionThread extends Thread {
	
	private IMQTxService mqTxService;
	
	public TransactionMessageExpulsionThread(IMQTxService mqTxMessage) {
		this.mqTxService = mqTxMessage;
	}
	
	@Override
	public void run() {
		
	}
	
}
