package com.qiuxs.cuteframework.core.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.function.Function;

import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.tech.mc.McFactory;

/**
 * 复杂功能封装
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年3月21日 下午1:32:18 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class FunctionUtils {

	/**
	 * 捕获异常
	 *  
	 * @author qiuxs  
	 * @param <V>
	 * @param call
	 * @return
	 */
	public static <V> V catchThrows(Callable<V> call) {
		try {
			return call.call();
		} catch (Throwable e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * 根据指定的lockName进行双锁检查获取某个数据
	 *  
	 * @author qiuxs  
	 * @param <V>
	 * @param lockName
	 * @param tester
	 * @param callFirst
	 * @param callSec
	 * @return
	 */
	public static <V> V doubleLockCheckGet(String lockName, TestFunction<V> tester, Callable<V> callFirst, Callable<V> callSec) {
		V res = null;
		try {
			res = callFirst.call();
		} catch (Exception e) {
			throw ExceptionUtils.unchecked(e);
		}
		if (!tester.test(res)) {
			Lock lock = null;
			try {
				lock = McFactory.getFactory().getLock(lockName);
				lock.lock();
				res = callFirst.call();
				if (!tester.test(res)) {
					res = callSec.call();
				}
			} catch (Exception e) {
				throw ExceptionUtils.unchecked(e);
			} finally {
				if (lock != null) {
					lock.unlock();
				}
			}
		}
		return res;
	}

	/**
	 * 根据指定的lockName进行双锁检查获取某个数据
	 *  
	 * @author qiuxs  
	 * @param <V>
	 * @param lockName
	 * @param tester
	 * @param callFirst
	 * @param callSec
	 * @return
	 */
	public static <V> V doubleLockCheckGet(String lockName, TestFunction<V> tester, Callable<V> getFirst, Function<V, V> getSec) {
		V res = null;
		try {
			res = getFirst.call();
		} catch (Exception e) {
			throw ExceptionUtils.unchecked(e);
		}
		if (!tester.test(res)) {
			Lock lock = null;
			try {
				lock = McFactory.getFactory().getLock(lockName);
				lock.lock();
				res = getFirst.call();
				if (!tester.test(res)) {
					res = getSec.apply(res);
				}
			} catch (Exception e) {
				throw ExceptionUtils.unchecked(e);
			} finally {
				if (lock != null) {
					lock.unlock();
				}
			}
		}
		return res;
	}

}
