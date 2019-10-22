package com.qiuxs.cuteframework.common.biz.func.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.common.biz.func.entity.Func;
import com.qiuxs.cuteframework.common.biz.func.service.IFuncService;
import com.qiuxs.cuteframework.core.basic.constants.SymbolConstants;
import com.qiuxs.cuteframework.core.basic.utils.ClassPathResourceUtil;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.ListUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.basic.utils.converter.XmlUtil;
import com.qiuxs.cuteframework.core.basic.utils.reflect.FieldUtils;

/**
 * 功能菜单初始化工具类
 * @author qiuxs
 *
 */
@Service
public class FuncInitHelper {

	private static Logger log = LogManager.getLogger(FuncInitHelper.class);

	private static final String FUNC_XML_PATH = ClassPathResourceUtil.CLASSPATH_ALL_URL_PREFIX + "/config/func/*.xml";

	private static final String MENU_QNAME = "menu";

	@javax.annotation.Resource
	private IFuncService funcService;
	
	private ThreadLocal<AtomicInteger> tlShowOrder = new ThreadLocal<>();

	/**
	 * 初始化所有功能菜单
	 */
	public void init() {
		List<Resource> resList = ClassPathResourceUtil.getResourceList(FUNC_XML_PATH);
		if (resList.size() == 0) {
			return;
		}
		this.tlShowOrder.set(new AtomicInteger(0));
		for (Resource res : resList) {
			try {
				Document funcXml = ClassPathResourceUtil.getResourceXmlDoc(res);
				Element rootE = funcXml.getRootElement();
				List<Func> allFunc = this.parseChildren("", rootE);
				this.saveFuncs(allFunc);
			} catch (IOException | DocumentException e) {
				StringBuilder sb = new StringBuilder("init Func Failed");
				if (res != null && res.exists()) {
					sb.append(", res = ").append(res.getFilename());
				}
				sb.append(", Ext = ").append(e.getLocalizedMessage());
				log.error(sb.toString(), e);
				throw ExceptionUtils.unchecked(e);
			}
		}
	}

	/**
	 * 解析子节点，并向下轮询
	 * @param parentId
	 * @param parentE
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Func> parseChildren(String parentId, Element parentE) {
		List<Element> childrenE = parentE.elements(MENU_QNAME);
		if (ListUtils.isNullOrEmpty(childrenE)) {
			return ListUtils.emptyList();
		}
		List<Func> children = new ArrayList<>(childrenE.size());
		for (Element childE : childrenE) {
			Func child = this.parseElement(parentId, childE);
			children.add(child);
			children.addAll(this.parseChildren(child.getId(), childE));
		}
		return children;
	}

	/**
	 * 解析单个节点
	 * @param parentId
	 * @param childE
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Func parseElement(String parentId, Element childE) {
		AtomicInteger showOrder = this.tlShowOrder.get();
		Func func = new Func();
		try {
			XmlUtil.setBeanByElement(func, childE);
			func.setParentId(parentId);
			if (StringUtils.isNotBlank(parentId)) {
				func.setId(parentId + SymbolConstants.SEPARATOR_HYPHEN + func.getId());
			}
			func.setLevel(func.getId().split(SymbolConstants.SEPARATOR_HYPHEN).length);
			func.setShowOrder(showOrder.incrementAndGet());
			
			Set<String> allFieldNames = FieldUtils.getDeclaredFieldNames(Func.class);
			
			JSONObject extra = new JSONObject();
			
			List<Attribute> attrs = childE.attributes();
			for (Attribute attr : attrs) {
				String name = attr.getName();
				if (!allFieldNames.contains(name)) {
					extra.put(name, attr.getValue());
				}
			}
			if (extra.size() > 0) {
				func.setExtra(extra.toJSONString());
			}
			return func;
		} catch (Exception e) {
			log.error("parse Element Failed Ext = " + e.getLocalizedMessage(), e);
		}
		return null;
	}

	/**
	 * 保存功能菜单到数据库
	 * @param allFunc
	 */
	private void saveFuncs(List<Func> allFunc) {
		if (ListUtils.isNullOrEmpty(allFunc)) {
			return;
		}
		List<Func> oldFuncs = this.funcService.getAll();
		Map<String, Func> oldFuncMap = oldFuncs.stream().collect(Collectors.toMap(Func::getId, val -> val));
		for (Func func : allFunc) {
			String id = func.getId();
			Func oldFunc = oldFuncMap.remove(id);
			if (oldFunc != null) {
				this.funcService.update(func);
			} else {
				this.funcService.create(func);
			}
		}
		if (oldFuncMap.size() > 0) {
			for (String id : oldFuncMap.keySet()) {
				this.funcService.deleteById(id);
			}
		}
	}

}
