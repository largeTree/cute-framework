package com.qiuxs.cuteframework.core.basic.bean;

/**
 * 可以合并的对象
 * @author qiuxs
 *
 */
public interface IMergeable<T> {

	public void merge(T margeable);

}
