package com.qiuxs.cuteframework.tech.mybatis.interceptor.utils;

import java.util.Collections;
import java.util.List;

import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;
import com.qiuxs.cuteframework.tech.mybatis.interceptor.hook.IMbiHook;

/**
 * MyBatis钩子工具
 * 
 * @author qiuxs
 * 
 *         创建时间 ： 2018年8月17日 下午8:36:54
 *
 */
public class MbiHookUtils {

	private static List<IMbiHook> mbiHooks;

	/**
	 * 获取所有MyBatis钩子
	 * 
	 * @author qiuxs
	 *
	 * @return
	 *
	 * 		创建时间：2018年8月17日 下午8:37:07
	 */
	public static List<IMbiHook> getMbiHooks() {
		if (mbiHooks == null) {
			synchronized (MbiHookUtils.class) {
				if (mbiHooks == null) {
					mbiHooks = ApplicationContextHolder.getBeansForType(IMbiHook.class);
					Collections.sort(mbiHooks,
							(o1, o2) -> o1.getOrder() > o2.getOrder() ? 1 : o1.getOrder() == o2.getOrder() ? 0 : -1);
				}
			}
		}
		return mbiHooks;
	}

}
