package com.hzecool.frm.monitor.controller;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IPListController {
//	@RequestMapping({"/ip", "/devops/ip"})
//	@ResponseBody
//	public String getIP() throws Exception {
//		InetAddress local = InetAddress.getLocalHost();
//		String host = local.getHostName();
//		String s1 = host;
//		InetAddress[] a = InetAddress.getAllByName(host);
//		for (int i = 0; i < a.length; i++) {
//			s1 += " " + a[i].getHostAddress();
//		}
//		return s1;
//	}

	@RequestMapping({ "/ip", "/devops/ip" })
	@ResponseBody
	public String getIPList() throws Exception {
		StringBuilder sb = new StringBuilder();
		Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
		while (nis.hasMoreElements()) {
			NetworkInterface ni = (NetworkInterface) nis.nextElement();

			Enumeration<InetAddress> inetAddrs = ni.getInetAddresses();
			StringBuilder addrsSb = new StringBuilder();
			while (inetAddrs.hasMoreElements()) {
				InetAddress inetAddress = (InetAddress) inetAddrs.nextElement();
				String addr = inetAddress.getHostAddress();
				if (inetAddress instanceof Inet4Address && !addr.equalsIgnoreCase("127.0.0.1")) {
					addrsSb.append(inetAddress.getHostName()).append(" ").append(addr).append(" ");
				}
			}
			if (addrsSb.length() > 0) {
				sb.append(addrsSb);
			}
		}
		return sb.toString();
	}

}
