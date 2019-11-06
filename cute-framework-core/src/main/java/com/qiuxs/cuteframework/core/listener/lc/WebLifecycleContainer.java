package com.qiuxs.cuteframework.core.listener.lc;

import java.lang.instrument.IllegalClassFormatException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.core.io.Resource;

import com.qiuxs.cuteframework.core.basic.utils.ClassPathResourceUtil;
import com.qiuxs.cuteframework.core.basic.utils.ClassUtils;
import com.qiuxs.cuteframework.core.basic.utils.converter.XmlUtil;
import com.qiuxs.cuteframework.core.log.Console;

/**
 * web生命周期容器
 * 功能描述: <p>  
 * 新增原因: TODO<p>  
 * 新增日期: 2019年11月6日 下午6:20:38 <p>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class WebLifecycleContainer {

	private static final String PATH = "classpath*:/config/lifecycle.xml";

	private static List<IWebLifecycle> lifecycles;

	@SuppressWarnings("unchecked")
	private static void init() {
		try {
			List<Resource> ress = ClassPathResourceUtil.getResourceList(PATH);
			List<IWebLifecycle> lifecycles = new ArrayList<IWebLifecycle>();
			for (Resource res : ress) {
				Document doc = XmlUtil.readAsDocument(res);
				Element root = doc.getRootElement();
				Iterator<Element> lcIter = root.elementIterator("lc");
				while (lcIter.hasNext()) {
					Element e = lcIter.next();
					String clzName = e.attributeValue("class");
					if (clzName == null) {
						throw new IllegalClassFormatException("className not allow Empty in " + res);
					}
					Class<IWebLifecycle> clz = ClassUtils.forName(clzName);
					IWebLifecycle webLifecycle = clz.newInstance();
					lifecycles.add(webLifecycle);
					Console.log.info("Add WebLifecycle[" + clzName + "], order = " + webLifecycle.order());
				}
			}
			lifecycles.sort(new Comparator<IWebLifecycle>() {
				@Override
				public int compare(IWebLifecycle o1, IWebLifecycle o2) {
					int order = o1.order();
					int order2 = o2.order();
					return order > order2 ? 1 : order == order2 ? 0 : -1;
				}
			});
			WebLifecycleContainer.lifecycles = lifecycles;
		} catch (Throwable e) {
			Console.log.error("init Lifecycle.xml Failed ext = " + e.getLocalizedMessage(), e);
		}
	}

	public static List<IWebLifecycle> getLifecycles() {
		if (lifecycles == null) {
			init();
		}
		return lifecycles;
	}

}
