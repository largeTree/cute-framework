package com.qiuxs.cuteframework.core.basic.config;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.qiuxs.cuteframework.core.basic.utils.ClassPathResourceUtil;
import com.qiuxs.cuteframework.core.basic.utils.ListUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.basic.utils.converter.XmlUtil;

/**
 * 默认的xml配置实现
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2019年12月21日 下午3:03:47 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
@SuppressWarnings("unchecked")
public class DefaultXMLConfiguration extends AbstractConfiguration {

	public DefaultXMLConfiguration(String merge) {
		super(merge);
	}

	public void addDocuments(List<Document> docs) {
		if (ListUtils.isNotEmpty(docs)) {
			for (Document doc : docs) {
				// 合并方式为最后一个时，每次添加文件时都清空配置
				if (UConfigUtils.MERGE_TYPE_LAST_ONE.equals(this.merge)) {
					super.clear();
				}
				addDocument(doc);
			}
		}
	}

	public void addDocument(Document document) {
		if (document == null) {
			throw new NullPointerException("Null document...");
		}
		Element rootElement = document.getRootElement();
		String rootPath = rootElement.getPath();
		Iterator<Element> eles = rootElement.elementIterator();
		this.parseElements(rootPath, eles);
	}

	private void parseElements(String rootPath, Iterator<Element> eles) {
		while (eles.hasNext()) {
			Element e = eles.next();
			this.parseElements(rootPath, e);
			this.parseElements(rootPath, e.elementIterator());
		}
	}

	private void parseElements(String rootPath, Element e) {
		String path = e.getPath().replace(rootPath + "/", "").replace("/", ".");
		String text = e.getTextTrim();
		if (StringUtils.isBlank(text)) {
			return;
		}
		if (UConfigUtils.MERGE_TYPE_REPLACE.equals(this.merge)) {
			// 合并方式
			super.put(path, text);
		} else if (UConfigUtils.MERGE_TYPE_ORDER.equals(this.merge)) {
			// 顺序方式
			if (!super.containsKey(path)) {
				super.put(path, text);
			}
		}
	}

	/**
	 * 按文件地址添加配置
	 *  
	 * @author qiuxs  
	 * @param paths
	 */
	public void addPaths(List<String> paths) {
		for (String path : paths) {
			this.addPath(path);
		}
	}

	/**
	 * 添加文件地址
	 *  
	 * @author qiuxs  
	 * @param path
	 */
	public void addPath(String path) {
		try {
			Document document = null;
			if (path.startsWith(UConfigUtils.CLASSPATH_PREFIX)) {
				// classpath下的文件
				document = ClassPathResourceUtil.getResourceXmlDoc(path);
				this.addDocument(document);
			} else if (path.startsWith(UConfigUtils.FILE_SYS_PREFIX)) {
				// 文件系统中的文件
				document = XmlUtil.readAsDocument(path);
			}
			if (document == null) {
				return;
			}
			this.addDocument(document);
		} catch (Exception e) {
			log.warn(path + ", config load failed...");
		}
	}

}
