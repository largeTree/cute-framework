package com.qiuxs.cuteframework.core.tx.mq;

import java.io.Serializable;

public interface TxMessage extends Serializable {

	public Long getTxId();

	public Object getMessage();
	
	public long getBorntime();

}
