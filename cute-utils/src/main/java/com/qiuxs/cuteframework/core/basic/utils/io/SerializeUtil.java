package com.qiuxs.cuteframework.core.basic.utils.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 序列化与反序列化,如果输入null,输出也为null
 * @author qiuxs
 * 2019年6月15日 下午9:56:48
 */
public class SerializeUtil {

	private static final Logger log = LogManager.getLogger(SerializeUtil.class);

	/**
	 * 序列化。与serialize方法的区别是，该方法序列化失败时，将抛出异常。
	 * @author lsh  
	 * @param object 待序列化对象
	 * @return 序列化后字节数组
	 */
	public static byte[] serial(Object object) {
		byte[] bytes = null;
		if (object != null) {
			ObjectOutputStream oos = null;
			ByteArrayOutputStream baos = null;
			try {
				baos = new ByteArrayOutputStream();
				oos = new ObjectOutputStream(baos);
				oos.writeObject(object);
				bytes = baos.toByteArray();
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				close(baos);
				close(oos);
			}
		}
		return bytes;
	}

	/**
	 * 序列化
	 * 
	 * @param object
	 * @return
	 */
	public static byte[] serialize(Object object) {
		//		byte[] bytes = null;
		//		if (object != null) {
		//			ObjectOutputStream oos = null;
		//			ByteArrayOutputStream baos = null;
		//			try {
		//				baos = new ByteArrayOutputStream();
		//				oos = new ObjectOutputStream(baos);
		//				oos.writeObject(object);
		//				bytes = baos.toByteArray();
		//			} catch (Exception e) {
		//				FdnCommonLogger.log.error("ex=" + e.getMessage(), e);
		//			} finally {
		//				close(baos);
		//				close(oos);
		//			}
		//		}
		//		return bytes;
		try {
			return serial(object);
		} catch (Exception e) {
			log.error("ex=" + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 反序列化。与unserialize方法的区别是，反序列化失败时，将抛出异常。
	 * @author lsh  
	 * @param bytes 对象序列化后的字节数组
	 * @return 反序列化后的对象
	 */
	@SuppressWarnings("unchecked")
	public static <V> V unserial(byte[] bytes) {
		V o = null;
		if (bytes != null) {
			ByteArrayInputStream bais = null;
			ObjectInputStream ois = null;
			try {
				bais = new ByteArrayInputStream(bytes);
				ois = new ObjectInputStream(bais);
				o = (V) ois.readObject();
			} catch (IOException | ClassNotFoundException e) {
				throw new RuntimeException(e);
			} finally {
				close(ois);
				close(bais);
			}
		}
		return o;
	}

	/**
	 * 反序列化
	 * 
	 * @param bytes
	 * @return
	 */
	public static <V> V unserialize(byte[] bytes) {
		try {
			return unserial(bytes);
		} catch (Exception e) {
			log.error("ex=" + e.getMessage(), e);
			return null;
		}
	}

	private static void close(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				//
			}
		}
	}

	private static void close(OutputStream os) {
		if (os != null) {
			try {
				os.close();
			} catch (IOException e) {
				//
			}
		}
	}

	/**
	 * 序列化保存到文件中
	 * 
	 * @param object
	 * @param filename
	 */
	public static void serialize(Object object, String filename) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(filename);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(object);
			oos.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			close(oos);
			close(fos);
		}
	}

	/**
	 * 从文件中反序列化
	 * 
	 * @param bytes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <V> V unserialize(String filename) {
		V o = null;
		if (filename != null) {
			FileInputStream fis = null;
			ObjectInputStream ois = null;
			try {
				fis = new FileInputStream(filename);
				ois = new ObjectInputStream(fis);
				o = (V) ois.readObject();
			} catch (Exception e) {
				log.error("ex=" + e.getMessage(), e);
			} finally {
				close(ois);
				close(fis);
			}
		}
		return o;
	}
}
