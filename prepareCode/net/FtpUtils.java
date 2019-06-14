/**   
 * Copyright © 2018 , 杭州衣科信息技术有限公司-版权所有
 * 
 * 功能描述：TODO<br/>
 * @Package: org.util 
 * @author: zhengc 
 * @date: 2018年7月11日 下午6:28:04 
 */
package com.hzecool.fdn.utils.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 * @author:zhengc
 * @date: 2018年7月11日 下午6:28:04
 * @version 修改历史: Date Author Description <br>
 *          -----------------------------------------------*<br>
 *          2018年7月11日 zhengc 修改原因 <br>
 */
public class FtpUtils {
	/**
	 * 上传文件到ftp服务器
	 * @Description: 
	 * @param url FTP服务器hostname 
	 * @param port FTP服务器端口 
	 * @param username	FTP登录账号 
	 * @param password	FTP登录密码 
	 * @param path	FTP服务器保存目录(上传到xx目录就是传xx,不传就是根目录)
	 * @param filename	上传到FTP服务器上的文件名
	 * @param input	输入流
	 * @param isBinary	是否是字节传输
	 * @return 
	 * 
	 * @author: zhengc
	 * @date: 2018年7月11日 下午6:28:40 
	 *
	 */
	public static boolean uploadFile(String url, int port, String username, String password, String path,
			String filename, InputStream input,boolean isBinary) {
		boolean uploadFlag = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(url, port);// 连接FTP服务器
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
			if(isBinary)
				ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return uploadFlag;
			}
			ftp.changeWorkingDirectory(path);
			ftp.storeFile(filename, input);

			input.close();
			ftp.logout();
			uploadFlag = true;
		} catch (IOException e) {
			e.printStackTrace();
			//TODO 日志记录下
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return uploadFlag;
	}
	/**
	 * 上传文件到ftp服务器
	 * @Description: 
	 * @param url FTP服务器hostname 
	 * @param port FTP服务器端口 
	 * @param username	FTP登录账号 
	 * @param password	FTP登录密码 
	 * @param path	FTP服务器保存目录(上传到xx目录就是传xx,不传就是根目录)
	 * @param filename	要上传的文件名
	 * @param uploadFileName 上传到FTP服务器上的文件名
	 * @param isBinary	是否是字节传输(txt 不是，zip是)
	 * @return 
	 * 
	 * @author: zhengc
	 * @date: 2018年7月11日 下午6:34:20 
	 *
	 */
	public static boolean uploadFile(String url, int port, String username, String password, String path,
			String filename,String uploadFileName,boolean isBinary) {
		InputStream inputStream=getInputStream(filename);
		if(null == inputStream)
			return false;
		return uploadFile(url,port,username,password,path,uploadFileName,inputStream,isBinary);
	}
	private static InputStream getInputStream(String filename){
		File file=new File(filename);
		if(file.exists()){
			try {
				return new FileInputStream(file);
			} catch (FileNotFoundException e) {
				//TODO 日志记录下
				return null;
			}  
		}
		return null;
	}
}
