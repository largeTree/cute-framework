/*
 * Created on 2005-5-11
 * 
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.hzecool.fdn.utils.net;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.SocketException;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.hzecool.fdn.Constant.ExportTips;
import com.hzecool.fdn.FdnCommonLogger;
import com.hzecool.fdn.utils.IOUtils;
import com.hzecool.fdn.utils.io.ZipUtil;

/**
 * 
 * 功能描述: 响应工具类 
 * <p>新增原因: TODO
 *  
 * @author fengdg   
 * @version 1.0.0
 * @since 2017年3月18日 上午11:29:38
 */
public class ResponseUtils {
    private static Logger logger = Logger.getLogger(ResponseUtils.class);
    
    public static void main(String[] args){
        try {
            String strUri = "jar:file:/D:/tomcat-6.0.18/webapps/glpServer/WEB-INF/lib/glpCommon.jar!/struts/shk.glp.common.web.xml";
            java.net.URI uri = new java.net.URI(strUri);
            System.out.println(uri.toASCIIString());
            File entry = new File(uri);
            System.out.println(entry.getAbsolutePath());
            //System.out.println(doGet("http://192.168.0.119:9080/glp/res/std/queryChapter.action?loginName=sys&pwd=123456&clientsession=&needXml=true&kind=code&includeSubs=False&gradeNo=7&subjectNo=1&volumeNo=0001&pressNo=1&versionNo=第一版"));
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

	/**
	 * 针对提供文件下载的servlet，准备好response头信息，并返回可以写入的输出流
	 * @param response
	 * @param downLoadName
	 * @param clientType: 0:inline; 1:attachment
	 * @return
	 * @throws Exception
	 */
	public static java.io.OutputStream prepareResponseOfFileStream(HttpServletResponse response, String downLoadName, long size, int clientType) throws Exception{
		return prepareResponseOfFileStream(response, downLoadName, null, size, clientType);
	}
	
	public static int DOWNTYPE_ONLINE = 0;//在线播放
	public static int DOWNTYPE_OPEN = 1;//打开
	public static java.io.OutputStream prepareResponseOfFileStream(HttpServletResponse response, String downLoadName, String mime, long size, int clientType) throws Exception{
        String strContent = mime;
        if (strContent == null)
            strContent = "application/octet-stream";
        response.setContentType(strContent);
        response.setHeader("Content-Type", strContent);
        String fileNameOut = new String(downLoadName.getBytes("GBK"), "iso-8859-1");
        if (size < 0)
            clientType = 0;//强制等于0
        
        if (clientType == DOWNTYPE_ONLINE)
            response.setHeader("Content-Disposition", "inline;filename=" + fileNameOut);//在线播放
        else
            response.setHeader("Content-Disposition", "attachment;filename=" + fileNameOut);// 此时会提醒下载还是打开
        response.setHeader("Content-Length", Long.toString(size));
        ServletOutputStream outStream = response.getOutputStream();
        return outStream;
    }
	
	/**
	 * 将文件流提供http 下载，下载文件名可以指定
	 * @param response http响应，提供下载通道
	 * @param downLoadName 客户端下载时得到的文件名
	 * @param pic 下载的文件字节数组
	 * @throws Exception
	 */
	public static void doResponseOfFileStream(HttpServletResponse response, String downLoadName, byte[] pic, int clientType){		
		java.io.OutputStream outStream = null;
		try{
			outStream = prepareResponseOfFileStream(response, downLoadName, pic.length, clientType);
			if (pic != null){
				java.io.ByteArrayInputStream input = new java.io.ByteArrayInputStream(pic);
				IOUtils.copyLarge(input, outStream);
			    //IOUtils.copy(input, outStream);
			    input.close();
			}
			outStream.flush();
		}
        catch (Exception ex){
            throw new RuntimeException("输出字节流失败:" + ex.getMessage(), ex);
        }
		finally{
			if (outStream != null){
                try {
                    outStream.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
			}
		}
	}
	
	/**
     * 将文件流提供http 下载，下载文件名可以指定
     * @param response http响应，提供下载通道
     * @param downLoadName 客户端下载时得到的文件名
     * @param input 下载的文件字节流
     * @throws Exception
     */
	public static void doResponseOfFileStream(HttpServletResponse response, String downLoadName, InputStream input, long size, int clientType){     
	    doResponseOfFileStream(response, downLoadName, input, null, size, clientType);
    }
	
	
	public static void doResponseOfFileStream(HttpServletResponse response, String downLoadName, InputStream input, String mime, long size, int clientType){     
        OutputStream outStream = null;
        try{
            outStream = prepareResponseOfFileStream(response, downLoadName, mime, size, clientType);
            /*byte[] bytes = new byte[1024*1024];
            do{
                int length = input.read(bytes);
                if (length < 0)
                    break;
                outStream.write(bytes, 0, length);
            }
            while (true);*/
            if (input != null) {
                IOUtils.copyLarge(input, outStream);
            }
            outStream.flush();
        }
        catch (Exception ex){
            Throwable th = ex.getCause();
            if (th instanceof SocketException){
                if (logger.isInfoEnabled())
                    logger.info("下载" + downLoadName + "因网络故障或其他原因被中断");
            }
            else
                logger.info("下载" + downLoadName + "被中断:" + ex.getMessage());//throw new RuntimeException("输出字节流失败:" + ex.getMessage(), ex);
        }
        finally{
            try{
                if (input != null)
                    input.close();
                if (outStream != null)
                    outStream.close();
            }
            catch (Exception ex){
                ;
            }
        }
    }
	
	
	public static void writeReturnMsg(HttpServletResponse rep, String content) throws IOException {
		rep.setCharacterEncoding("UTF-8");
		rep.setContentType("text/html;charset=UTF-8");
		rep.getWriter().write(ExportTips.MESSAGE_CODE.value() + content);
	}

	public static void writeReturnErr(HttpServletResponse rep, String content) throws IOException {
		rep.setCharacterEncoding("UTF-8");
		rep.setContentType("text/html;charset=UTF-8");
		rep.getWriter().write(ExportTips.ERROR_CODE.value() + content);
	}

	public static void writeReturnValidErr(HttpServletResponse rep, String content) throws IOException {
		rep.setCharacterEncoding("UTF-8");
		rep.setContentType("text/html;charset=UTF-8");
		rep.getWriter().write(ExportTips.VALID_ERROR_CODE.value() + content);
	}

	public static void writeReturnBusinessErr(HttpServletResponse rep, String content) throws IOException {
		rep.setCharacterEncoding("UTF-8");
		rep.setContentType("text/html;charset=UTF-8");
		rep.getWriter().write(ExportTips.BUSINESS_ERROR_CODE.value() + content);
	}

	public static void writeReturn(HttpServletResponse rep, String content) throws IOException {
		rep.setCharacterEncoding("UTF-8");
		rep.setContentType("text/html;charset=UTF-8");
		rep.getWriter().write(content);
	}
	/**
	 * 向response中写文本内容，如果指定了compresstype 按指定的格式压缩
	 * @param text 文本内容
	 * @param response
	 * @param compresstype 压缩方式:zip/gzip
	 * @see ControllerUtil.writeStringResult()
	 */
	public static  void writeReturnJson(ServletResponse response, String text, String compresstype){
		PrintWriter writer = null;
		try{
			FdnCommonLogger.log.debug("result:" + text);
			response.setContentType("application/json; charset=UTF-8");
			if("gzip".equals(compresstype)){
				text = ZipUtil.gzipString(text);
			}else if("zip".equals(compresstype)){
				text = ZipUtil.zipString(text);
			}
			writer = response.getWriter();
			writer.write(text);
			writer.flush();
		} catch (Exception e) {
			FdnCommonLogger.log.error("error:", e);
		}finally{
			if(writer != null)
			    writer.close();
		}
	}

}
