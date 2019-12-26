package com.qiuxs.cuteframework.core.basic.config;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.qiuxs.cuteframework.core.basic.utils.ListUtils;

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
		Iterator<Element> eles = rootElement.elementIterator();
		this.parseElements(eles);
	}

	private void parseElements(Iterator<Element> eles) {
		while (eles.hasNext()) {
			Element e = eles.next();
			this.parseElements(e);
			this.parseElements(e.elementIterator());
		}
	}

	private void parseElements(Element e) {
		String path = e.getPath();
		if (UConfigUtils.MERGE_TYPE_REPLACE.equals(this.merge)) {
			// 合并方式
			super.put(path, e.getText());
		} else if (UConfigUtils.MERGE_TYPE_ORDER.equals(this.merge)) {
			// 顺序方式
			if (!super.containsKey(path)) {
				super.put(path, e.getText());
			}
		}
	}

}
