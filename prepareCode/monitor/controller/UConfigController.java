package com.hzecool.frm.monitor.controller;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hzecool.core.context.EnvironmentHolder;
import com.hzecool.core.log.logger.Console;
import com.hzecool.fdn.uconfig.IConfiguration;
import com.hzecool.fdn.uconfig.UConfig;
import com.hzecool.fdn.uconfig.UConfigUtils;
import com.hzecool.fdn.utils.CollectionUtils;
import com.hzecool.fdn.utils.io.ClassPathResourceUtil;
import com.hzecool.fdn.utils.net.RequestUtils;
import com.hzecool.frm.context.MyEnvHolder;
import com.hzecool.tech.log4j2.jdbc.EcJdbcAppenderConfiguration;

/**
 * 
 * 功能描述: 环境变量参数
 * 新增原因: TODO<br/>  
 * 新增日期: 2017年4月22日 上午8:41:05 <br/>  
 *  
 * @author laisf   
 * @version 1.0.0
 */
@Controller
@RequestMapping({"/uconfig", "/devops/uconfig"})
public class UConfigController {

	@javax.annotation.Resource
	private EcJdbcAppenderConfiguration ecJdbcAppenderConfiguration; 
	/**
	 * 重新加载配置域
	 *  
	 * @author laisf  
	 * @param key
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/reload.do")
	@ResponseBody
	public String reload(HttpServletRequest req) throws Exception {
		String domain = RequestUtils.getStringValue(req, "domain", UConfig.CONFIG_DOMAIN_MYENV);
		UConfigUtils.reloadConfig(domain);//重新载入
		if (UConfig.CONFIG_DOMAIN_MYENV.equals(domain)) {
			EnvironmentHolder.clearCache();
		} else if (UConfig.CONFIG_DOMAIN_LOG4J.equals(domain)) {
			final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
			ctx.reconfigure();
			ecJdbcAppenderConfiguration.reconfigureMyJdbcAppender();
//			LogManager.shutdown();
//			ConfigurationFactory.setConfigurationFactory(EcLog4jConfigurationFactory.getInstance());
		}
		Console.logger.info("Reload " + domain + " Success!");
		return print(req);
	}
	
	
	@RequestMapping("/print.do")
	@ResponseBody
	public String print(HttpServletRequest req) throws Exception {
		return printInner(req, "<br/>");
	}
	
	@RequestMapping("/show.do")
	@ResponseBody
	public String show(HttpServletRequest req) throws Exception {
		return printInner(req, "\r\n");
	}
	
	private String printInner(HttpServletRequest req, String seperator) throws Exception {
		String domain = RequestUtils.getStringValue(req, "domain", UConfig.CONFIG_DOMAIN_MYENV);
		if ("log4j".equalsIgnoreCase(domain)) {
			domain = UConfig.CONFIG_DOMAIN_LOG4J;
		} else {
			domain = domain.toLowerCase();
		}
		
		String key = RequestUtils.getStringValue(req, "key");
		
        IConfiguration uc = UConfigUtils.getConfig(domain); 
        if (uc != null) {
        	if(StringUtils.isNotBlank(key)){
    			return "{" + key + "=" + uc.getString(key) + "}";
        	} else {
        		Properties properties = uc.getProperties();
//        		return properties.toString();
        		
        		//每个属性占一行，并按字符排序。update by liushanhong 2017/6/23
        		String[] keys = properties.keySet().toArray(new String[0]);
        		Arrays.sort(keys, String.CASE_INSENSITIVE_ORDER);
        		StringBuilder sb = new StringBuilder();
        		for(String k : keys) {
        			sb.append(k + "=" + properties.getProperty(k) + seperator);
        		}
        		return sb.toString();
			}
		}
        return "no domain: " + domain;
	}

	@RequestMapping("/file.do")
	@ResponseBody
	public void file(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter writer = response.getWriter();
		String domain = RequestUtils.getStringValue(request, "domain", UConfig.CONFIG_DOMAIN_MYENV);
		List<String> locationList =  UConfigUtils.getAvailableLocationList(domain);
		
		if (CollectionUtils.isEmpty(locationList)) {
			writer.write("no config location for domain: " + domain);
			writer.flush();
			return;
		}
		for (String location : locationList) {
			writer.println();
			writer.println();
			writer.println("-------------------->location:" + location + "<--------------------");
			writer.println();
			writer.println();
			
			List<Resource> resources = ClassPathResourceUtil.getResourceList(location);
			for (Resource res : resources) {
				writer.println();
				writer.println("--------->url:" + res.getURL().toString() + "<---------");
				writer.println();
				IOUtils.copy(res.getInputStream(), writer, "utf-8");
			}
		}

		writer.flush();
	}
	
	@RequestMapping("/setTemp.do")
	@ResponseBody
	public String setTemp(HttpServletRequest req) throws Exception {
		String domain = RequestUtils.getStringValue(req, "domain", UConfig.CONFIG_DOMAIN_MYENV);
		String keyStr = RequestUtils.getStringValue(req, "key");
		String valueStr = RequestUtils.getStringValue(req, "value");
		
		String msg = "Set " + domain + ": " + keyStr + "=" + valueStr;
		if (UConfig.CONFIG_DOMAIN_LOG4J.equals(domain) || "log4j".equals(domain)) {
			Level level = null;
			if ("debug".equalsIgnoreCase(valueStr)) {
				level = Level.DEBUG;
			} else if ("info".equalsIgnoreCase(valueStr)) {
				level = Level.INFO;
			} else if ("error".equalsIgnoreCase(valueStr)) {
				level = Level.ERROR;
			} else {
				return "Unsupport Level";
			}
			if (StringUtils.isEmpty(keyStr)) {
				final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
				Configuration config = ctx.getConfiguration();
				if (CollectionUtils.isNotEmpty(config.getLoggers())) {
					boolean updateFlag = false;
					for (LoggerConfig lc: config.getLoggers().values()) {
						if (!lc.getLevel().equals(level)) {
							lc.setLevel(level);
							updateFlag = true;
						}
					}
					if (updateFlag) {
						ctx.updateLoggers();
					}
				}
			} else {
				Configurator.setLevel(keyStr, level);//自动刷新；不存在会自动新增logger
			}
		} else if (UConfig.CONFIG_DOMAIN_MYENV.equals(domain)) {
			MyEnvHolder.setMyEnvParam(keyStr, valueStr);
		}
		msg = msg + " Success, Take Effect Already! Call 'reload.do?domain=log4j2' OR Restart Container To Reset! ";
		return msg;
	}
	
}
