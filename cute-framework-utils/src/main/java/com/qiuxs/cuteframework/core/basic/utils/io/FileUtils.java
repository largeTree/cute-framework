package com.qiuxs.cuteframework.core.basic.utils.io;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qiuxs.cuteframework.core.basic.constants.SymbolConstants;
import com.qiuxs.cuteframework.core.basic.utils.DateFormatUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.basic.utils.generator.UUIDUtils;

public class FileUtils {

	public static final String FILE_SCHEMA_SUFFIX = "://";// 文件schema后缀
	public static final String LINE_SEPARATOR = System.getProperties().getProperty("line.separator");
	public static final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");

	private static Logger log = LogManager.getLogger(FileUtils.class);

	public static InputStream readAsInputStream(String location) throws FileNotFoundException {
		if (StringUtils.isBlank(location)) {
			throw new NullPointerException("location is empty or Null!");
		}
		return new FileInputStream(location);
	}

	/**
	 * 使用指定的编码，把文件读取到字符串中。注意：适用于小文件。
	 * 
	 * @param fileName
	 *            文件名
	 * @param encode
	 *            编码
	 * @return
	 * @throws Exception
	 */
	public static String readFileToString(String fileName, String encode) throws Exception {
		return readFileToString(new FileInputStream(fileName), encode);
	}

	/**
	 * 使用指定的编码，把输入流读取到字符串中，注意：适用于小输入流。
	 * 
	 * @param in
	 *            输入流
	 * @param encode
	 *            编码
	 * @return
	 * @author zhangyz created on 2012-5-7
	 */
	public static String readFileToString(InputStream in, String encode) {
		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(in, encode);
			StringBuffer strBuffer = new StringBuffer("");
			int length = 500;
			char b[] = new char[length];
			int i;
			do {
				i = reader.read(b, 0, length);
				if (i < 0)
					break;
				strBuffer.append(new String(b, 0, i));
			} while (true);
			in.close();
			return strBuffer.toString();
		} catch (Exception ex) {
			throw new RuntimeException("读取文件流失败:" + ex.getMessage(), ex);
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	/**
	 * 读取文件内容
	 *  
	 * @author maozj  
	 * @param filePath 文件路径，例：xxxx/file/abc.txt
	 * @return
	 */
	@SuppressWarnings("resource")
	public static String readFileContent(String filePath) {
		StringBuffer strBuff = new StringBuffer();
		try {
			File file = new File(filePath);
			// 文件是否存在
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), "gbk");
				BufferedReader bufferRead = new BufferedReader(read);
				String strcon = null;
				while ((strcon = bufferRead.readLine()) != null) {
					strBuff.append(strcon);
				}
				return strBuff.toString();
			} else {
				throw new RuntimeException("找不到指定文件！");
			}
		} catch (Exception e) {
			throw new RuntimeException("读取文件内容错误, 失败：" + e.getMessage(), e);
		}
	}

	/**
	 * 根据传入的文件全路径，返回文件所在路径
	 * 
	 * @param fullPath
	 *            文件全路径
	 * @return 文件所在路径
	 * @roseuid 3FBE26DE029A
	 */
	public static String getDir(String fullPath) {
		int iPos1 = fullPath.lastIndexOf("/");
		int iPos2 = fullPath.lastIndexOf("\\");
		iPos1 = (iPos1 > iPos2 ? iPos1 : iPos2);
		return fullPath.substring(0, iPos1 + 1);
	}

	/**
	 * 把字符串内容存储到指定的目录里。
	 * 
	 * @param fullPath
	 *            目标文件名。
	 * @param content
	 *            内容
	 * @param replace
	 *            是否替换
	 * @return
	 * @throws Exception
	 */
	public static boolean saveStringToFile(String fullPath, String content, boolean replace, String encode) throws Exception {
		File f = new File(fullPath);
		if (f.exists()) {
			if (replace == false) {
				return true;
			}
			if (f.isFile() && replace == true) {
				if (f.delete() == false)
					return false;
			}
		} else {
			String dir = getDir(fullPath);
			f = new File(dir);
			if (!f.exists()) {
				if (f.mkdirs() == false)
					return false;
			}
		}

		java.io.FileOutputStream fw = null;
		try {
			fw = new java.io.FileOutputStream(fullPath);
			fw.write(content.getBytes(encode));
			/*
			 * FileWriter fw = new FileWriter(fullPath); fw.write(content);
			 */
			return true;
		} catch (IOException e) {
			throw new Exception("保存内容至文件" + fullPath + "失败:" + e.getMessage(), e);
		} finally {
			if (fw != null)
				fw.close();
		}
	}

	/**
	 * 判定在指定目录下有没有和指定的文件名同名的，如果有生成一个新文件名。新文件名是采用后面递增加1的办法。
	 * 
	 * @param path
	 *            文件路径
	 * @param fileName
	 *            文件名
	 * @return 没有重名的新文件对象，但没有实际创建它。
	 */
	public static File getFileNameNoSame(String path, String fileName) {
		File file = null;
		file = new File(path, fileName);
		if (file.exists() == false)// 不存在
			return file;

		String[] segs = divideFileName(fileName);
		int index = 1;
		do {
			fileName = segs[0] + index + segs[1];// 文件名加1
			file = new File(path, fileName);
			if (file.exists() == false)
				break;
			index++;
		} while (true);
		return file;
	}

	/**
	 * 判定在指定目录下有没有和指定的文件名同名的，如果有生成一个新文件名。新文件名是采用文件名加当前时间的办法。
	 * 
	 * @param path
	 *            文件路径
	 * @param fileName
	 *            文件名
	 * @return 没有重名的新文件对象，但没有实际创建它。
	 * @author qiuxs 2019-06-15
	 */
	public static File getFileNameNoSameAddTime(String path, String fileName) {
		File file = null;
		file = new File(path, fileName);
		if (file.exists() == false)// 不存在
			return file;

		String[] segs = divideFileName(fileName);
		// 分隔符
		String separator = "_";
		// 当前时间
		String dateTime = null;

		do {
			// 获取当前时间
			dateTime = DateFormatUtils.format(new Date(), DateFormatUtils.yyyy_MM_dd_HH_MM_SS_CN);
			// 拼接新文件名
			fileName = segs[0] + separator + dateTime + segs[1];
			file = new File(path, fileName);
			if (file.exists() == false)
				break;
		} while (true);
		return file;
	}

	/**
	 * 把一个文件名分隔成文件名本身和扩展名两部分。
	 * 
	 * @param name
	 * @return String[];0:为文件名本身加.号;1:为扩展名,含点号
	 */
	public static String[] divideFileName(String name) {
		String preName;// 文件名
		String suffix;// 扩展名
		int index = name.lastIndexOf(".");
		// 下面文件名拆分一下。
		if (index > 0) {
			preName = name.substring(0, index);
			suffix = name.substring(index);// 含.号
		} else {
			preName = name;
			suffix = "";
		}
		return new String[] { preName, suffix };
	}

	/**
	 * 获取文件的扩展名
	 * 
	 * @param name
	 * @return
	 * @author zhangyz created on 2012-4-27
	 */
	public static String getSuffixName(String name) {
		if (name == null)
			return "";
		String suffix;// 扩展名
		int index = name.lastIndexOf(".");
		// 下面文件名拆分一下。
		if (index > 0 && index < name.length() - 1) {
			suffix = name.substring(index + 1);
		} else {
			suffix = "";
		}
		return suffix;
	}

	/**
	 * 获取文件名的名子部分，不包含扩展名
	 * 
	 * @param fullName
	 * @return
	 * @author zhangyz created on 2012-5-22
	 */
	public static String getOnlyFileName(String fullName) {
		String fn;// 扩展名
		int index = fullName.lastIndexOf(".");
		// 下面文件名拆分一下。
		if (index > 0) {
			fn = fullName.substring(0, index);
		} else {
			fn = fullName;
		}
		return fn;
	}

	/**
	 * 把源文件拷贝到目标目录下，采用同名
	 * 
	 * @param sourceFileName
	 *            源文件
	 * @param destPath
	 *            目标目录
	 * @author qiuxs created on 2012-7-5
	 */
	public static void copyFile(String sourceFileName, String destPath) {
		try {
			File source = new File(sourceFileName);
			File dest = new File((new StringBuilder(String.valueOf(destPath))).append(source.getName()).toString());
			if (!dest.exists())
				dest.createNewFile();
			FileInputStream in = new FileInputStream(source);
			FileOutputStream out = new FileOutputStream(dest);
			IOUtils.copy(in, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void copyFile(File source, File dest) {
		try {
			if (!dest.exists())
				dest.createNewFile();
			FileInputStream in = new FileInputStream(source);
			FileOutputStream out = new FileOutputStream(dest);
			IOUtils.copy(in, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 把文件名分离出路径和文件名两部分。
	 * 
	 * @param fileFullName
	 *            可能是 aa/bce.jpg 也可能是youpai://abc.jpg; 对于后者而言其实没有路径。
	 * @return
	 * @author zhangyz created on 2015-7-1
	 */
	public static String[] extractPathAndFile(String fileFullName) {
		int index = fileFullName.lastIndexOf("/");
		String path = null;
		String fileName = null;
		if (index >= 0) {
			int schemaIndex = fileFullName.indexOf(FILE_SCHEMA_SUFFIX);
			if (schemaIndex >= 0)
				schemaIndex += 2;
			if (schemaIndex == index) {
				// 前面找到的/不不是路径分隔符，而是schema分隔符,不算数。
				fileName = fileFullName;
			} else {
				if (index == fileFullName.length() - 1)
					path = fileFullName;
				else {
					path = fileFullName.substring(0, index + 1);
					fileName = fileFullName.substring(index + 1);
				}
			}
		} else {
			fileName = fileFullName;
		}
		return new String[] { path, fileName };
	}

	public static String MIX_SUFFIX = ".mix";// 混合字段产生的文件扩展名

	/**
	 * 存储的内容是否为文件名
	 * 
	 * @param filePathName
	 * @return
	 * @author zhangyz created on 2012-8-16
	 */
	public static boolean isMixFileName(String filePathName) {
		if (StringUtils.isNotBlank(filePathName) && filePathName.endsWith(MIX_SUFFIX)) {
			if (filePathName.length() == 40)
				return true;
			else {
				int index = filePathName.lastIndexOf("/");
				if (index > 0) {
					filePathName = filePathName.substring(index + 1);
					if (filePathName.length() == 40)
						return true;
				}
				// 还不行说明是新的数字命名的文件名
				String onlyFileName = FileUtils.getOnlyFileName(filePathName);// 如最新的试题回答表的id是long型
				if (StringUtils.isNumeric(onlyFileName))
					return true;
			}
			return false;
		} else
			return false;
	}

	/**
	 * 保存文件，将一个字节流保存到一个文件中
	 * 
	 * @param fullFileName
	 *            保存文件的文件名，是一个完整路径
	 * @param out
	 *            一个输出字节流，即上传的文件流
	 * @throws Exception
	 */
	public static void saveFile(String fullFileName, ByteArrayOutputStream out) throws Exception {
		BufferedOutputStream bout = null;
		try {// 创建一个文件输出流
			bout = new BufferedOutputStream(new FileOutputStream(new File(fullFileName)));
			bout.write(out.toByteArray());
			bout.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("保存文件时出错！", e);
		} finally {
			if (bout != null) {
				try {
					bout.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 根据输入流写入文件
	 *  
	 * @author laisf  
	 * @param absFilePath 写入文件的绝对路径
	 * @param in 输入流
	 */
	public static void createFile(String absFilePath, InputStream in) {
		File destFile = new File(absFilePath);
		if (destFile.exists()) {
			destFile.delete();
		}
		mkDirsForFilePath(absFilePath);
		FileOutputStream destFile2 = null;
		try {
			destFile2 = new FileOutputStream(destFile);
			int b = 0;
			byte[] ch = new byte[1024];
			while ((b = in.read(ch)) != -1) {
				destFile2.write(ch, 0, b);
			}
		} catch (Exception e) {
			log.error("ex=" + e.getMessage(), e);
		} finally {
			try {
				// in.close();
				destFile2.close();
			} catch (Exception e2) {
				;
			}
		}
	}

	/**
	 * 为文件绝对路径创建目录
	 *  
	 * @author laisf
	 * @param absFilePath
	 */
	public static void mkDirsForFilePath(String absFilePath) {
		absFilePath = absFilePath.replaceAll("\\\\", "/");
		String forder = absFilePath.substring(0, absFilePath.lastIndexOf("/"));
		File forderFile = new File(forder);
		if (!forderFile.exists())
			forderFile.mkdirs();
	}

	/**
	 * 获取系统的临时目录
	 * 
	 * 2019年6月15日 下午9:50:13
	 * @auther qiuxs
	 * @return
	 */
	public static String getSystemTempPath() {
		return System.getProperty("java.io.tmpdir");
	}

	/**
	 * 创建一个临时文件对象供写入.File并未真正创建。
	 * @param suffixName
	 * @return
	 */
	public static File createTmpFile(String suffixName) {
		String tempPath = getSystemTempPath();
		File dir = new File(tempPath);
		if (!dir.exists())
			dir.mkdirs();
		String fileName = tempPath + UUIDUtils.getSimpleUuid32();
		if (suffixName != null && suffixName.length() > 0)
			fileName += ("." + suffixName);
		File file = new File(fileName);
		return file;
	}

	/**
	 * 文件是否存在
	 *  
	 * @author fengdg  
	 * @param absPath
	 * @return
	 */
	public static boolean fileExist(String absPath) {
		File file = new File(absPath);
		return fileExist(file);
	}

	/**
	 * 文件是否存在
	 *  
	 * @author fengdg  
	 * @param absPath
	 * @return
	 */
	public static boolean fileExist(File file) {
		return file != null && file.isFile() && file.exists();
	}

	/**
	 * 往文件加一行
	 *  
	 * @author fengdg  
	 * @param f
	 * @param line
	 */
	public static void appendLine(String f, String line) {
		FileWriter writer;
		try {
			writer = new FileWriter(f, true);
			writer.append(line + LINE_SEPARATOR);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			log.error("ext = " + e.getLocalizedMessage(), e);
		}

	}

	/**
	 * 替换扩展名
	 *  
	 * @author fengdg  
	 * @param fileName
	 * @param fileExt
	 * @return
	 */
	public static String replaceFileExt(String fileName, String fileExt) {
		String fileNameWithoutExt = FilenameUtils.removeExtension(fileName);
		return fileNameWithoutExt + SymbolConstants.SEPARATOR_DOT + fileExt;
	}

}
