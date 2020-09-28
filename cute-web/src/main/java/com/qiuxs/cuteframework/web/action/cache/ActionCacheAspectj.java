package com.qiuxs.cuteframework.web.action.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.core.basic.utils.CollectionUtils;
import com.qiuxs.cuteframework.tech.mc.redis.comn.CommonCachePool;
import com.qiuxs.cuteframework.web.action.IAction;
import com.qiuxs.cuteframework.web.bean.ActionResult;
import com.qiuxs.cuteframework.web.bean.ReqParam;

/**
 * Action级别缓存拦截器
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年9月28日 下午1:55:52 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
@Aspect
@Component
public class ActionCacheAspectj {

	private static Logger log = LoggerFactory.getLogger(ActionCacheAspectj.class);
	
	@Around("@annotation(ac)")
	public Object beforeAspect(ProceedingJoinPoint point, ActionCache ac) throws Throwable {
		try {
			Object target = point.getTarget();
			if (!(target instanceof IAction)) {
				throw new IllegalAccessException("@ActionCache only allow in " + IAction.class.getName());
			}
			Object[] args = point.getArgs();
			ReqParam param = (ReqParam) args[0];
			String[] keys = ac.keys();
			if (keys.length == 0) {
				throw new IllegalArgumentException("@ActionCache.keys not allow empty");
			}
			String key = CommonCachePool.genKeyByMap(param, param.getApiKey(), CollectionUtils.genSet(keys));
			ActionResult actionResult = CommonCachePool.get(key);
			if (actionResult == null) {
				actionResult = (ActionResult) point.proceed();
				CommonCachePool.put(key, ac.expiresIn(), actionResult);
			} else {
				log.info("ActionResult form Cache " + key);
			}
			return actionResult;
		} catch (Throwable e) {
			throw e;
		}
	}

}
