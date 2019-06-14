package com.hzecool.frm.monitor.tomcat;

import com.hzecool.fdn.utils.FileUtils;
import com.hzecool.fdn.utils.FilepathUtil;
import com.hzecool.fdn.utils.date.DateFormatUtils;
import com.hzecool.frm.monitor.tomcat.controller.TomcatInfoController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.config.Configuration;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * 功能描述: dump当前线程列表<br>
 * 新增日期: 2018/9/21<br>
 *
 * @author laisf
 * @version 1.0.0
 */
public class TomcatThreadListDumpUtil {

    private static long lastDumpTimeStamp = 0;
    /***
     *  dump当前线程列表
     */
    public static void dump(){
        Date now = new Date();
        //30秒内只dump一次，避免产生太多的dump文件
        if(lastDumpTimeStamp > 0 && now.getTime() - lastDumpTimeStamp < 30 * 1000){
            return;
        }
        lastDumpTimeStamp = now.getTime();
        TomcatInfoController controller = new TomcatInfoController();
        String content = controller.threadListTable();
        String filePath = FilepathUtil.getRootRealPath() + "/../logs/";
        String fileName = "threadList_" +
                DateFormatUtils.format(now, DateFormatUtils.yyyyMMddHHmmssSSS_PATTERN) + ".html";
        filePath += fileName;
        ByteArrayInputStream stream = new ByteArrayInputStream(content.getBytes());
        FileUtils.createFile(filePath,  stream);
    }
}
