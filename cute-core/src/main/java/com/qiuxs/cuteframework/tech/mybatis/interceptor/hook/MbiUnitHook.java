package com.qiuxs.cuteframework.tech.mybatis.interceptor.hook;

import java.util.Map;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.core.basic.bean.UserLite;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.context.UserContext;
import com.qiuxs.cuteframework.core.persistent.database.entity.IUnitId;
import com.qiuxs.cuteframework.core.persistent.database.lookup.DataSourceContext;
import com.qiuxs.cuteframework.tech.mybatis.interceptor.utils.MbiUtils;

@Component
public class MbiUnitHook implements IMbiHook {

	private static Logger log = LogManager.getLogger(MbiUnitHook.class);

	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public void beforeExecutor(Invocation invocation) {
		Object[] args = invocation.getArgs();
		MappedStatement ms = (MappedStatement)args[0];
		Object parameter = args[1];
		if (parameter instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> paramMap = (Map<String, Object>) parameter;
			String nameSpace = MbiUtils.getNameSpace(ms);
			Class<?> pojoClass = DataSourceContext.getPojoClass(nameSpace);
			UserLite userLite = UserContext.getUserLiteOpt();
			if (userLite != null) { 
				// 是否是继承自IUnitId的，是的话设置一下unitId
				if (pojoClass != null && IUnitId.class.isAssignableFrom(pojoClass)) {
					Long unitId = userLite.getUnitId();
					putIfNeed(paramMap, "unitId", unitId, -3L);
				}
			}
		}
	}

	private void putIfNeed(Map<String, Object> paramMap, String key, Object ctxVal, Object ignoreVal) {
		if (log.isDebugEnabled()) {
			log.debug("FieldName = " + key + ", ctxVal = " + ctxVal + ", ignoreVal = " + ignoreVal + ", paramMap = " + paramMap);
		}
		Object oldVal = paramMap.containsKey(key) ? paramMap.get(key) : null;
		if (oldVal == null) {
			if (ctxVal != null && !ctxVal.equals(ignoreVal)) {
				paramMap.put(key, ctxVal);
			}
		} else if (StringUtils.equals(oldVal.toString(), String.valueOf(ignoreVal))) {
			paramMap.remove(key);
		}
	}

}
