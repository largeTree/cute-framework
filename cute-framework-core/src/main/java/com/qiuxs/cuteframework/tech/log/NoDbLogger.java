package com.qiuxs.cuteframework.tech.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * 功能描述: 不会触发写日志数据库操作的Logger
 * -适用场景：log4j2记录到数据库时抛出异常时，记录异常信息到日志文件，但不再寄到数据库，否则会导致死循环
 *  
 * <p>新增原因: TODO
 *  
 * @author qiuxs   
 * @version 1.0.0
 * @since 2017年6月22日 上午8:55:52
 */
public class NoDbLogger {
	public static final Logger log = LogManager.getLogger("NoDbLogger");
}
