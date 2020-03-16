package com.qiuxs.cuteframework.mock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.qiuxs.cuteframework.core.persistent.util.IDGenerateUtil;

import mockit.Mock;
import mockit.MockUp;

/**
 * 序列生成器mock
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年3月16日 下午9:27:31 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class IDGenerateUtilMock {

	private static Map<String, AtomicLong> idMap = new HashMap<String, AtomicLong>() {
		private static final long serialVersionUID = -1307029521688205132L;
		
		public AtomicLong get(Object key) {
			AtomicLong along = super.get(key);
			if (along == null) {
				along = new AtomicLong();
				super.put((String) key, along);
			}
			return along;
		}
		
	};

	public static void mock() {
		new MockUp<IDGenerateUtil>(IDGenerateUtil.class) {
			@Mock
			public Long getNextLongId(String tableName) {
				AtomicLong atomicLong = idMap.get(tableName);
				return atomicLong.incrementAndGet();
			}
		};
	}

}
