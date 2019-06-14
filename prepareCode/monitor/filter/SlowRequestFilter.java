package com.hzecool.frm.monitor.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.hzecool.core.context.EnvironmentHolder;
import com.hzecool.core.log.mgr.BaseLogManager;
import com.hzecool.fdn.Constant.BooleanInt;
import com.hzecool.fdn.utils.generator.RandomGenerator;
import com.hzecool.fdn.utils.net.RequestUtils;
import com.hzecool.frm.action.ActionConstants;
import com.hzecool.frm.monitor.bean.LogInfoBean;
import com.hzecool.frm.monitor.thread.LogRequestSlowThread;

public class SlowRequestFilter implements Filter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		req.setCharacterEncoding("UTF-8");
		// 不能和前面try合在一起，因为要保证chain.doFilter的执行(=转servlet等)
		try {
			String reqsn = req.getParameter(ActionConstants.PARAM_CLI_REQ_ID);
			if (StringUtils.isEmpty(reqsn)) {
				reqsn = BaseLogManager.getReqsn();//生成线程标识
			}
			
			String url = req.getServletPath();
			String apiKey = req.getParameter(ActionConstants.PARAM_APIKEY);
			if (StringUtils.isNotEmpty(apiKey)) {
				url += "?apiKey=" + apiKey;
			} else {
				String ifcId = req.getParameter("interfaceid");
				if (StringUtils.isNotEmpty(ifcId)) {
					url += "?interfaceid=" + ifcId;
				}
			}
			
			
			long start = System.currentTimeMillis();
			LogInfoBean logInfoBean = new LogInfoBean();
			logInfoBean.setStart(start);
			logInfoBean.setRequrl(url);//req.getServletPath()
			logInfoBean.setReqsn(reqsn);
			req.setAttribute(RequestUtils.PARAM_KEY_REQ_SN, reqsn);
			chain.doFilter(req, response);
			//结束时
			logRequestSlow(reqsn, logInfoBean, BooleanInt.TRUE.value());
		} catch (Exception e) {
	        e.printStackTrace();
        }
		
	}
	/**
	 * 生成线程标识： +2位随机数， 光用时间戳reqsn还会有重复的情况
	 * 该方法作废，移到BaseLogManager lsh updated 2017/5/16
	 * @author laisf  
	 * @return
	 */
	@Deprecated
	public static String getReqsn(){
		long now = System.currentTimeMillis();
		String reqsn = "reqsn=" + now + RandomGenerator.randomTwoDigit();
		return reqsn;
	}

	/**
	 * 记录长请求，在myenv.xml设置参数是否开启
	 *  logRequestSlow 是否开启长请求   true/false
	 *  logRequestSlowSecond 长请求时间长度，单位秒
	 * @author laisf  
	 * @param start
	 */
	public static void logRequestSlow(String reqsn, LogInfoBean bean, int finished) {
		// 是否记录长请求
		String logRequestSlow = EnvironmentHolder.getEnvParam("logRequestSlow");
		if ("true".equals(logRequestSlow)) {
			// 定义长请求时长下限，单位为秒
			String logRequestSlowSecond = EnvironmentHolder.getEnvParam("logRequestSlowSecond");
			if (StringUtils.isNotBlank(logRequestSlowSecond)) {
				long diff = Integer.parseInt(logRequestSlowSecond);
				long cost = System.currentTimeMillis() - bean.getStart();
				if (cost >= diff * 1000) {
					// 异步线程处理，避免因为日志原因导致主线程不成功
					LogRequestSlowThread t = new LogRequestSlowThread();
					t.setLogInfoBean(bean);
					t.setCost(new Long(cost / 1000).intValue());
					t.setFinished(finished);
					t.start();
				}
			}
		}
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
