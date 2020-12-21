package com.qiuxs.cuteframework.core.tx.mq;

import java.io.Serializable;

import com.qiuxs.cuteframework.tech.microsvc.disttx.DistTransInfo;

public interface TxMessage extends Serializable {

	public DistTransInfo getDistTx();

	public long getBorntime();

}
