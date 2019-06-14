package com.hzecool.frm.monitor.tomcat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.JMException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
  
public final class TomcatInformations implements Serializable {  
  
    private static final boolean TOMCAT_USED = System.getProperty("catalina.home") != null;  
  
    private static final long serialVersionUID = -6145865427461051370L;  
  
    @SuppressWarnings("all")  
    private static final List<ObjectName> THREAD_POOLS = new ArrayList<ObjectName>();  
    @SuppressWarnings("all")  
    private static final List<ObjectName> GLOBAL_REQUEST_PROCESSORS = new ArrayList<ObjectName>();  
  
    private final String name;  
    private final int maxThreads;  
    private final int currentThreadCount;  
    private final int currentThreadsBusy;  
    private final long bytesReceived;  
    private final long bytesSent;  
    private final int requestCount;  
    private final int errorCount;  
    private final long processingTime;  
    private final long maxTime;  
  
    private TomcatInformations(MBeans mBeans, ObjectName threadPool) throws JMException {  
        super();  
        name = threadPool.getKeyProperty("name");  
        maxThreads = (Integer) mBeans.getAttribute(threadPool, "maxThreads");  
        currentThreadCount = (Integer) mBeans.getAttribute(threadPool, "currentThreadCount");  
        currentThreadsBusy = (Integer) mBeans.getAttribute(threadPool, "currentThreadsBusy");  
        ObjectName grp = null;  
        for (final ObjectName globalRequestProcessor : GLOBAL_REQUEST_PROCESSORS) {  
            if (name.equals(globalRequestProcessor.getKeyProperty("name"))) {  
                grp = globalRequestProcessor;  
                break;  
            }  
        }  
        if (grp != null) {  
            bytesReceived = (Long) mBeans.getAttribute(grp, "bytesReceived");  
            bytesSent = (Long) mBeans.getAttribute(grp, "bytesSent");  
            requestCount = (Integer) mBeans.getAttribute(grp, "requestCount");  
            errorCount = (Integer) mBeans.getAttribute(grp, "errorCount");  
            processingTime = (Long) mBeans.getAttribute(grp, "processingTime");  
            maxTime = (Long) mBeans.getAttribute(grp, "maxTime");  
        } else {  
            bytesReceived = 0;  
            bytesSent = 0;  
            requestCount = 0;  
            errorCount = 0;  
            processingTime = 0;  
            maxTime = 0;  
        }  
    }  
  
    public static List<TomcatInformations> buildTomcatInformationsList() {  
        if (!TOMCAT_USED) {  
            return Collections.emptyList();  
        }  
        try {  
        	if (THREAD_POOLS.isEmpty() || GLOBAL_REQUEST_PROCESSORS.isEmpty()) {  
        		synchronized (THREAD_POOLS) {  
        			if (THREAD_POOLS.isEmpty() || GLOBAL_REQUEST_PROCESSORS.isEmpty()) {  
        				initMBeans();  
        			}  
        		}  
        	}
            final MBeans mBeans = new MBeans();  
            final List<TomcatInformations> tomcatInformationsList = new ArrayList<TomcatInformations>(  
                    THREAD_POOLS.size());  
            for (final ObjectName threadPool : THREAD_POOLS) {  
                tomcatInformationsList.add(new TomcatInformations(mBeans, threadPool));  
            }  
            return tomcatInformationsList;  
        } catch (final InstanceNotFoundException e) {  
            return Collections.emptyList();  
        } catch (final AttributeNotFoundException e) {  
            return Collections.emptyList();  
        } catch (final JMException e) {  
            throw new IllegalStateException(e);  
        }  
    }  
  
    private static void initMBeans() throws MalformedObjectNameException {  
        final MBeans mBeans = new MBeans();  
        THREAD_POOLS.clear();  
        GLOBAL_REQUEST_PROCESSORS.clear();  
        THREAD_POOLS.addAll(mBeans.getTomcatThreadPools());  
        GLOBAL_REQUEST_PROCESSORS.addAll(mBeans.getTomcatGlobalRequestProcessors());  
    }  
  
    public String getName() {  
        return name;  
    }  
  
    public int getMaxThreads() {  
        return maxThreads;  
    }  
  
    public int getCurrentThreadCount() {  
        return currentThreadCount;  
    }  
  
    public int getCurrentThreadsBusy() {  
        return currentThreadsBusy;  
    }  
  
    public long getBytesReceived() {  
        return bytesReceived;  
    }  
  
    public long getBytesSent() {  
        return bytesSent;  
    }  
  
    public int getRequestCount() {  
        return requestCount;  
    }  
  
    public int getErrorCount() {  
        return errorCount;  
    }  
  
    public long getProcessingTime() {  
        return processingTime;  
    }  
  
    public long getMaxTime() {  
        return maxTime;  
    }  
  
    /** {@inheritDoc} */  
    @Override  
    public String toString() {  
        return getClass().getSimpleName() + "[端口名name=" + getName() + ", 最大线程数maxThreads="  
                + getMaxThreads() + ",当前线程数 currentThreadCount=" + getCurrentThreadCount()  
                + ", 当前活动线程数currentThreadsBusy=" + getCurrentThreadsBusy() + ",接收字节数 bytesReceived="  
                + getBytesReceived() + ",发送字节数 bytesSent=" + getBytesSent() + ",请求数 requestCount="  
                + getRequestCount() + ", 错误数errorCount=" + getErrorCount() + ", 处理时间processingTime="  
                + getProcessingTime() + ", 最大处理时间maxTime=" + getMaxTime() + ']';  
    }  
}  