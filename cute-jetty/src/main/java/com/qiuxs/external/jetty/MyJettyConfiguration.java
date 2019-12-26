package com.qiuxs.external.jetty;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import org.eclipse.jetty.util.resource.PathResource;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.AbstractConfiguration;
import org.eclipse.jetty.webapp.MetaData;
import org.eclipse.jetty.webapp.Ordering;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * 
 * 功能描述: 沟入Jetty启动过程.
 * 1. 支持扫描classes/文件夹下的web-fragment.xml等；默认仅扫描WEB-INF/lib/xx.jar中的。
 * 2. 支持web-fragment.xml中配置<filter-mapping>和<servlet-mapping>，默认会报重复的错误。
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class MyJettyConfiguration extends AbstractConfiguration {

	@Override
	public void preConfigure(final WebAppContext context) throws Exception {
		//this.getClass().getClassLoader().getURLs()只包含jetty相关的jar目录
		ClassLoader cl = context.getClassLoader();//this.getClass()
		URL[] urls = ((URLClassLoader) cl).getURLs();
		for (URL url : urls) {
			if (new File(url.toURI()).isDirectory()) {
				Resource resource = new PathResource(new File(url.toURI()));
				//1. MetaInfConfiguration#preconfigure()->#scanJars()->#scanForFragment()放入context._attributes["org.eclipse.jetty.webFragments"]
				//2. FragmentConfiguration#preconfigure()->#findWebFragments()中根据轮询context._attributes["org.eclipse.jetty.webFragments"]放入metaData._webFragmentResourceMap
				//3. start()过程中：MetaData#resolve()中根据_webInfJar从_webFragmentResourceMap中获取fragment，从而添加listener等
				//3.1 ContextHandler#_contextListeners
				//	        	context.getMetaData().addContainerResource(resource);
				context.getMetaData().addWebInfJar(resource);
			}
		}
	}

	@Override
	public void configure(WebAppContext context) throws Exception {
		//不进行如下处理的异常：java.lang.IllegalStateException: Multiple servlets map to path: /metrics/admin/*: metrics-admin,metrics-admin
		//原因：
		//1. FragmentConfiguration#preconfigure()->#findWebFragments()->MetaData#addFragment()->#orderFragment()
		//2. MetaData#orderFragment()内部列表不清空，导致每多调用一次，MetaData#_orderedWebInfResources就重复一份
		//3. MetaData#resolve()时根据MetaData#_orderedWebInfResources解析mapping，导致重复
		/*		List<Resource> orderedWebInfResources = context.getMetaData().getOrderedWebInfJars();
				Set<Resource> orderedResourceSet = new HashSet<Resource>(orderedWebInfResources);
				orderedWebInfResources.clear();
				orderedWebInfResources.addAll(orderedResourceSet);*/
		MetaData metaData = context.getMetaData();
		metaData.setOrdering(new Ordering.RelativeOrdering(metaData));//自带排序操作
	}
}
