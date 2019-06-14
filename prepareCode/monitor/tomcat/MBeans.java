package com.hzecool.frm.monitor.tomcat;

import java.lang.management.ManagementFactory;  
import java.util.Set;  
  
import javax.management.JMException;  
import javax.management.MBeanServer;  
import javax.management.MalformedObjectNameException;  
import javax.management.ObjectName;  
  
public class MBeans {  
  
    private final MBeanServer mbeanServer;  
  
    MBeans() {  
        this(getPlatformMBeanServer());  
    }  
  
    private MBeans(MBeanServer mbeanServer) {  
        super();  
        this.mbeanServer = mbeanServer;  
    }  
    /** 
     * 获取MBeanServer 
     * @return 
     */  
    static MBeanServer getPlatformMBeanServer() {  
        return ManagementFactory.getPlatformMBeanServer();  
    }  
    /** 
     * 获取tomcat的线程池 
     * @return 
     * @throws MalformedObjectNameException 
     */  
    Set<ObjectName> getTomcatThreadPools() throws MalformedObjectNameException {  
    	//tomcat有默认会启动两个连接池，一个是http 另外一个是ajp，我们只监控http的
        return mbeanServer.queryNames(new ObjectName("*:type=ThreadPool,name=\"http*\""), null);  
    }  
  
    Set<ObjectName> getTomcatGlobalRequestProcessors() throws MalformedObjectNameException {  
        return mbeanServer.queryNames(new ObjectName("*:type=GlobalRequestProcessor,name=\"http*\""), null);  
    }  
  
    Object getAttribute(ObjectName name, String attribute) throws JMException {  
        return mbeanServer.getAttribute(name, attribute);  
    }  
      
}  
