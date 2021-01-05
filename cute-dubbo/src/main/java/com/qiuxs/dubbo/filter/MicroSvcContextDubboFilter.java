package com.qiuxs.dubbo.filter;

import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;

import com.qiuxs.dubbo.microsvc.DubboMicroSvcContext;

public class MicroSvcContextDubboFilter implements Filter {

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		if (RpcContext.getContext().isConsumerSide()) {
			DubboMicroSvcContext microContext = new DubboMicroSvcContext(invocation);
			microContext.putMicroContext();
		} else {
			DubboMicroSvcContext microContext = new DubboMicroSvcContext(invocation);
			microContext.cacheContextFromSource();
		}
		return invoker.invoke(invocation);
	}

}
