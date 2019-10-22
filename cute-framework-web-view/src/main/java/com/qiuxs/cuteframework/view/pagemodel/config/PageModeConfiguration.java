package com.qiuxs.cuteframework.view.pagemodel.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.core.io.Resource;

import com.qiuxs.cuteframework.core.basic.utils.ClassPathResourceUtil;
import com.qiuxs.cuteframework.core.basic.utils.ListUtils;
import com.qiuxs.cuteframework.core.basic.utils.converter.XmlUtil;
import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.cuteframework.view.pagemodel.DataList;
import com.qiuxs.cuteframework.view.pagemodel.Field;
import com.qiuxs.cuteframework.view.pagemodel.FormModel;
import com.qiuxs.cuteframework.view.pagemodel.FormSection;
import com.qiuxs.cuteframework.view.pagemodel.Page;
import com.qiuxs.cuteframework.view.pagemodel.Search;
import com.qiuxs.cuteframework.view.pagemodel.Table;
import com.qiuxs.cuteframework.view.pagemodel.Td;
import com.qiuxs.cuteframework.view.pagemodel.TdBtn;

/**
 * 页面模型配置
 * @author qiuxs
 *
 */
public class PageModeConfiguration {

	private static Logger log = LogManager.getLogger(PageModeConfiguration.class);

	private static final String PAGE_CONFIG_PATH_PREFIX = "classpath*:/config/pages/**/";
	private static final String PAGE_CONFIG_PATH_SUFFIX = ".xml";
	private static final String PAGE_CONFIG_PATH = PAGE_CONFIG_PATH_PREFIX + "*" + PAGE_CONFIG_PATH_SUFFIX;

	private static Map<String, Page> pageModels;

	/**
	 * 获取一个页面配置
	 * @param pageId
	 * @return
	 */
	public static Page getPage(String pageId) {
		if (EnvironmentContext.getEnvContext().isDebug()) {
			initById(pageId);
		}
		return pageModels.get(pageId);
	}

	/**
	 * 初始化所有页面配置
	 */
	public static void init() {
		List<Resource> pageConfigs = ClassPathResourceUtil.getResourceList(PAGE_CONFIG_PATH);
		Map<String, Page> pageModels = new HashMap<String, Page>();
		for (Resource res : pageConfigs) {
			Page page = initByRes(res);
			if (page != null) {
				pageModels.put(page.getId(), page);
			}
		}
		PageModeConfiguration.pageModels = pageModels;
	}

	private static Page initByRes(Resource res) {
		try {
			Document doc = ClassPathResourceUtil.getResourceXmlDoc(res);
			Element root = doc.getRootElement();
			Page page = new Page();
			// 查询列表
			parseDataList(page, root);
			// 表单
			parseForms(page, root);
			page.setId(res.getFilename().split("\\.")[0]);
			log.info("inited PageConfig[" + res.getDescription() + "]");
			return page;
		} catch (Exception e) {
			log.error("Init PageMode Failed " + res.getDescription() + ", ext = " + e.getLocalizedMessage(), e);
		}
		return null;
	}

	/**
	 * 解析表单列表
	 * @param page
	 * @param root
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private static void parseForms(Page page, Element root) throws Exception {
		List<Element> formElements = root.elements("from");
		Map<String, FormModel> forms = new HashMap<>();
		for (Element formE : formElements) {
			FormModel formModel = new FormModel();
			parseForm(formModel, formE);
			forms.put(formModel.getId(), formModel);
		}
		page.setMapForms(forms);
	}

	/**
	 * 解析单个表单
	 * @param formModel
	 * @param formE
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private static void parseForm(FormModel formModel, Element formE) throws Exception {
		XmlUtil.setBeanByElement(formModel, formE);
		List<Element> fieldsE = formE.elements("field");
		List<Field> fields = parseFields(fieldsE);
		formModel.setFields(fields);
		List<Element> sectionsE = formE.elements("section");
		List<FormSection> sections = new ArrayList<>();
		if (ListUtils.isNotEmpty(sectionsE)) {
    		for (Element sectionE : sectionsE) {
    			FormSection section = new FormSection();
    			XmlUtil.setBeanByElement(section, sectionE);
    			List<Element> sectionFs = sectionE.elements("field");
    			List<Field> sectionFields = parseFields(sectionFs);
    			section.setFields(sectionFields);
    			sections.add(section);
    		}
		}
		formModel.setSections(sections);
	}

	/**
	 * 解析查询列表
	 * @param page
	 * @param root
	 * @throws Exception 
	 */
	private static void parseDataList(Page page, Element root) throws Exception {
		Element dataList = root.element("dataList");
		if (dataList == null) {
			return;
		}
		DataList dl = new DataList();
		String apiKey = dataList.attributeValue("apiKey");
		if (apiKey == null) {
			throw new RuntimeException("Element[DataList] must set apiKey");
		}
		dl.setApiKey(apiKey);
		Element search = dataList.element("search");
		@SuppressWarnings("unchecked")
		List<Element> searchFields = search.elements("field");
		// 查询条件配置
		Search searchModel = new Search();
		dl.setSearch(searchModel);
		List<Field> fields = parseFields(searchFields);
		searchModel.setFields(fields);

		// 数据表格配置
		Table table = new Table();
		Element tableE = dataList.element("table");
		@SuppressWarnings("unchecked")
		List<Element> tdEles = tableE.elements("td");
		List<Td> tds = new ArrayList<>();
		for (Element e : tdEles) {
			Td td = new Td();
			XmlUtil.setBeanByElement(td, e);
			tds.add(td);
			if (Td.TD_TYPE_BTN.equals(td.getType())) {
				// 初始化按钮列表
				initTdBtns(td, e);
			}
		}
		table.setTds(tds);
		dl.setTable(table);
		page.setDataList(dl);
	}

	/**
	 * 初始化行内按钮列表
	 * @param td
	 * @param e
	 * @throws Exception 
	 */
	private static void initTdBtns(Td td, Element tdE) throws Exception {
		List<TdBtn> btns = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<Element> btnEles = tdE.elements("btn");
		for (Element btnE : btnEles) {
			TdBtn btn = new TdBtn();
			XmlUtil.setBeanByElement(btn, btnE);
			btns.add(btn);
		}
		td.setBtns(btns);
	}

	/**
	 * 解析字段
	 * @param fieldEles
	 * @return
	 * @throws Exception
	 */
	private static List<Field> parseFields(List<Element> fieldEles) throws Exception {
		List<Field> fields = new ArrayList<Field>();
		for (Element e : fieldEles) {
			Field f = new Field();
			XmlUtil.setBeanByElement(f, e);
			fields.add(f);
		}
		return fields;
	}

	/**
	 * 根据页面ID初始化一个配置
	 * @param pageId
	 */
	private static void initById(String pageId) {
		Resource res = ClassPathResourceUtil.getSingleResource(PAGE_CONFIG_PATH_PREFIX + pageId + PAGE_CONFIG_PATH_SUFFIX);
		Page page = initByRes(res);
		PageModeConfiguration.pageModels.put(pageId, page);
	}

}
