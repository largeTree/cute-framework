package com.qiuxs.wxpay;

import org.junit.Test;

import com.qiuxs.cuteframework.core.basic.utils.JsonUtils;
import com.qiuxs.wxpay.req.TransferToOpenIdRequest;
import com.qiuxs.wxpay.resp.TransferToOpenIdResponse;

public class WxPayClientTester {
	
	@Test
	public void testTransferToOpenId() {
		WxPayClient client = new WxPayClient("https://api.mch.weixin.qq.com", "xxx", "xxx", "xxx", "xxx");
		client.statr();
		
		TransferToOpenIdRequest req = new TransferToOpenIdRequest();
		req.setAmount(30);
		req.setCheckName(TransferToOpenIdRequest.CheckName.NO_CHECK.name());
		req.setDesc("购有惠收益提现");
		req.setOpenid("oc5HrshYr5k4OBCQOfsp_qjUw4oY");
		req.setPartnerTradeNo("202005141052000201");
		TransferToOpenIdResponse resp = client.execute(req);
		System.out.println(JsonUtils.toJSONString(resp));
	}

}
