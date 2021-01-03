package com.qiuxs.dubbo.microsvc;

import com.alibaba.dubbo.rpc.Invocation;
import com.qiuxs.cuteframework.tech.microsvc.MicroSvcContext;

public class DubboMicroSvcContext extends MicroSvcContext {
	
	private Invocation invcation;
	
	public DubboMicroSvcContext(Invocation invcation) {
		super(invcation.getAttachments());
		this.invcation = invcation;
	}

}
