package com.qiuxs.cuteframework.core.basic.utils.io;

import static com.qiuxs.cuteframework.core.basic.utils.io.IOUtils.closeQuietly;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qiuxs.cuteframework.core.basic.Constants;

/**
 * 压缩解压工具类
 * @author qiuxs
 * 2019年6月15日 下午9:57:49
 */
public class ZipUtil {

	private static final Logger log = LogManager.getLogger(ZipUtil.class);

	/**
	 * 解压gzip压缩的文本
	 * 
	 * 2019年6月15日 下午9:58:11
	 * @auther qiuxs
	 * @param str
	 * @return
	 * @throws IOException
	 */
	public static String ungzipString(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return str;
		}
		GZIPInputStream gis = null;
		ByteArrayOutputStream out = null;
		try {
			byte[] compressed = Base64.decodeBase64(str);
			InputStream is = new ByteArrayInputStream(compressed);
			gis = new GZIPInputStream(is);
			out = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int n;
			while ((n = gis.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
			return out.toString("UTF-8");
		} catch (IOException e) {
			throw e;
		} finally {
			closeQuietly(out);
			closeQuietly(gis);
		}
	}

	/**
	 * 以gzip方式压缩文本
	 * 
	 * 2019年6月15日 下午10:04:01
	 * @auther qiuxs
	 * @param str
	 * @return
	 * @throws IOException
	 */
	public static String gzipString(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return str;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(str.getBytes("UTF-8"));
		gzip.close();
		String compressedStr = Base64.encodeBase64String(out.toByteArray());
		return compressedStr;
	}

	/**
	 * 这个是为了有些情况下调用方便封装的，不抛出异常
	 * 
	 * 2019年6月15日 下午9:58:21
	 * @auther qiuxs
	 * @param str
	 * @return
	 */
	public static String gzip(String str) {
		String ret = null;
		try {
			ret = gzipString(str);
		} catch (Exception e) {
			log.error("input=" + str + ",ret=" + ret, e);
		}
		return ret;
	}

	/**
	 * 解压gzip文本
	 * 
	 * 2019年6月15日 下午9:58:25
	 * @auther qiuxs
	 * @param str
	 * @return
	 */
	public static String ungzip(String str) {
		String ret = null;
		try {
			ret = ungzipString(str);
		} catch (Exception e) {
			log.error("input=" + str + ",ret=" + ret, e);
		}
		return ret;
	}

	/**
	 * 使用zip进行压缩文本
	 * 
	 * 2019年6月15日 下午9:58:43
	 * @auther qiuxs
	 * @param str
	 * @param entryName
	 * @return
	 */
	public static byte[] zipString(String str, String entryName) {
		if (str == null || str.length() == 0) {
			return null;
		}
		byte[] compressed = null;
		ByteArrayOutputStream out = null;
		ZipOutputStream zout = null;
		try {
			out = new ByteArrayOutputStream();
			zout = new ZipOutputStream(out);
			zout.putNextEntry(new ZipEntry(entryName));
			zout.write(str.getBytes(Constants.UTF_8));
			zout.closeEntry();
		} catch (IOException e) {
			compressed = null;
		} finally {
			closeQuietly(zout);
			closeQuietly(out);
		}
		if (out != null) {
			compressed = out.toByteArray();
		}
		return compressed;
	}

	/**
	 * 使用zip进行压缩文本
	 * 
	 * @param str
	 *            压缩前的文本
	 * @return 返回压缩后的文本
	 */
	public static String zipString(String str) {
		return Base64.encodeBase64String(zipString(str, "0"));
	}

	/**
	 * 使用zip进行解压缩
	 * 
	 * @param compressed
	 *            压缩后的文本
	 * @return 解压后的字符串
	 */
	public static String unzipString(String compressedStr) {
		if (compressedStr == null) {
			return null;
		}

		ByteArrayOutputStream out = null;
		ByteArrayInputStream in = null;
		ZipInputStream zin = null;
		String decompressed = null;
		try {
			byte[] compressed = Base64.decodeBase64(compressedStr);
			out = new ByteArrayOutputStream();
			in = new ByteArrayInputStream(compressed);
			zin = new ZipInputStream(in);
			zin.getNextEntry();
			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = zin.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			decompressed = out.toString();
		} catch (IOException e) {
			log.error("ext = " + e.getLocalizedMessage(), e);
			decompressed = null;
		} finally {
			closeQuietly(zin);
			closeQuietly(in);
			closeQuietly(out);
		}
		return decompressed;
	}

	/**
	 * 使用zip压缩文件或者文件夹
	 *  
	 * @author qiuxs  
	 * @param sourcePath
	 * @param zipPath
	 */
	public static boolean createZip(String sourcePath, String zipPath) {
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			fos = new FileOutputStream(zipPath);
			zos = new ZipOutputStream(fos);
			writeZip(new File(sourcePath), "", zos);
			return true;
		} catch (FileNotFoundException e) {
			log.error("创建ZIP文件失败", e);
		} catch (IOException e) {
			log.error("创建ZIP文件失败", e);
		} finally {
			closeQuietly(zos);
		}
		return false;
	}

	/**
	 * 创建zip文件
	 *  
	 * @author qiuxs  
	 * @param file
	 * @param parentPath
	 * @param zos
	 * @throws IOException 
	 */
	private static void writeZip(File file, String parentPath, ZipOutputStream zos) throws IOException {
		if (file.exists()) {
			if (file.isDirectory()) {// 处理文件夹
				parentPath += file.getName() + "/";
				File[] files = file.listFiles();
				if (files != null) {
					for (File f : files) {
						writeZip(f, parentPath, zos);
					}
				}
			} else {
				FileInputStream fis = null;
				DataInputStream dis = null;
				try {
					fis = new FileInputStream(file);
					dis = new DataInputStream(new BufferedInputStream(fis));
					ZipEntry ze = new ZipEntry(parentPath + file.getName());
					zos.putNextEntry(ze);
					byte[] content = new byte[1024];
					int len;
					while ((len = fis.read(content)) != -1) {
						zos.write(content, 0, len);
						zos.flush();
					}
				} catch (FileNotFoundException e) {
					throw e;
				} catch (IOException e) {
					throw e;
				} finally {
					closeQuietly(dis);
				}
			}
		}
	}

}
