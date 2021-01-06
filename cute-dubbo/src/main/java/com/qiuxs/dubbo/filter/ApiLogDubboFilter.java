package com.qiuxs.dubbo.filter;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.util.StopWatch;

import com.qiuxs.dubbo.DubboConstants;
import com.qiuxs.dubbo.DubboLogger;

public class ApiLogDubboFilter implements Filter {

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		if (RpcContext.getContext().isConsumerSide()) {
			StopWatch sw = new StopWatch();
			URL url = invoker.getUrl();
			String invokeKey = url.getProtocol() + "://" + url.getParameter(DubboConstants.URL_PARAM_SERVER_ID) + "/" + url.getServiceInterface() + "." + invocation.getMethodName();
			sw.start();
			try {
				return invoker.invoke(invocation);
			} finally {
				sw.stop();
				DubboLogger.logger.info("invoke {} CostMs:{}", invokeKey, sw.getLastTaskTimeMillis());
			}
		} else {
			return invoker.invoke(invocation);
		}
	}

}
