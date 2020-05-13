package com.qiuxs.cuteframework.core.basic.bean;

import java.io.Serializable;

public class Three<V1, V2, V3> implements Serializable {

	private static final long serialVersionUID = -7144190144045178439L;

	private V1 v1;
	private V2 v2;
	private V3 v3;

	public Three(V1 v1, V2 v2, V3 v3) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
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

	public V3 getV3() {
		return v3;
	}

	public void setV3(V3 v3) {
		this.v3 = v3;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((v1 == null) ? 0 : v1.hashCode());
		result = prime * result + ((v2 == null) ? 0 : v2.hashCode());
		result = prime * result + ((v3 == null) ? 0 : v3.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Three<?, ?, ?> other = (Three<?, ?, ?>) obj;
		if (v1 == null) {
			if (other.v1 != null)
				return false;
		} else if (!v1.equals(other.v1))
			return false;
		if (v2 == null) {
			if (other.v2 != null)
				return false;
		} else if (!v2.equals(other.v2))
			return false;
		if (v3 == null) {
			if (other.v3 != null)
				return false;
		} else if (!v3.equals(other.v3))
			return false;
		return true;
	}

}
