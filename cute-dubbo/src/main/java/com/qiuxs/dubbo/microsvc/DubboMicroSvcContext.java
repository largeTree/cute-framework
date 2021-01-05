package com.qiuxs.dubbo.microsvc;

import org.apache.dubbo.rpc.Invocation;

import com.qiuxs.cuteframework.tech.microsvc.MicroSvcContext;

public class DubboMicroSvcContext extends MicroSvcContext {
	
	// private Invocation invocation;
	
	public DubboMicroSvcContext(Invocation invocation) {
		super(invocation.getAttachments());
		// this.invocation = invocation;
	}

}
