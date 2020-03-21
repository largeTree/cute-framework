package com.qiuxs.upyun.dto;

import java.io.Serializable;

/**
 * 文件信息
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年3月21日 下午3:56:31 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class FileInfo implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -9104860203784231384L;

	/**  文件是否存在. */
	private boolean exists;
	/**  文件类型. */
	private String type;

	/**  大小. */
	private long size;

	/**  创建日期. */
	private String date;

	/**  md5. */
	private String md5;

	/**
	 * Checks if is 文件是否存在.
	 *
	 * @return the 文件是否存在
	 */
	public boolean isExists() {
		return exists;
	}

	/**
	 * Sets the 文件是否存在.
	 *
	 * @param exists the new 文件是否存在
	 */
	public void setExists(boolean exists) {
		this.exists = exists;
	}

	/**
	 * Gets the 文件类型.
	 *
	 * @return the 文件类型
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the 文件类型.
	 *
	 * @param type the new 文件类型
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the 大小.
	 *
	 * @return the 大小
	 */
	public long getSize() {
		return size;
	}

	/**
	 * Sets the 大小.
	 *
	 * @param size the new 大小
	 */
	public void setSize(long size) {
		this.size = size;
	}

	/**
	 * Gets the 创建日期.
	 *
	 * @return the 创建日期
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Sets the 创建日期.
	 *
	 * @param date the new 创建日期
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Gets the md5.
	 *
	 * @return the md5
	 */
	public String getMd5() {
		return md5;
	}

	/**
	 * Sets the md5.
	 *
	 * @param md5 the new md5
	 */
	public void setMd5(String md5) {
		this.md5 = md5;
	}

}
