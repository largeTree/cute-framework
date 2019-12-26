package com.qiuxs.cuteframework.core.basic.utils.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.commons.lang3.StringUtils;

import com.qiuxs.cuteframework.core.basic.utils.ip2region.Ip2RegionFacade;
import com.qiuxs.cuteframework.core.basic.utils.ip2region.RegionBean;

/**
 * 
 * 功能描述: 网络相关的工具类，除了Request、Response、Http请求、Cookie等特定的大类工具类外
 * <p>新增原因: TODO
 *  
 * @author fengdg   
 * @version 1.0.0
 * @since 2017年3月17日 下午3:18:29
 */
public class NetUtils {
	public static final String PROTOCOL_HTTPS = "https";
	public static final String PROTOCOL_HTTP = "http";

	public static String getAddrPort(Socket socket) {
		String addrPort = null;
		if (socket != null) {
			String localAddress = socket.getLocalAddress().getHostAddress();
			int localPort = socket.getLocalPort();
			addrPort = "local=" + localAddress + ":" + localPort + " => ";
			addrPort += socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
		}
		return addrPort;
	}

	//获得本机内网ip
	public static String getLocalAddr() throws IOException {
		InetAddress ia = InetAddress.getLocalHost();
		return ia.getHostAddress();
	}

	/**
	 * 根据ip，获取省
	 * 
	 * @param ip
	 * @return
	 */
	public static String getProvinceId(String ip) {
		String provinceId = null;
		if (StringUtils.isNotBlank(ip)) {
			RegionBean city = Ip2RegionFacade.getRegion(ip);
			if (city != null && city.getProvinceId() != null) {
				provinceId = city.getProvinceId() + "";
			}
		}
		return provinceId;
	}

	public static String getUrl(String protocol, String host, int port, String context) {
		StringBuilder sb = new StringBuilder();
		sb.append(protocol).append("://").append(host);
		if ((port == 80 && protocol.equalsIgnoreCase(PROTOCOL_HTTP)) || (port == 443 && protocol.equalsIgnoreCase(PROTOCOL_HTTPS))) {
		} else {
			sb.append(":").append(port);
		}
		sb.append("/").append(context);
		return sb.toString();
	}
}
