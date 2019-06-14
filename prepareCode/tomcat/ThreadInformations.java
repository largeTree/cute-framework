package com.hzecool.frm.monitor.tomcat;

import java.io.Serializable;  
import java.lang.management.ManagementFactory;  
import java.lang.management.ThreadMXBean;  
import java.net.InetAddress;  
import java.net.UnknownHostException;  
import java.util.ArrayList;  
import java.util.Arrays;  
import java.util.Collections;  
import java.util.List;  
import java.util.Map;  


public class ThreadInformations implements Serializable {  
    private static final long serialVersionUID = 3604281253550723654L;  
      
      
    private final String name;//线程名称  
    private final long id;//线程id  
    private final int priority;//线程有限度  
    private final boolean daemon;//是不是守护线程  
    private final Thread.State state;//线程状态  
    private final long cpuTimeMillis;//返回指定 ID 的线程的总 CPU 时间  
    private final long userTimeMillis;//返回指定 ID 的线程在用户模式中执行的 CPU 时间  
    private final boolean deadlocked;//是否死锁  
    private final String globalThreadId;//全局线程id名称  
    private final List<StackTraceElement> stackTrace;  
  
    private ThreadInformations(Thread thread, List<StackTraceElement> stackTrace, long cpuTimeMillis,  
            long userTimeMillis, boolean deadlocked, String hostAddress) {  
        super();  
  
        this.name = thread.getName();  
        this.id = thread.getId();  
        this.priority = thread.getPriority();  
        this.daemon = thread.isDaemon();  
        this.state = thread.getState();  
        this.stackTrace = stackTrace;  
        this.cpuTimeMillis = cpuTimeMillis;  
        this.userTimeMillis = userTimeMillis;  
        this.deadlocked = deadlocked;  
        this.globalThreadId = buildGlobalThreadId(thread, hostAddress);  
    }  
  
    public static List<ThreadInformations> buildThreadInformationsList() {  
        final ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();  
        final Map<Thread, StackTraceElement[]> stackTraces = Thread.getAllStackTraces();  
        final List<Thread>    threads = new ArrayList<Thread>(stackTraces.keySet());  
  
        //虚拟机是否允许测量所有线程的cup时间  
         //isThreadCpuTimeSupported() 方法可用于确定 Java 虚拟机是否支持测量任何线程的 CPU 时间。  
        //isCurrentThreadCpuTimeSupported() 方法可用于确定 Java 虚拟机是否支持测量当前线程的 CPU 时间。  
        //支持任何线程 CPU 时间测量的 Java 虚拟机实现也支持当前线程的 CPU 时间测量  
        final boolean cpuTimeEnabled = threadBean.isThreadCpuTimeSupported() && threadBean.isThreadCpuTimeEnabled();  
        //获取所有死锁线程的id  
        final long[] deadlockedThreads = getDeadlockedThreads(threadBean);  
        final List<ThreadInformations> threadInfosList = new ArrayList<ThreadInformations>(threads.size());  
          
        String hostAddress;  
        try {  
            hostAddress = InetAddress.getLocalHost().getHostAddress();  
        } catch (UnknownHostException e) {  
            hostAddress = "unknown";  
        }  
          
        for (final Thread thread : threads) {  
            final StackTraceElement[] stackTraceElements = stackTraces.get(thread);  
            final List<StackTraceElement> stackTraceElementList = stackTraceElements == null ? null  
                    : new ArrayList<StackTraceElement>(Arrays.asList(stackTraceElements));  
            final long cpuTimeMillis;//返回指定 ID 的线程的总 CPU 时间（以毫微秒为单位）。  
            final long userTimeMillis;//返回指定 ID 的线程在用户模式中执行的 CPU 时间（以毫微秒为单位）。  
            if (cpuTimeEnabled) {  
                cpuTimeMillis = threadBean.getThreadCpuTime(thread.getId()) / 1000000;  
                userTimeMillis = threadBean.getThreadUserTime(thread.getId()) / 1000000;  
            } else {  
                cpuTimeMillis = -1;  
                userTimeMillis = -1;  
            }  
            final boolean deadlocked = deadlockedThreads != null  
                    && Arrays.binarySearch(deadlockedThreads, thread.getId()) >= 0;  
            threadInfosList.add(new ThreadInformations(thread, stackTraceElementList,  
                    cpuTimeMillis, userTimeMillis, deadlocked, hostAddress));  
        }  
        return threadInfosList;  
    }  
    /** 
     * 获取所有的死锁线程id 
     * @param threadBean 
     * @return 
     */  
    private static long[] getDeadlockedThreads(ThreadMXBean threadBean) {  
        final long[] deadlockedThreads;  
        //这方法是jdk1.6才提供的，简单点，在这就不做检查了，  
        if (threadBean.isSynchronizerUsageSupported()) {  
            deadlockedThreads = threadBean.findDeadlockedThreads();  
        } else {  
            deadlockedThreads = threadBean.findMonitorDeadlockedThreads();  
        }  
        if (deadlockedThreads != null) {  
            Arrays.sort(deadlockedThreads);  
        }  
        return deadlockedThreads;  
    }  
      
  
    public String getName() {  
        return name;  
    }  
  
    public long getId() {  
        return id;  
    }  
  
    public int getPriority() {  
        return priority;  
    }  
  
    public boolean isDaemon() {  
        return daemon;  
    }  
  
    public Thread.State getState() {  
        return state;  
    }  
  
    public List<StackTraceElement> getStackTrace() {  
        if (stackTrace != null) {  
            return Collections.unmodifiableList(stackTrace);  
        }  
        return stackTrace;  
    }  
  
    public String getExecutedMethod() {  
        final List<StackTraceElement> trace = stackTrace;  
        if (trace != null && !trace.isEmpty()) {  
            return trace.get(0).toString();  
        }  
        return "";  
    }  
  
    public long getCpuTimeMillis() {  
        return cpuTimeMillis;  
    }  
  
    public long getUserTimeMillis() {  
        return userTimeMillis;  
    }  
  
    public boolean isDeadlocked() {  
        return deadlocked;  
    }  
  
    public  String getGlobalThreadId() {  
        return globalThreadId;  
    }  
    
    public String getStackTraceText(){
    	StringBuffer sb = new StringBuffer();  
        for(StackTraceElement s : getStackTrace()){  
            sb.append(s.toString()).append("<br/>");  
        }  
        return sb.toString();
    }
  
    private static String buildGlobalThreadId(Thread thread, String hostAddress) {  
        return PID.getPID() + '_' + hostAddress + '_' + thread.getId();  
    }  
  
    /** {@inheritDoc} */  
    @Override  
    public String toString() {  
        StringBuffer sb = new StringBuffer();  
        sb.append("[线程id=" + getId()   
                +",线程具体id(pid_host_tid)="+getGlobalThreadId()  
                + ",线程名称 name=" + getName()   
                + ", 是否是守护线程daemon="+ isDaemon()   
                + ",线程优先度 priority=" + getPriority()   
                + ",是不是死锁 deadlocked=" + isDeadlocked()  
                + ", 运行状态state=" + getState()   
                +",线程使用CPU时间(毫秒)="+getCpuTimeMillis()  
                +",线程在用户模式下使用CPU时间(毫秒)="+getUserTimeMillis()  
                +",执行的方法="+getExecutedMethod());  
        if(getStackTrace() != null && !getStackTrace().isEmpty()){  
            sb.append("<br/>栈信息:");  
            for(StackTraceElement s : getStackTrace()){  
                sb.append("<br/>").append(s.toString());  
            }  
        }  
        sb.append( ']');  
          
        return sb.toString();  
    }  
}  