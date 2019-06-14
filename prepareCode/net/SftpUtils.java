/**   
 * Copyright © 2018 , 杭州衣科信息技术有限公司-版权所有
 * 
 * 功能描述：TODO<br/>
 * @Package: org.test 
 * @author: zhengc 
 * @date: 2018年7月21日 下午5:00:27 
 */
package com.hzecool.fdn.utils.net;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.hzecool.fdn.utils.StringUtils;
import com.hzecool.fdn.utils.io.ZipUtil;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * @author:zhengc
 * @date: 2018年7月21日 下午5:00:27
 * @version 修改历史: Date Author Description <br>
 *          -----------------------------------------------*<br>
 *          2018年7月21日 zhengc 修改原因 <br>
 */
public class SftpUtils {

	private final static int time_out = 1 * 1000;

	/**
	 * 
	 * @Description: 将文件上传到服务器
	 * @param fileName(在服务器上的文件名)
	 * @param input
	 * @param channelSftp
	 * @return
	 * 
	 * @author: zhengc
	 * @date: 2018年7月21日 下午5:18:35
	 *
	 */
	private static boolean uploadFile(String fileName, InputStream input, ChannelSftp channelSftp) {
		OutputStream outstream = null;
		boolean successFlag = false;
		try {
			outstream = channelSftp.put(fileName);
			byte b[] = new byte[1024];
			int n;
			while ((n = input.read(b)) != -1) {
				outstream.write(b, 0, n);
			}
			outstream.flush();
			successFlag = true;
		} catch (Exception e) {
			// 日志记录下
		} finally {
			try {
				if (input != null) {
					input.close();
				}
				if (outstream != null) {
					outstream.close();
				}
			} catch (IOException e) {
				// 关闭流失败
			}
		}
		return successFlag;
	}

	/**
	 * @Description: 创建会话
	 * @param ftpHost
	 * @param ftpPort
	 * @param ftpUserName
	 * @param ftpPassword
	 * @param privateKey
	 *            秘钥
	 * @param passphrase
	 *            私钥
	 * @return
	 * @throws JSchException
	 * 
	 * @author: zhengc
	 * @date: 2018年7月21日 下午5:13:25
	 *
	 */
	private static Session initJschSession(String ftpHost, Integer ftpPort, String ftpUserName, String ftpPassword,
			String privateKey, String passphrase) throws JSchException {
		JSch jsch = new JSch(); // 创建JSch对象
		if (StringUtils.isNotBlank(privateKey) && StringUtils.isNotBlank(passphrase)) {
			jsch.addIdentity(privateKey, passphrase);
		}
		if (StringUtils.isNotBlank(privateKey) && StringUtils.isBlank(passphrase)) {
			jsch.addIdentity(privateKey);
		}
		Session session = jsch.getSession(ftpUserName, ftpHost, ftpPort); // 根据用户名，主机ip，端口获取一个Session对象
		if (StringUtils.isNotBlank(ftpPassword)) {
			session.setPassword(ftpPassword); // 设置密码
		}
		return session;
	}

	/**
	 * 获取ChannelSftp链接
	 * 
	 * @param timeout
	 *            超时时间
	 * @return 返回ChannelSftp对象
	 * @throws JSchException
	 */
	private static ChannelSftp getChannelSftp(Session session, int timeout) throws JSchException {
		Channel channel = null;
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config); // 为Session对象设置properties
		session.setTimeout(timeout); // 设置timeout时间
		session.connect(); // 通过Session建立链接
		channel = session.openChannel("sftp"); // 打开SFTP通道
		channel.connect(); // 建立SFTP通道的连接
		return (ChannelSftp) channel;
	}

	/**
	 * 断开sftp链接
	 * 
	 * @param session
	 *            会话
	 * @param channel
	 *            通道
	 */
	private static void closeConnection(Channel channel, Session session) {
		try {
			if (session != null) {
				session.disconnect(); // 关闭session链接
			}
			if (channel != null) {
				channel.disconnect(); // 断开连接
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Description: 将文件上传到服务器
	 * @param fileName(在服务器上的文件名,有对应文件夹的，需要包含文件夹)
	 * @param input
	 * @param ftpHost
	 * @param ftpPort
	 * @param ftpUserName
	 * @param ftpPassword
	 * @param privateKey
	 * @param passphrase
	 * @return
	 * 
	 * @author: zhengc
	 * @return
	 * @date: 2018年7月21日 下午5:23:59
	 *
	 */
	public static boolean uploadFile(String fileName, InputStream input, String ftpHost, Integer ftpPort,
			String ftpUserName, String ftpPassword, String privateKey, String passphrase) {
		boolean success = false;
		Session session = null;
		ChannelSftp channel = null;
		try {
			session = initJschSession(ftpHost, ftpPort, ftpUserName, ftpPassword, privateKey, passphrase);
			channel = getChannelSftp(session, time_out);
			success = uploadFile(fileName, input, channel);
		} catch (JSchException e) {
		} finally {
			closeConnection(channel, session);
		}
		return success;
	}

	public static void main(String[] args) {
		String str = "txt";
		// 根据str生成二进制输入流（zip 里面是txt）
		InputStream input = new ByteArrayInputStream(ZipUtil.zipString(str, "text" + ".txt"));
		boolean flag = uploadFile("pub/text.zip", input, "192.168.0.35", 9231, "spftp", "CottonXu", null, null);
		System.out.println(flag);
	}

}
