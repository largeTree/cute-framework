package com.qiuxs.cuteframework.web.common.biz.func.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.core.basic.code.tree.TreeItem;
import com.qiuxs.cuteframework.core.basic.utils.JsonUtils;
import com.qiuxs.cuteframework.core.basic.utils.MapUtils;
import com.qiuxs.cuteframework.core.persistent.database.modal.BaseField;
import com.qiuxs.cuteframework.core.persistent.database.modal.PropertyWrapper;
import com.qiuxs.cuteframework.core.persistent.database.service.AbstractDataPropertyService;
import com.qiuxs.cuteframework.core.persistent.database.service.filter.IServiceFilter;
import com.qiuxs.cuteframework.core.persistent.database.service.filter.impl.IdGenerateFilter;
import com.qiuxs.cuteframework.web.common.biz.func.dao.FuncDao;
import com.qiuxs.cuteframework.web.common.biz.func.entity.Func;
import com.qiuxs.cuteframework.web.common.biz.func.service.IFuncService;
/**
 * 功能菜单表服务类
 *
 * @author qiuxs
 *
 */
@Service
public class FuncService extends AbstractDataPropertyService<String, Func, FuncDao> implements IFuncService {

	private static final String TABLE_NAME = "ct_func";
	private static final String PK_FIELD = "id";

	public FuncService() {
		super(String.class, Func.class, TABLE_NAME, PK_FIELD);
	}

	@Resource
	private FuncDao funcDao;

	@Override
	protected FuncDao getDao() {
		return this.funcDao;
	}
	
	@Override
	public List<TreeItem> funcTree(String parentId, boolean includeSub) {
		// 父级id为空时查找顶级菜单
		if (parentId == null) {
			parentId = "";
		}
		Map<String, Object> params = MapUtils.genMap("parentId", parentId, "orderBy", "showOrder");
		List<Func> funcs = this.findByMap(params);
		List<TreeItem> funcTree = new ArrayList<>(funcs.size());
		for (Func func : funcs) {
			String id = func.getId();
			TreeItem treeItem = new TreeItem();
			treeItem.setId(id);
			treeItem.setName(func.getName());
			String extra = func.getExtra();
			if (extra != null) {
				treeItem.setAttr(JsonUtils.parseJSONObject(extra));
			}
			if (includeSub) {
				treeItem.setChildren(this.funcTree(id, includeSub));
			}
			funcTree.add(treeItem);
		}
		return funcTree;
	}
	
	@Override
	protected void initServiceFilters(List<IServiceFilter<String, Func>> serviceFilters) {
		serviceFilters.add(new IdGenerateFilter<>(TABLE_NAME));
	}

	@Override
	protected void initProps(List<PropertyWrapper<?>> props) {
		super.initProps(props);
		
		PropertyWrapper<?> prop = null;
		
		prop = new PropertyWrapper<String>(new BaseField("parentId", "上级菜单ID", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<Integer>(new BaseField("level", "菜单深度", Integer.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<Integer>(new BaseField("funcType", "菜单类型", Integer.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<String>(new BaseField("name", "菜单名", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<Long>(new BaseField("termCap", "终端类型", Long.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<Integer>(new BaseField("showOrder", "显示顺序", Integer.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<String>(new BaseField("extra", "扩展属性", "json"), null);
		props.add(prop);
		
		prop = new PropertyWrapper<String>(new BaseField("rem", "备注", String.class), null);
		props.add(prop);
	}

}
