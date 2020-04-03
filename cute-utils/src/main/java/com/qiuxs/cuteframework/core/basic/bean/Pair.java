package com.qiuxs.cuteframework.core.basic.bean;

import java.io.Serializable;

public class Pair<V1, V2> implements Serializable {

	private static final long serialVersionUID = 5598719150299261748L;
	
	private V1 v1;
	private V2 v2;

	public Pair() {
	}

	public Pair(V1 v1, V2 v2) {
		this.v1 = v1;
		this.v2 = v2;
	}

	public V1 getV1() {
		return v1;
	}

	public void setV1(V1 v1) {
		this.v1 = v1;
	}

	public V2 getV2() {
		return v2;
	}

	public void setV2(V2 v2) {
		this.v2 = v2;
	}

}
