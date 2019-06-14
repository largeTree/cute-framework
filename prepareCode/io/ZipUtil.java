package com.hzecool.fdn.utils.io;

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
import org.apache.log4j.Logger;

import com.hzecool.fdn.Constant;
import com.hzecool.fdn.FdnCommonLogger;

/**
 * 压缩解压工具类
 * 
 * @author lsf
 *
 */
public class ZipUtil {

	private static final Logger log = FdnCommonLogger.log;
	
	/**
	 * 解压gzip压缩的文本
	 * 
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
			if (out != null)
				out.close();
			if (gis != null)
				gis.close();
		}
	}

	/**
	 * 以gzip方式压缩文本
	 * 
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
	 * @param str 压缩前的文本
	 * @param entryName zip entry名称
	 * @return 返回压缩后的字节数组
	 */
	public static byte[] zipString(String str, String entryName) {
		if (str == null || str.length() == 0) {
			return null;
		}
		byte[] compressed;
		ByteArrayOutputStream out = null;
		ZipOutputStream zout = null;
		try {
			out = new ByteArrayOutputStream();
			zout = new ZipOutputStream(out);
			zout.putNextEntry(new ZipEntry(entryName));
			zout.write(str.getBytes(Constant.UTF8));
			zout.closeEntry();
//			compressed = out.toByteArray();
		} catch (IOException e) {
			compressed = null;
		} finally {
			if (zout != null) {
				try {
					zout.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		if(out != null) {
			compressed = out.toByteArray();
		} else {
			compressed = null;
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
//		if (str == null || str.length() == 0) {
//			return null;
//		}
//		byte[] compressed;
//		ByteArrayOutputStream out = null;
//		ZipOutputStream zout = null;
//		String compressedStr = null;
//		try {
//			out = new ByteArrayOutputStream();
//			zout = new ZipOutputStream(out);
//			zout.putNextEntry(new ZipEntry("0"));
//			zout.write(str.getBytes());
//			zout.closeEntry();
//			compressed = out.toByteArray();
//			compressedStr = Base64.encodeBase64String(compressed);
//		} catch (IOException e) {
//			compressed = null;
//		} finally {
//			if (zout != null) {
//				try {
//					zout.close();
//				} catch (IOException e) {
//				}
//			}
//			if (out != null) {
//				try {
//					out.close();
//				} catch (IOException e) {
//				}
//			}
//		}
//		return compressedStr;
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
			decompressed = null;
		} finally {
			if (zin != null) {
				try {
					zin.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
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
			e.printStackTrace();
			log.error("创建ZIP文件失败", e);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("创建ZIP文件失败", e);
		} finally {
			try {
				if (zos != null) {
					zos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				log.error("创建ZIP文件失败", e);
			}
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
					try {
						if (dis != null) {
							dis.close();
						}
					} catch (IOException e) {
						throw e;
					}
				}
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
//		String str = "{details_mat.propdres.colorid_2=2, details_mat.propdres.colorid_1=2, details_mat.propdres.colorid_4=2, details_mat.propdres.colorid_3=2, details_mat.propdres.colorid_0=2, totalsum=0, details_total_9=0.00, details_total_8=0.00, details_mat.propdres.colorid_9=3, details_mat.propdres.colorid_5=3, details_mat.propdres.colorid_6=3, details_mat.propdres.colorid_7=3, details_mat.propdres.colorid_8=3, details_rowid_6=1, details_rowid_5=1, details_rowid_4=0, details_rowid_3=0, details_rowid_2=0, details_rowid_1=0, details_rowid_0=0, details_rowid_9=1, details_rowid_7=1, details_rowid_8=1, formid=saleorder_dres, sellerid=146833, details_rowid_30=6, details_rowid_31=6, details_rowid_32=6, details_rowid_33=6, details_rowid_34=6, details_rowid_35=7, details_rowid_36=7, details_rowid_37=7, details_rowid_38=7, details_rowid_39=7, details_discount_0=1, details_rowid_40=8, details_rowid_43=8, details_rowid_44=8, details_rowid_41=8, details_discount_9=1, details_rowid_42=8, details_rowid_47=9, details_discount_7=1, details_rowid_48=9, details_discount_8=1, details_discount_5=1, details_rowid_45=9, details_rowid_46=9, details_discount_6=1, details_discount_3=1, details_discount_4=1, dwid=11347381, details_rowid_49=9, details_discount_1=1, details_discount_2=1, details_rowid_10=2, details_rowid_11=2, details_stdpricetype_57=1, details_rowid_16=3, details_rowid_17=3, details_stdpricetype_58=1, details_rowid_18=3, details_stdpricetype_59=1, details_rowid_19=3, details_rowid_12=2, details_stdpricetype_53=1, details_stdpricetype_54=1, details_rowid_13=2, details_rowid_14=2, details_stdpricetype_55=1, details_rowid_15=3, details_stdpricetype_56=1, details_rowid_21=4, details_rowid_22=4, details_rowid_20=4, invalidflag=0, details_rowid_29=5, details_rem_54=, details_rem_53=, details_rowid_27=5, details_rem_56=, details_rem_55=, details_rowid_28=5, details_rowid_25=5, details_rem_58=, details_rem_57=, details_rowid_26=5, details_rowid_23=4, details_rem_59=, details_rowid_24=4, details_rem_46=, details_rem_47=, details_rem_48=, details_rem_49=, details_num_30=30.00, details_num_31=35.00, details_rem_42=, details_rem_43=, details_num_32=35.00, details_num_33=30.00, details_rem_44=, details_rem_45=, details_num_34=20.00, details_num_35=30.00, details_num_36=35.00, details_num_37=35.00, details_num_38=30.00, details_num_39=20.00, details_mat.propdres.styleid_10=23148005, details_mat.propdres.styleid_11=23148005, deliver=146833, details_mat.propdres.styleid_18=23148005, details_rem_50=, details_mat.propdres.styleid_19=23148005, details_rem_52=, details_mat.propdres.styleid_16=23148005, details_rem_51=, details_mat.propdres.styleid_17=23148005, details_mat.propdres.styleid_14=23148005, details_mat.propdres.styleid_15=23148005, details_mat.propdres.styleid_12=23148005, details_mat.propdres.styleid_13=23148005, details_rem_37=, details_price_18=0, details_num_40=18.00, details_price_19=0, details_num_41=21.00, details_rem_38=, details_rem_35=, details_price_16=0, details_price_17=0, details_rem_36=, details_rem_33=, details_num_44=12.00, details_num_45=18.00, details_rem_34=, details_rem_31=, details_num_42=21.00, details_num_43=18.00, details_rem_32=, details_price_10=0, details_num_48=18.00, details_num_49=12.00, details_price_11=0, details_num_46=21.00, details_num_47=21.00, details_price_14=0, details_price_15=0, finpay.agency=, details_rem_39=, details_price_12=0, details_price_13=0, finpay.cash=0, details_mat.propdres.styleid_20=23147973, details_mat.propdres.styleid_21=23147973, details_mat.propdres.styleid_22=23147973, details_rem_41=, details_mat.propdres.styleid_27=23147973, details_mat.propdres.styleid_28=23147973, details_rem_40=, details_mat.propdres.styleid_29=23147973, details_mat.propdres.styleid_23=23147973, details_mat.propdres.styleid_24=23147973, details_mat.propdres.styleid_25=23147973, pk=1437813, details_mat.propdres.styleid_26=23147973, finpay.verifybillids=, details_rem_20=, details_rem_21=, details_num_10=25.00, details_num_11=35.00, details_rem_22=, details_rem_23=, details_num_12=35.00, details_price_27=0, details_rem_24=, details_rem_25=, details_price_28=0, details_price_29=0, details_rem_26=, details_rem_27=, details_price_23=0, details_num_17=35.00, details_rem_28=, details_num_18=30.00, details_price_24=0, details_rem_29=, details_price_25=0, details_num_19=25.00, details_price_26=0, details_num_13=30.00, details_price_20=0, details_num_14=25.00, details_num_15=25.00, details_price_21=0, details_num_16=35.00, details_price_22=0, finpay.card=0, details_rem_30=, details_rem_11=, details_num_22=35.00, details_num_23=30.00, details_rem_12=, details_num_20=25.00, details_num_21=35.00, details_rem_10=, details_rem_15=, details_rem_16=, details_rem_13=, details_price_38=0, details_price_39=0, details_rem_14=, details_price_36=0, details_rem_19=, details_price_37=0, details_price_34=0, details_rem_17=, details_num_28=30.00, details_price_35=0, details_rem_18=, details_num_29=25.00, details_num_26=35.00, details_price_32=0, details_price_33=0, details_num_27=35.00, details_price_30=0, details_num_24=25.00, details_num_25=25.00, details_price_31=0, details_price_42=0, details_price_41=0, details_price_44=0, details_price_43=0, details_price_46=0, details_price_45=0, details_price_48=0, details_price_47=0, details_price_49=0, details_price_40=0, details_price_55=0, details_price_54=0, details_price_53=0, details_price_52=0, details_price_59=0, details_price_58=0, details_price_57=0, details_price_56=0, details_price_50=0, details_price_51=0, finpay.cashaccountid=0, details_num_58=18.00, details_num_57=21.00, details_num_59=12.00, details_num_54=12.00, details_num_53=18.00, details_num_56=21.00, details_num_55=18.00, details_num_50=18.00, details_num_52=21.00, details_num_51=21.00, actid=39, details_total_1=0.00, details_total_0=0.00, details_total_3=0.00, details_total_2=0.00, details_total_5=0.00, details_total_4=0.00, details_total_7=0.00, details_total_6=0.00, clientid=11347381, details_mat.propdres.colorid_25=52, details_mat.propdres.colorid_26=52, details_mat.propdres.colorid_27=52, details_mat.propdres.colorid_28=52, details_mat.propdres.colorid_29=52, details_mat.propdres.sizeid_11=17, finpay.remitaccountid=0, details_mat.propdres.sizeid_10=29, details_mat.propdres.colorid_20=3, details_mat.propdres.colorid_21=3, details_mat.propdres.colorid_22=3, details_mat.propdres.colorid_23=3, details_mat.propdres.colorid_24=3, details_rem_9=, details_rem_8=, details_rem_7=, details_rem_6=, details_rem_5=, details_rem_4=, details_rem_3=, details_rem_2=, prodate=2015-12-28, details_mat.propdres.colorid_38=52, details_mat.propdres.colorid_39=52, details_mat.propdres.colorid_36=52, details_mat.propdres.colorid_37=52, details_rem_0=, details_mat.propdres.colorid_30=3, clz=OrderDresSvc, details_mat.propdres.colorid_31=3, details_rem_1=, details_mat.propdres.colorid_34=3, details_mat.propdres.colorid_35=52, details_mat.propdres.colorid_32=3, details_mat.propdres.colorid_33=3, optime=2015-12-28, details_mat.propdres.sizeid_25=29, details_mat.propdres.sizeid_26=17, details_mat.propdres.sizeid_23=19, details_mat.propdres.sizeid_24=20, details_mat.propdres.sizeid_29=20, details_mat.propdres.sizeid_27=18, slh_version=6.6101, details_mat.propdres.sizeid_28=19, details_mat.propdres.colorid_47=52, details_mat.propdres.colorid_48=52, details_mat.propdres.colorid_49=52, details_mat.propdres.colorid_43=51, details_mat.propdres.colorid_44=51, details_mat.propdres.colorid_45=52, details_mat.propdres.colorid_46=52, details_mat.propdres.sizeid_33=19, details_mat.propdres.colorid_40=51, details_mat.propdres.sizeid_32=18, details_mat.propdres.sizeid_31=17, details_mat.propdres.colorid_41=51, details_mat.propdres.sizeid_30=29, details_mat.propdres.colorid_42=51, details_mat.propdres.sizeid_6=17, details_mat.propdres.sizeid_12=18, details_mat.propdres.sizeid_13=19, details_mat.propdres.sizeid_5=29, details_mat.propdres.sizeid_4=20, details_mat.propdres.sizeid_14=20, details_mat.propdres.sizeid_3=19, details_mat.propdres.sizeid_15=29, details_mat.propdres.sizeid_16=17, details_mat.propdres.sizeid_2=18, details_mat.propdres.sizeid_1=17, details_mat.propdres.sizeid_17=18, details_mat.propdres.sizeid_18=19, details_mat.propdres.sizeid_0=29, details_mat.propdres.sizeid_19=20, totalnum=1560, finpay.card3=, details_mat.propdres.sizeid_9=20, details_mat.propdres.sizeid_8=19, finpay.card2=, details_mat.propdres.sizeid_7=18, invid=37841, details_mat.propdres.colorid_58=59, details_mat.propdres.colorid_59=59, details_mat.propdres.colorid_56=59, details_mat.propdres.colorid_57=59, details_mat.propdres.colorid_54=3, details_mat.propdres.colorid_55=59, details_mat.propdres.sizeid_20=29, details_mat.propdres.colorid_52=3, details_mat.propdres.colorid_53=3, details_mat.propdres.sizeid_22=18, details_mat.propdres.colorid_50=3, details_mat.propdres.colorid_51=3, details_mat.propdres.sizeid_21=17, details_pk_15=7168261, details_pk_14=7168257, details_pk_13=7168253, details_pk_12=7168249, details_pk_19=7168277, details_pk_18=7168273, details_pk_17=7168269, finpay.cardaccountid=0, details_pk_16=7168265, details_pk_11=7168245, details_pk_10=7168241, details_maxidx=60, details_mat.propdres.styleid_57=23177673, details_mat.propdres.styleid_56=23177673, details_mat.propdres.styleid_59=23177673, details_mat.propdres.styleid_58=23177673, finpay.verifysum=0, details_mat.propdres.styleid_51=23177673, details_mat.propdres.styleid_50=23177673, details_mat.propdres.styleid_53=23177673, details_mat.propdres.styleid_52=23177673, details_mat.propdres.styleid_55=23177673, details_mat.propdres.styleid_54=23177673, finpay.remit=0, details_pk_39=7168357, details_pk_38=7168353, details_mat.propdres.styleid_49=23147989, details_mat.propdres.styleid_48=23147989, details_pk_37=7168349, details_pk_36=7168345, details_mat.propdres.styleid_47=23147989, details_pk_35=7168341, details_mat.propdres.styleid_46=23147989, details_mat.propdres.styleid_45=23147989, details_pk_34=7168337, details_mat.propdres.styleid_44=23147989, details_pk_3=7168213, details_pk_33=7168333, details_pk_4=7168217, details_mat.propdres.styleid_43=23147989, details_pk_32=7168329, details_pk_31=7168325, details_mat.propdres.styleid_42=23147989, details_pk_5=7168221, details_pk_30=7168321, details_mat.propdres.styleid_41=23147989, details_pk_6=7168225, details_mat.propdres.styleid_40=23147989, details_pk_0=7168201, details_pk_1=7168205, details_pk_2=7168209, details_pk_8=7168233, details_pk_7=7168229, details_pk_9=7168237, details_mat.propdres.styleid_39=23147961, details_pk_28=7168313, svc=invokeMethod, details_pk_27=7168309, details_mat.propdres.styleid_38=23147961, details_pk_29=7168317, details_mat.propdres.styleid_35=23147961, details_pk_24=7168297, details_mat.propdres.styleid_34=23147961, details_pk_23=7168293, details_pk_26=7168305, details_mat.propdres.styleid_37=23147961, details_pk_25=7168301, details_mat.propdres.styleid_36=23147961, details_mat.propdres.styleid_31=23147961, details_pk_20=7168281, details_mat.propdres.styleid_30=23147961, details_mat.propdres.styleid_33=23147961, details_pk_22=7168289, details_pk_21=7168285, details_mat.propdres.styleid_32=23147961, details_mat.propdres.colorid_13=51, details_mat.propdres.colorid_12=51, details_mat.propdres.colorid_11=51, details_mat.propdres.colorid_10=51, details_mat.propdres.colorid_19=52, details_mat.propdres.colorid_18=52, details_mat.propdres.colorid_17=52, details_mat.propdres.colorid_16=52, details_mat.propdres.colorid_15=52, details_mat.propdres.colorid_14=51, details_stdpricetype_47=1, details_stdpricetype_2=1, details_discount_54=1, details_stdpricetype_3=1, details_stdpricetype_46=1, details_discount_55=1, details_discount_56=1, details_stdpricetype_4=1, details_stdpricetype_49=1, details_stdpricetype_5=1, details_stdpricetype_48=1, details_discount_57=1, details_stdpricetype_6=1, details_discount_58=1, details_stdpricetype_43=1, details_stdpricetype_42=1, details_discount_59=1, details_stdpricetype_7=1, details_stdpricetype_8=1, details_stdpricetype_45=1, details_stdpricetype_9=1, details_stdpricetype_44=1, details_total_30=0.00, inoutflag=2, details_total_32=0.00, details_pk_50=7168401, details_total_31=0.00, details_pk_51=7168405, details_pk_52=7168409, details_pk_53=7168413, details_pk_54=7168417, details_pk_55=7168421, details_pk_56=7168425, details_total_38=0.00, details_stdpricetype_50=1, details_pk_57=7168429, details_total_37=0.00, details_stdpricetype_51=1, details_pk_58=7168433, details_stdpricetype_52=1, details_total_39=0.00, details_pk_59=7168437, details_total_34=0.00, details_total_33=0.00, details_stdpricetype_1=1, details_total_36=0.00, details_total_35=0.00, details_stdpricetype_0=1, details_stdpricetype_38=1, details_rowid_51=10, details_discount_45=1, hashkey=CC:C7:60:6A:B0:16-2015-12-28 16:57:14:171, details_discount_46=1, details_rowid_50=10, details_stdpricetype_37=1, details_discount_43=1, details_stdpricetype_36=1, details_stdpricetype_35=1, details_discount_44=1, details_stdpricetype_34=1, details_rowid_55=11, details_discount_49=1, finpay.agencyaccountid=0, details_stdpricetype_33=1, details_rowid_54=10, details_discount_47=1, details_rowid_53=10, details_stdpricetype_32=1, details_discount_48=1, details_rowid_52=10, details_stdpricetype_31=1, details_rowid_59=11, details_rowid_58=11, details_rowid_57=11, details_rowid_56=11, details_stdpricetype_39=1, details_total_43=0.00, details_pk_40=7168361, details_total_42=0.00, details_total_41=0.00, details_total_40=0.00, details_pk_43=7168373, details_pk_44=7168377, details_pk_41=7168365, details_pk_42=7168369, details_stdpricetype_40=1, details_pk_47=7168389, details_pk_48=7168393, details_stdpricetype_41=1, details_pk_45=7168381, details_total_49=0.00, details_pk_46=7168385, details_total_48=0.00, details_discount_53=1, details_total_47=0.00, details_discount_52=1, details_total_46=0.00, details_pk_49=7168397, details_discount_51=1, details_total_45=0.00, details_discount_50=1, details_total_44=0.00, details_stdpricetype_21=1, details_mat.propdres.styleid_6=23148321, details_discount_36=1, details_stdpricetype_20=1, details_mat.propdres.styleid_5=23148321, details_discount_37=1, details_stdpricetype_23=1, details_mat.propdres.styleid_4=23148321, details_discount_38=1, details_mat.propdres.styleid_3=23148321, details_stdpricetype_22=1, details_discount_39=1, details_stdpricetype_25=1, details_mat.propdres.styleid_2=23148321, details_discount_32=1, details_discount_33=1, details_mat.propdres.styleid_1=23148321, details_stdpricetype_24=1, details_discount_34=1, details_stdpricetype_27=1, details_mat.propdres.styleid_0=23148321, details_stdpricetype_26=1, details_discount_35=1, details_stdpricetype_29=1, details_stdpricetype_28=1, version=1, epid=27433, details_total_50=0.00, details_total_52=0.00, details_total_51=0.00, details_total_54=0.00, details_total_53=0.00, details_num_2=35.00, details_discount_40=1, details_total_56=0.00, details_total_55=0.00, details_num_3=30.00, details_total_58=0.00, details_num_4=25.00, details_discount_42=1, details_total_57=0.00, details_discount_41=1, details_num_5=25.00, details_total_59=0.00, details_num_0=25.00, details_stdpricetype_30=1, details_num_1=35.00, details_discount_27=1, details_stdpricetype_12=1, details_discount_28=1, details_stdpricetype_11=1, details_stdpricetype_10=1, details_discount_25=1, details_discount_26=1, details_stdpricetype_16=1, details_discount_23=1, details_discount_24=1, details_stdpricetype_15=1, details_discount_21=1, details_stdpricetype_14=1, details_stdpricetype_13=1, details_discount_22=1, details_stdpricetype_19=1, details_stdpricetype_18=1, details_stdpricetype_17=1, details_discount_29=1, details_discount_31=1, details_discount_30=1, details_mat.propdres.styleid_7=23148321, details_mat.propdres.styleid_8=23148321, details_mat.propdres.styleid_9=23148321, details_discount_19=1, details_mat.propdres.sizeid_52=18, details_mat.propdres.sizeid_53=19, details_discount_18=1, details_mat.propdres.sizeid_54=20, details_mat.propdres.sizeid_55=29, remark=, details_mat.propdres.sizeid_50=29, details_mat.propdres.sizeid_51=17, details_discount_11=1, details_discount_10=1, details_discount_13=1, details_discount_12=1, details_discount_15=1, details_discount_14=1, details_discount_17=1, details_discount_16=1, details_discount_20=1, details_mat.propdres.sizeid_49=20, details_mat.propdres.sizeid_46=17, details_mat.propdres.sizeid_45=29, details_mat.propdres.sizeid_48=19, details_mat.propdres.sizeid_47=18, details_mat.propdres.sizeid_43=19, details_mat.propdres.sizeid_44=20, details_mat.propdres.sizeid_41=17, details_mat.propdres.sizeid_42=18, details_mat.propdres.sizeid_40=29, clz.mth=edit, details_num_8=30.00, details_num_9=25.00, details_num_6=35.00, details_num_7=35.00, details_price_1=0, details_price_2=0, details_price_0=0, details_mat.propdres.sizeid_39=20, details_mat.propdres.sizeid_38=19, details_mat.propdres.sizeid_37=18, details_mat.propdres.sizeid_36=17, details_mat.propdres.sizeid_35=29, details_mat.propdres.sizeid_34=20, details_price_4=0, details_price_3=0, details_price_6=0, details_price_5=0, details_price_8=0, details_price_7=0, details_price_9=0, details_total_11=0.00, details_total_12=0.00, details_total_13=0.00, details_total_14=0.00, details_total_15=0.00, details_total_16=0.00, details_total_17=0.00, details_total_18=0.00, showrep=1, details_total_10=0.00, finpay.balance=0, opid=146781, details_total_19=0.00, sessionid=CE9E3835-6F9C-1200-5DD2-0F42B9A51C91, details_total_24=0.00, details_total_25=0.00, details_total_22=0.00, details_total_23=0.00, details_total_28=0.00, details_total_29=0.00, details_total_26=0.00, details_total_27=0.00, target=14401, details_mat.propdres.sizeid_59=20, details_mat.propdres.sizeid_58=19, details_mat.propdres.sizeid_57=18, details_mat.propdres.sizeid_56=17, details_total_20=0.00, details_total_21=0.00}";
//		System.out.println(str.length());
//		// String compressStr = zipString(str);
//		String compressStr = gzipString(str);
//		System.out.println(compressStr.length());
//		System.out.println(compressStr);
//		compressStr = "H4sIAAAAAAAAA4VUTW+jMBC991cgzpvKfKQBzr300NMeV6tqak+CFYOR7VDRqv+9jjGJTSL1guQ3zzNv3oz5ekiSVGEH6pgmTZKmf85AC7o94uSQrGzqusG8YVWz2zZ0u8lJ9rQh2YbUSV402a7ZFk1Vk/mqHAzv0N288ubQnvcDTI8UFMuDYh4eUfH99M6F4EwHYQPqgMZLyX0VKj4d8tKPr8D7Z4X670h9Pqk6zlxYg0B5Mm/MxqNitmNugFJ56o3n+swMDXDhBPyz5yT5ct9zhGvHn6U4tsO1YYPiFM004E2Mf6LPn5VXWMmPqKoDO1ikXDEqhVQeza/wQcnT0J+6FVshCCdlLcNIA8KBBQnpdLxNos0kLpoJyfKirAgJxN+tMBxXaZbEccF1sXncK/Bs5AI67Nt+/8/LsHicZ5dFeOxM6zBgLBryOwjovVRyZ9d02PsSggP2dLq7GnJYbKmrovTrEg9ydlrHvafarub5zPtRHvEVTSu9UouANWAv4BBm4b3d2guY3zyfQPagJAPz64O725ATuxpUCnRhLR5HzgSldSs/FA7BKqS4WFTWZXUjoggu28499Wm3i6nucQZUhoLbia2tvyTWbdRAZ38I0VtNH75/AK+4zVPnBAAA";
//		// String uncompressStr = unzipString(compressStr);
//		String uncompressStr = ungzipString(compressStr);
//		System.out.println(uncompressStr);
//		String str = "H4sIAAAAAAAAAG1Uv4/TMBT+XzKB1EpJ06YpI9cBpGNhYEEIuYmTs3DtKHYbhdP9NTcw3AQIJDhxgkoIAQMndMDCgNgAIbHehv1iO2mVpXrx+/W9932vh16Jl0SiJOErJknqXfO8gX5D5QNnE6lMX9nigFf3E1SmO/HwnlZZah+6tqwLrKxRoOye3DUuSVaL1bLbI63A/fJEPSwQRSzBxk0YX8mMolyXbMILiA1bfOKg2+P5yZtfT35+uBh++vrq35ePp480PkyJamzSNCpTPiOsQPUdwLQglJJUNDANCjOMDqU8J+K6imG8CSlKniIJs/pBNPTHw2Bm4/bVT4ZtqoaozNgfw0RrkpY4s5sD0ONoFsTaW2gaxuF0MmqX48D7vj948f3134vH1gfN5tUWMX0MoxyzpO6j0S306fG7H89+X5FYyKthdL5puBKEM+UMbPgOeiwRoWpldw89ySWibkpBHuICldh0C0BZiBYlAWpHUWyCwK0/lkgmVBDLTMIpL+Froj8bcrqacclnl5s/GgsRMF0XrZA1bYL2boRRPHh7/O3888Ypx7U4u3yvpSdkCgAN67oMA6Fq1eQlXxXM6XZ7kiVXLO1RJLR85vs32XqOJVS0AEbxJIgCcx9uEsVVs0k1vQnzw6jZVrJuu0mU24aQxitXAhQTzPx4Gs28o3t9WG4hwrqd4TZBOrdtf5TIdvU7FzXVuZrdJWe4dhQ3mupeRyvEjJixYRJRGI35tlK71i1iQV3d4+7VrYpCKsf8KzhB2OQeIS/s2QbT0Dv6D7QQmwAFBQAA";
//		String result = ungzipString(str);
//		System.out.println(result);
//		String sourcePath = "c:\\Users\\suoyi\\Desktop\\tabs";
//		String zipPath = "c:\\Users\\suoyi\\Desktop\\tabs.zip";
//		createZip(sourcePath, zipPath);
		byte[] b =  zipString("A123", "1.txt");
		FileOutputStream fos = new FileOutputStream("d:/1.zip");
		fos.write(b);
		fos.close();
	}
	
	
}
